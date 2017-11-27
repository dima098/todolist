package ru.ginger.dao

import java.util.UUID
import ru.ginger.common.component.DatabaseModelComponent
import ru.ginger.model.db.TaskModel

trait TaskQueryRepository {
  this: DatabaseModelComponent[TaskModel] =>

  import api._

  def taskBy(userId: UUID, id: UUID, listId: UUID) =
    models.task.filter(t =>
      t.id === id.bind &&
      t.listId === listId.bind &&
      t.userId === userId.bind
    )

  def listTaskBy(userId: UUID, listIds: Seq[UUID]) =
    models.task.filter(t => t.listId.inSetBind(listIds) && t.userId === userId.bind)
}
