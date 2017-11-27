package ru.ginger.protocol

import java.util.UUID
import ru.ginger.model.TaskStatus.TaskStatus

case class TaskRequest(id: Option[UUID],
                       position: Int,
                       status: TaskStatus,
                       title: String,
                       description: Option[String])
