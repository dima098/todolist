package ru.ginger.validation

import java.util.UUID

import ru.ginger.common.component.ExecutionContextComponent
import ru.ginger.service.UserServiceComponent
import ru.ginger.common.utils.ValuesImplicits._
import ru.ginger.exception.UserValidationException

import scala.concurrent.Future

trait UserValidationService {
  def validateCreate(phone: String): Future[Unit]
  def validateUpdate(id: UUID, phone: String): Future[Unit]
}

trait UserValidationServiceComponent {
  def userValidationService: UserValidationService
}

class UserValidationServiceImpl extends UserValidationService {
  this: ExecutionContextComponent
    with UserServiceComponent =>

  override def validateCreate(phone: String): Future[Unit] = {
    userService.count(phone = phone.some).flatMap {
      case 0 => Future.successful(())
      case _ => Future.failed(new UserValidationException(s"Unable to create user with [ PHONE = $phone ]"))
    }
  }

  override def validateUpdate(id: UUID, phone: String): Future[Unit] = {
    userService.count(id.some, phone.some).flatMap {
      case 0 | 1 => Future.successful(())
      case _ => Future.failed(new UserValidationException(s"Unable to update user with [ PHONE = $phone ]"))
    }
  }
}
