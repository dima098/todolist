package ru.ginger.controller

import java.util.UUID
import akka.http.scaladsl.server.Route
import ru.ginger.common.component.ExecutionContextComponent
import ru.ginger.common.controller.{Controller, SecurityDirectives}
import ru.ginger.protocol.UserRequest
import ru.ginger.service.{SessionServiceComponent, UserServiceComponent, UserViewServiceComponent}
import ru.ginger.format.JsonFormat._
import ru.ginger.common.utils.ValuesImplicits._

class UserController extends Controller with SecurityDirectives {
  this: ExecutionContextComponent
    with UserServiceComponent
    with SessionServiceComponent
    with UserViewServiceComponent =>

  override def route: Route = pathPrefix("user") {
    path(JavaUUID) { userId =>
      getUser(userId) ~
      updateUser(userId) ~
      removeUser(userId)
    } ~
      pathEndOrSingleSlash {
        listUser ~
        createUser
      }
  }

  protected def getUser(id: UUID): Route = {
    (get & adminPermission) { admin =>
      completeFuture(userViewService.get(id.some))
    }
  }

  protected def listUser: Route = {
    (get & adminPermission & parameter("offset".as[Int] ? 0) & parameter("limit".as[Int] ? 50)) { (admin, offset, limit) =>
      completeFuture(userViewService.list(offset, limit))
    }
  }

  protected def createUser: Route = {
    (post & adminPermission & entity(as[UserRequest])) { (admin, request) =>
      completeFuture(userViewService.create(request))
    }
  }

  protected def updateUser(id: UUID): Route = {
    (put & adminPermission & entity(as[UserRequest])) { (admin, request) =>
      completeFuture(userViewService.update(id, request))
    }
  }

  protected def removeUser(id: UUID): Route = {
    (delete & adminPermission) { admin =>
      completeFuture(userViewService.remove(id))
    }
  }
}
