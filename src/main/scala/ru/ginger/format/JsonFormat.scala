package ru.ginger.format

import play.api.libs.json._
import ru.ginger.common.utils.JsonUtils._
import ru.ginger.model.Role.Role
import ru.ginger.model.TaskStatus.TaskStatus
import ru.ginger.model.{Role, TaskStatus}
import ru.ginger.protocol._

object JsonFormat {
  implicit val roleFormat: Format[Role] = formatEnumeration(Role)
  implicit val taskStatusFormat: Format[TaskStatus] = formatEnumeration(TaskStatus)

  implicit val taskRequestReads: Reads[TaskRequest] = Json.reads[TaskRequest]
  implicit val taskListRequestReads: Reads[TaskListRequest] = Json.reads[TaskListRequest]

  implicit val userRequestReads: Reads[UserRequest] = Json.reads[UserRequest]

  implicit val loginRequestReads: Reads[LoginRequest] = Json.reads[LoginRequest]
  implicit val loginConfirmationRequestReads: Reads[LoginConfirmationRequest] = Json.reads[LoginConfirmationRequest]

  implicit val taskListCreatedResponseViewWrites: Writes[TaskListCreatedResponseView] = Json.writes[TaskListCreatedResponseView]
  implicit val userCreatedResponseViewWrites: Writes[UserCreatedResponseView] = Json.writes[UserCreatedResponseView]

  implicit val taskViewWrites: Writes[TaskView] = Json.writes[TaskView]
  implicit val taskListViewWrites: Writes[TaskListView] = Json.writes[TaskListView]

  implicit val userViewWrites: Writes[UserView] = Json.writes[UserView]

  implicit val sessionViewWrites: Writes[SessionView] = Json.writes[SessionView]

  implicit val unitWrites: Writes[Unit] = { _ => JsString("Success") }
  implicit val errorResponseWrites: Writes[ErrorResponse] = Json.writes[ErrorResponse]
  implicit def successResponseWrites[T : Writes]: Writes[SuccessResponse[T]] = Json.writes[SuccessResponse[T]]
}
