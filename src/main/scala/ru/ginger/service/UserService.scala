package ru.ginger.service

import java.util.UUID
import ru.ginger.common.component.{DatabaseRunnerComponent, ExecutionContextComponent}
import ru.ginger.common.utils.FutureImplicits._
import ru.ginger.dao.UserDaoComponent
import ru.ginger.model.UserCreatedResponse
import ru.ginger.model.db.User
import ru.ginger.protocol.UserRequest
import ru.ginger.validation.UserValidationServiceComponent
import scala.concurrent.Future

trait UserService {
  def find(id: Option[UUID] = None, phone: Option[String] = None): Future[Option[User]]
  def get(id: Option[UUID] = None, phone: Option[String] = None): Future[User]
  def count(id: Option[UUID] = None, phone: Option[String] = None): Future[Int]
  def list(offset: Int, limit: Int): Future[Seq[User]]
  def create(request: UserRequest): Future[UserCreatedResponse]
  def update(id: UUID, request: UserRequest): Future[Unit]
  def remove(id: UUID): Future[Unit]
}

trait UserServiceComponent {
  def userService: UserService
}

class UserServiceImpl extends UserService {
  this: ExecutionContextComponent
    with UserDaoComponent
    with UserValidationServiceComponent
    with DatabaseRunnerComponent =>

  override def find(id: Option[UUID], phone: Option[String]): Future[Option[User]] = {
    databaseRunner.run(userDao.findBy(id, phone))
  }

  override def get(id: Option[UUID], phone: Option[String]): Future[User] = {
    databaseRunner.run(userDao.getBy(id, phone))
  }

  override def count(id: Option[UUID], phone: Option[String]): Future[Int] = {
    databaseRunner.run(userDao.countBy(id, phone))
  }

  override def list(offset: Int, limit: Int): Future[Seq[User]] = {
    databaseRunner.run(userDao.list(offset, limit))
  }

  override def create(request: UserRequest): Future[UserCreatedResponse] = {
    val user = User.makeFrom(UUID.randomUUID(), request)

    def createUser: Future[UserCreatedResponse] = {
      databaseRunner.run {
        for {
          _ <- userDao.create(user)
        } yield UserCreatedResponse(user.id)
      }
    }

    for {
      _ <- userValidationService.validateCreate(request.phone)
      response <- createUser
    } yield response
  }

  override def update(id: UUID, request: UserRequest): Future[Unit] = {
    val user = User.makeFrom(id, request)

    def updateUser: Future[Unit] = {
      databaseRunner.run(userDao.update(user)).toUnit
    }

    for {
      _ <- userValidationService.validateUpdate(id, request.phone)
      _ <- updateUser
    } yield ()
  }

  override def remove(id: UUID): Future[Unit] = {
    databaseRunner.run(userDao.remove(id)).toUnit
  }
}