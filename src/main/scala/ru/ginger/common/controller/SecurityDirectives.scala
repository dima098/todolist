package ru.ginger.common.controller

import java.util.UUID
import akka.http.scaladsl.server.{Directive1, Directives}
import ru.ginger.common.component.ExecutionContextComponent
import ru.ginger.model.{Role, Session}
import ru.ginger.model.db.User
import ru.ginger.service.{SessionServiceComponent, UserServiceComponent}
import ru.ginger.common.utils.ValuesImplicits._
import scala.util.{Failure, Success}

trait SecurityDirectives extends Directives {
  this: ExecutionContextComponent
    with UserServiceComponent
    with SessionServiceComponent =>

  def userDirective(userId: UUID): Directive1[User] = {
    onComplete(userService.find(userId.some)) flatMap {
      case Success(Some(user)) => provide(user)
      case Success(None) => reject()
      case Failure(e) => reject()
    }
  }

  def clientIp: Directive1[String] = {
    extractClientIP.flatMap { ipAddress =>
      val ip = ipAddress.toOption.map(_.getHostAddress).getOrElse("localhost")
      provide(ip)
    }
  }

  def sessionDirective: Directive1[Session] = {
    clientIp.flatMap { ip =>
      headerValueByName("sessionId").flatMap { sessionId =>
        onComplete(sessionService.find(sessionId, ip)) flatMap {
          case Success(Some(session)) => provide(session)
          case Success(None) => reject()
          case Failure(e) => reject()
        }
      }
    }
  }

  def adminPermission: Directive1[User] =
    sessionDirective.flatMap { session =>
      userDirective(session.userId).flatMap {
        case user if user.role == Role.Admin => provide(user)
        case _ => reject()
      }
    }
}
