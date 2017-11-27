package ru.ginger.model.db

import java.util.UUID
import ru.ginger.protocol.TaskListRequest

case class TaskList(id: UUID,
                    userId: UUID,
                    title: String)

trait TaskListModel extends AbstractDBModel {

  import slick.jdbc.H2Profile.api._

  class TaskListTable(tag: Tag) extends Table[TaskList](tag, "TODO_TASK_LIST") {
    def id = column[UUID]("ID")
    def userId = column[UUID]("USER_ID")
    def title = column[String]("TITLE")

    override def * = (id, userId, title) <> ((TaskList.apply _).tupled, TaskList.unapply)
  }

  val taskList: TableQuery[TaskListTable] = TableQuery[TaskListTable]
}

object TaskList {
  def makeFrom(userId: UUID, id: UUID, request: TaskListRequest): TaskList = {
    TaskList(
      id = id,
      userId = userId,
      title = request.title
    )
  }
}