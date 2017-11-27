package ru.ginger.model

import java.util.UUID
import ru.ginger.model.TaskStatus.TaskStatus
import ru.ginger.model.db.Task
import ru.ginger.protocol.TaskView

case class AggregatedTask(id: UUID,
                          position: Int,
                          status: TaskStatus,
                          title: String,
                          description: Option[String])

object AggregatedTask {
  def makeFrom(task: Task): AggregatedTask = {
    AggregatedTask(
      id = task.id,
      position = task.position,
      status = task.status,
      title = task.title,
      description = task.description
    )
  }

  def toView(task: AggregatedTask): TaskView = {
    TaskView(
      id = task.id,
      position = task.position,
      status = task.status,
      title = task.title,
      description = task.description
    )
  }
}