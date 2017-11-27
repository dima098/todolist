package ru.ginger.service

import java.util.UUID
import ru.ginger.common.component.DatabaseModelComponent.IOAction
import ru.ginger.common.component.{DatabaseRunnerComponent, ExecutionContextComponent}
import ru.ginger.dao.{AggregatedTaskListDaoComponent, TaskDaoComponent, TaskListDaoComponent}
import ru.ginger.model.db.{Task, TaskList}
import ru.ginger.model.{AggregatedTaskList, TaskListCreatedResponse}
import ru.ginger.protocol.TaskListRequest
import ru.ginger.service.TaskListService.SplitTasks
import slick.dbio.DBIOAction
import slick.dbio.Effect.{Transactional, Write}
import scala.concurrent.Future

trait TaskListService {
  def list(userId: UUID): Future[Seq[AggregatedTaskList]]
  def get(userId: UUID, id: UUID): Future[AggregatedTaskList]
  def create(userId: UUID, request: TaskListRequest): Future[TaskListCreatedResponse]
  def update(userId: UUID, id: UUID, request: TaskListRequest): Future[Unit]
  def remove(userId: UUID, id: UUID): Future[Unit]
}

trait TaskListServiceComponent {
  def taskListService: TaskListService
}

class TaskListServiceImpl extends TaskListService {
  this: ExecutionContextComponent
    with AggregatedTaskListDaoComponent
    with TaskListDaoComponent
    with TaskDaoComponent
    with DatabaseRunnerComponent =>

  override def list(userId: UUID): Future[Seq[AggregatedTaskList]] = {
    databaseRunner.run(aggregatedTaskListDao.list(userId))
  }

  override def get(userId: UUID, id: UUID): Future[AggregatedTaskList] = {
    databaseRunner.run(aggregatedTaskListDao.get(userId, id))
  }

  override def create(userId: UUID, request: TaskListRequest): Future[TaskListCreatedResponse] = {
    val taskList = TaskList.makeFrom(userId, UUID.randomUUID(), request)
    val tasks = request.tasks.map(Task.makeFrom(userId, taskList.id, _))

    databaseRunner.run {
      transactionally {
        for {
          _ <- taskListDao.create(taskList)
          _ <- taskDao.create(tasks)
        } yield TaskListCreatedResponse(taskList.id)
      }
    }
  }

  override def update(userId: UUID, id: UUID, request: TaskListRequest): Future[Unit] = {
    val taskList = TaskList.makeFrom(userId, id, request)

    def splitTasks(existingTasks: Seq[Task]): SplitTasks = {
      val existingTaskIds = existingTasks.map(_.id)
      val (tasksWithId, taskWithoutId) = request.tasks.partition(_.id.isEmpty)
      val (tasksForUpdate, tasksForDelete) = tasksWithId.partition(task => task.id.exists(existingTaskIds.contains))

      SplitTasks(
        delete = tasksForDelete.flatMap(_.id),
        update = tasksForUpdate.map(Task.makeFrom(userId, taskList.id, _)),
        create = taskWithoutId.map(Task.makeFrom(userId, taskList.id, _))
      )
    }

    def updateTaskList(existingTasks: Seq[Task]): IOAction[Unit, Write with Transactional] = {
      val SplitTasks(deleteIds, updateTasks, createTasks) = splitTasks(existingTasks)

      transactionally {
        for {
          _ <- taskListDao.update(taskList)
          _ <- taskDao.remove(userId, deleteIds, taskList.id)
          _ <- DBIOAction.sequence(updateTasks.map(taskDao.update))
          _ <- taskDao.create(createTasks)
        } yield ()
      }
    }

    databaseRunner.run {
      for {
        tasks <- taskDao.list(userId, Seq(taskList.id))
        _ <- updateTaskList(tasks)
      } yield ()
    }
  }

  override def remove(userId: UUID, id: UUID): Future[Unit] = {
    databaseRunner.run {
      transactionally {
        for {
          _ <- taskListDao.remove(userId, id)
          _ <- taskDao.remove(userId, id)
        } yield ()
      }
    }
  }
}

private[service] object TaskListService {
   case class SplitTasks(delete: Seq[UUID], update: Seq[Task], create: Seq[Task])
}

