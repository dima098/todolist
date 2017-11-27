package ru.ginger.service

import java.util.UUID
import ru.ginger.common.component.ExecutionContextComponent
import ru.ginger.model.UserCreatedResponse
import ru.ginger.model.db.User
import ru.ginger.protocol.{UserCreatedResponseView, UserRequest, UserView}
import scala.concurrent.Future

trait UserViewService {
  def get(id: Option[UUID] = None, phone: Option[String] = None): Future[UserView]
  def list(offset: Int, limit: Int): Future[Seq[UserView]]
  def create(request: UserRequest): Future[UserCreatedResponseView]
  def update(id: UUID, request: UserRequest): Future[Unit]
  def remove(id: UUID): Future[Unit]
}

trait UserViewServiceComponent {
  def userViewService: UserViewService
}

class UserViewServiceImpl extends UserViewService {
  this: ExecutionContextComponent
    with UserServiceComponent =>

  override def get(id: Option[UUID], phone: Option[String]): Future[UserView] =
    userService.get(id, phone).map(User.toView)

  override def list(offset: Int, limit: Int): Future[Seq[UserView]] =
    userService.list(offset, limit).map(_.map(User.toView))

  override def create(request: UserRequest): Future[UserCreatedResponseView] =
    userService.create(request).map(UserCreatedResponse.toView)

  override def update(id: UUID, request: UserRequest): Future[Unit] =
    userService.update(id, request)

  override def remove(id: UUID): Future[Unit] =
    userService.remove(id)
}