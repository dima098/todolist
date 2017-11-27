package ru.ginger.wiring

import akka.http.scaladsl.server.{Route, RouteConcatenation}
import ru.ginger.controller.{AuthorizationController, TaskListController, UserController}
import ComponentWiring._

trait ControllerWiring {
  lazy val routes: Route = RouteConcatenation.concat(controllers.map(_.route): _*)

  // internal

  private lazy val controllers = Seq(
    new AuthorizationController
      with ExecutionContextComponentImpl
      with UserServiceComponentImpl
      with SessionServiceComponentImpl
      with AuthorizationServiceComponentImpl,
    new TaskListController
      with ExecutionContextComponentImpl
      with UserServiceComponentImpl
      with SessionServiceComponentImpl
      with TaskListViewServiceComponentImpl,
    new UserController
      with ExecutionContextComponentImpl
      with UserServiceComponentImpl
      with SessionServiceComponentImpl
      with UserViewServiceComponentImpl
  )
}