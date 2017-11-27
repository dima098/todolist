package ru.ginger.service

import ru.ginger.common.component.ExecutionContextComponent
import ru.ginger.exception.ConfirmationCodeNotFoundException
import ru.ginger.model.Session
import ru.ginger.protocol.{LoginConfirmationRequest, LoginRequest, SessionView}
import scala.concurrent.Future
import ru.ginger.common.utils.ValuesImplicits._

trait AuthorizationService {
  def login(request: LoginRequest): Future[Unit]
  def logout(session: Session): Future[Unit]
  def confirm(ip: String, request: LoginConfirmationRequest): Future[SessionView]
}

trait AuthorizationServiceComponent {
  def authorizationService: AuthorizationService
}

class AuthorizationServiceImpl extends AuthorizationService {
  this: ExecutionContextComponent
    with SessionServiceComponent
    with UserServiceComponent
    with ConfirmationServiceComponent =>

  override def login(request: LoginRequest): Future[Unit] = {
    for {
      code <- confirmationService.code(request.phone)
    } yield println(s"[ CODE = $code ] for [ PHONE = ${request.phone} ]")
  }

  override def logout(session: Session): Future[Unit] = {
    sessionService.remove(session.sessionId)
  }

  override def confirm(ip: String, request: LoginConfirmationRequest): Future[SessionView] = {
    confirmationService.check(request.phone, request.smsCode).flatMap {
      case true => userService.get(phone = request.phone.some)
        .flatMap(user => sessionService.create(Session(user.id, ip)))
        .map(Session.toView)
      case false => Future.failed(new ConfirmationCodeNotFoundException(request.phone, request.smsCode))
    }
  }
}
