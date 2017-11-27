package ru.ginger.dao

import java.util.UUID
import ru.ginger.common.component.{DatabaseModelComponent, ExecutionContextComponent}
import ru.ginger.common.component.DatabaseModelComponent.IOAction
import ru.ginger.model.db.{Task, TaskModel}
import slick.dbio.Effect.{Read, Write}

trait TaskDao {
  def list(userId: UUID, listIds: Seq[UUID]): IOAction[Seq[Task], Read]
  def create(tasks: Seq[Task]): IOAction[Unit, Write]
  def update(task: Task): IOAction[Int, Write]
  def remove(userId: UUID, ids: Seq[UUID], listId: UUID): IOAction[Int, Write]
  def remove(userId: UUID, listId: UUID): IOAction[Int, Write]
}

trait TaskDaoComponent {
  def taskDao: TaskDao
}

class TaskDaoImpl extends TaskDao with TaskQueryRepository {
  this: DatabaseModelComponent[TaskModel]
    with ExecutionContextComponent =>

  import api._
  import models._

  override def list(userId: UUID, listIds: Seq[UUID]): IOAction[Seq[Task], Read] =
    listTaskBy(userId, listIds).result

  override def create(tasks: Seq[Task]): IOAction[Unit, Write] =
    (models.task ++= tasks).map(_ => ())

  override def update(task: Task): IOAction[Int, Write] =
    taskBy(task.userId, task.id, task.listId)
      .map(t => (t.status, t.position, t.title, t.description))
      .update((task.status, task.position, task.title, task.description))

  override def remove(userId: UUID, ids: Seq[UUID], listId: UUID): IOAction[Int, Write] =
    listTaskBy(userId, Seq(listId)).filter(_.id inSetBind ids).delete

  override def remove(userId: UUID, listId: UUID): IOAction[Int, Write] =
    listTaskBy(userId, Seq(listId)).delete
}