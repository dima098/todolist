package ru.ginger.service

import java.util.UUID
import ru.ginger.common.component.ExecutionContextComponent
import ru.ginger.model.{AggregatedTaskList, TaskListCreatedResponse}
import ru.ginger.protocol.{TaskListCreatedResponseView, TaskListRequest, TaskListView}
import scala.concurrent.Future

trait TaskListViewService {
  def list(userId: UUID): Future[Seq[TaskListView]]
  def get(userId: UUID, id: UUID): Future[TaskListView]
  def create(userId: UUID, request: TaskListRequest): Future[TaskListCreatedResponseView]
  def update(userId: UUID, id: UUID, request: TaskListRequest): Future[Unit]
  def remove(userId: UUID, id: UUID): Future[Unit]
}

trait TaskListViewServiceComponent {
  def taskListViewService: TaskListViewService
}

class TaskListViewServiceImpl extends TaskListViewService {
  this: ExecutionContextComponent
    with TaskListServiceComponent =>

  override def list(userId: UUID): Future[Seq[TaskListView]] =
    taskListService.list(userId).map(_.map(AggregatedTaskList.toView))

  override def get(userId: UUID, id: UUID): Future[TaskListView] =
    taskListService.get(userId, id).map(AggregatedTaskList.toView)

  override def create(userId: UUID, request: TaskListRequest): Future[TaskListCreatedResponseView] =
    taskListService.create(userId, request).map(TaskListCreatedResponse.toView)

  override def update(userId: UUID, id: UUID, request: TaskListRequest): Future[Unit] =
    taskListService.update(userId, id, request)

  override def remove(userId: UUID, id: UUID): Future[Unit] =
    taskListService.remove(userId, id)
}