package ru.ginger.model

import java.util.UUID

import ru.ginger.protocol.TaskListCreatedResponseView

case class TaskListCreatedResponse(id: UUID)

object TaskListCreatedResponse {
  def toView(response: TaskListCreatedResponse): TaskListCreatedResponseView = {
    TaskListCreatedResponseView(response.id)
  }
}
