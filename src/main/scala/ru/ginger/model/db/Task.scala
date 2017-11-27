package ru.ginger.model.db

import java.util.UUID
import ru.ginger.model.TaskStatus
import ru.ginger.model.TaskStatus.TaskStatus
import ru.ginger.protocol.TaskRequest
case class Task(id: UUID,
                listId: UUID,
                userId: UUID,
                position: Int,
                status: TaskStatus,
                title: String,
                description: Option[String])

trait TaskModel extends AbstractDBModel {

  import slick.jdbc.H2Profile.api._

  implicit lazy val taskStatusMappedColumnType: BaseColumnType[TaskStatus] =
    MappedColumnType.base[TaskStatus, String](_.toString, TaskStatus.withName)

  class TaskTable(tag: Tag) extends Table[Task](tag, "TODO_TASK") {

    def id = column[UUID]("ID")
    def listId = column[UUID]("LIST_ID")
    def userId = column[UUID]("USER_ID")
    def position = column[Int]("POSITION")
    def status = column[TaskStatus]("STATUS")
    def title = column[String]("TITLE")
    def description = column[Option[String]]("DESCRIPTION")

    override def * = (id, listId, userId, position, status, title, description) <> ((Task.apply _).tupled, Task.unapply)
  }

  val task: TableQuery[TaskTable] = TableQuery[TaskTable]
}

object Task {
  def makeFrom(userId: UUID, listId: UUID, request: TaskRequest): Task = {
    Task(
      id = request.id.getOrElse(UUID.randomUUID()),
      listId = listId,
      userId = userId,
      position = request.position,
      status = request.status,
      title = request.title,
      description = request.description
    )
  }
}