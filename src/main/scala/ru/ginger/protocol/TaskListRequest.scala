package ru.ginger.protocol

case class TaskListRequest(title: String,
                           tasks: Seq[TaskRequest])