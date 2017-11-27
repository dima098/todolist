package ru.ginger.dao

import java.util.UUID

import ru.ginger.common.component.ExecutionContextComponent
import ru.ginger.common.component.DatabaseModelComponent.IOAction
import ru.ginger.model.AggregatedTaskList
import ru.ginger.model.db.{Task, TaskList}
import slick.dbio.Effect.Read

trait AggregatedTaskListDao {
  def list(userId: UUID): IOAction[Seq[AggregatedTaskList], Read]
  def get(userId: UUID, id: UUID): IOAction[AggregatedTaskList, Read]
}

trait AggregatedTaskListDaoComponent {
  def aggregatedTaskListDao: AggregatedTaskListDao
}

class AggregatedTaskListDaoImpl extends AggregatedTaskListDao {
  this: ExecutionContextComponent
    with TaskListDaoComponent
    with TaskDaoComponent =>

  override def list(userId: UUID): IOAction[Seq[AggregatedTaskList], Read] = {
    for {
      listTaskList <- taskListDao.list(userId)
      tasks <- taskDao.list(userId, listTaskList.map(_.id))
    } yield makeAggregatedTaskLists(listTaskList, tasks)
  }

  override def get(userId: UUID, id: UUID): IOAction[AggregatedTaskList, Read] = {
    for {
      taskList <- taskListDao.get(userId, id)
      tasks <- taskDao.list(userId, Seq(taskList.id))
    } yield AggregatedTaskList.makeFrom(taskList, tasks)
  }

  // internal

  def makeAggregatedTaskLists(listTaskList: Seq[TaskList], tasks: Seq[Task]): Seq[AggregatedTaskList] = {
    val groupedTasks = tasks.groupBy(_.listId)

    listTaskList.map { taskList =>
      val chosenTasks = groupedTasks.getOrElse(taskList.id, Seq.empty)
      AggregatedTaskList.makeFrom(taskList, chosenTasks)
    }
  }
}