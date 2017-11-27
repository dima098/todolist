package ru.ginger.controller

import akka.http.scaladsl.server.Route
import ru.ginger.common.component.ExecutionContextComponent
import ru.ginger.common.controller.{Controller, SecurityDirectives}
import ru.ginger.protocol.{LoginConfirmationRequest, LoginRequest}
import ru.ginger.format.JsonFormat._
import ru.ginger.service.{AuthorizationServiceComponent, SessionServiceComponent, UserServiceComponent}
import ru.ginger.format.JsonFormat._

class AuthorizationController extends Controller with SecurityDirectives {
  this: ExecutionContextComponent
    with UserServiceComponent
    with SessionServiceComponent
    with AuthorizationServiceComponent =>

  override def route: Route =
    path("login") {
      login
    } ~
    path("confirm") {
      confirm
    } ~
    path("logout") {
      logout
    }

  protected def login: Route = {
    (post & entity(as[LoginRequest])) { request =>
      completeFuture(authorizationService.login(request))
    }
  }
  protected def logout: Route = {
    (post & sessionDirective) { session =>
      completeFuture(authorizationService.logout(session))
    }
  }

  protected def confirm: Route = {
    (post & clientIp & entity(as[LoginConfirmationRequest])) { (ip, request) =>
      completeFuture(authorizationService.confirm(ip, request))
    }
  }
}
