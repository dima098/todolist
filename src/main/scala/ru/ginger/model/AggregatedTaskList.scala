package ru.ginger.model

import java.util.UUID
import ru.ginger.model.db.{Task, TaskList}
import ru.ginger.protocol.TaskListView

case class AggregatedTaskList(id: UUID,
                              title: String,
                              tasks: Seq[AggregatedTask])

object AggregatedTaskList {
  def makeFrom(taskList: TaskList, tasks: Seq[Task]): AggregatedTaskList = {
    AggregatedTaskList(
      id = taskList.id,
      title = taskList.title,
      tasks = tasks.map(AggregatedTask.makeFrom).sortBy(_.position)
    )
  }

  def toView(taskList: AggregatedTaskList): TaskListView = {
    TaskListView(
      id = taskList.id,
      title = taskList.title,
      tasks = taskList.tasks.map(AggregatedTask.toView)
    )
  }
}
