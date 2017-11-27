package ru.ginger.controller

import java.util.UUID
import akka.http.scaladsl.server.Route
import ru.ginger.common.component.ExecutionContextComponent
import ru.ginger.common.controller.{Controller, SecurityDirectives}
import ru.ginger.protocol.TaskListRequest
import ru.ginger.service.{SessionServiceComponent, TaskListViewServiceComponent, UserServiceComponent}
import ru.ginger.format.JsonFormat._

class TaskListController extends Controller with SecurityDirectives {
  this: ExecutionContextComponent
    with UserServiceComponent
    with SessionServiceComponent
    with TaskListViewServiceComponent =>

  override def route: Route = pathPrefix("tasks") {
    path(JavaUUID) { taskListId =>
      getTaskList(taskListId) ~
      updateTaskList(taskListId) ~
      removeTaskList(taskListId)
    } ~
    pathEndOrSingleSlash {
      listTaskList ~
      createTaskList
    }
  }

  protected def getTaskList(id: UUID): Route = {
    (get & sessionDirective) { session =>
      completeFuture(taskListViewService.get(session.userId, id))
    }
  }

  protected def listTaskList: Route = {
    (get & sessionDirective) { session =>
      completeFuture(taskListViewService.list(session.userId))
    }
  }

  protected def createTaskList: Route = {
    (post & sessionDirective & entity(as[TaskListRequest])) { (session, request) =>
      println(s"${session.userId} for creating list")
      completeFuture(taskListViewService.create(session.userId, request))
    }
  }
  protected def updateTaskList(id: UUID): Route = {
    (put & sessionDirective & entity(as[TaskListRequest])) { (session, request) =>
      completeFuture(taskListViewService.update(session.userId, id, request))
    }
  }

  protected def removeTaskList(id: UUID): Route = {
    (delete & sessionDirective) { session =>
      completeFuture(taskListViewService.remove(session.userId, id))
    }
  }
}
