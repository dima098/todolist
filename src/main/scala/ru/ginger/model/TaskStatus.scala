package ru.ginger.model

object TaskStatus extends Enumeration {
  type TaskStatus = TaskStatus.Value

  val Open = Value("open")
  val InProgress = Value("in_progress")
  val Closed = Value("closed")
}
