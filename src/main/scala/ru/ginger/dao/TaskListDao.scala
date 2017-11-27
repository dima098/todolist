package ru.ginger.dao

import java.util.UUID

import ru.ginger.common.component.{DatabaseModelComponent, ExecutionContextComponent}
import ru.ginger.common.component.DatabaseModelComponent.IOAction
import ru.ginger.exception.TaskListNotFoundException
import ru.ginger.model.db.{TaskList, TaskListModel}
import slick.dbio.Effect.{Read, Write}

trait TaskListDao {
  def list(userId: UUID): IOAction[Seq[TaskList], Read]
  def find(userId: UUID, id: UUID): IOAction[Option[TaskList], Read]
  def get(userId: UUID, id: UUID): IOAction[TaskList, Read]
  def update(taskList: TaskList): IOAction[Int, Write]
  def create(taskList: TaskList): IOAction[Unit, Write]
  def remove(userId: UUID, id: UUID): IOAction[Int, Write]
}

trait TaskListDaoComponent {
  def taskListDao: TaskListDao
}

class TaskListDaoImpl extends TaskListDao with TaskListQueryRepository {
  this: ExecutionContextComponent
    with DatabaseModelComponent[TaskListModel] =>

  import api._

  override def list(userId: UUID): IOAction[Seq[TaskList], Read] =
    listTaskListBy(userId).result

  override def find(userId: UUID, id: UUID): IOAction[Option[TaskList], Read] =
    taskListBy(userId, id).result.headOption

  override def get(userId: UUID, id: UUID): IOAction[TaskList, Read] =
    find(userId, id).flatMap {
      case Some(value) => DBIO.successful(value)
      case None => DBIO.failed(new TaskListNotFoundException(id, userId))
    }

  override def update(taskList: TaskList): IOAction[Int, Write] =
    taskListBy(taskList.userId, taskList.id)
      .map(_.title)
      .update(taskList.title)

  override def create(taskList: TaskList): IOAction[Unit, Write] =
    (models.taskList += taskList).map(_ => ())

  override def remove(userId: UUID, id: UUID): IOAction[Int, Write] =
    taskListBy(userId, id).delete
}
