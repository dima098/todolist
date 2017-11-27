package ru.ginger.protocol

import java.util.UUID

case class TaskListView(id: UUID,
                        title: String,
                        tasks: Seq[TaskView])
