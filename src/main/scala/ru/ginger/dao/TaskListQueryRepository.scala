package ru.ginger.dao

import java.util.UUID

import ru.ginger.common.component.DatabaseModelComponent
import ru.ginger.model.db.TaskListModel

trait TaskListQueryRepository {
  this: DatabaseModelComponent[TaskListModel] =>

  import api._

  protected def taskListBy(userId: UUID, id: UUID) =
    models.taskList.filter(tl => tl.id === id.bind && tl.userId === userId.bind)

  protected def listTaskListBy(userId: UUID) =
    models.taskList.filter(tl => tl.userId === userId.bind)
}
