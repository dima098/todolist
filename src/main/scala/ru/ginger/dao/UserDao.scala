package ru.ginger.dao

import java.util.UUID

import ru.ginger.common.component.{DatabaseModelComponent, ExecutionContextComponent}
import ru.ginger.common.component.DatabaseModelComponent.IOAction
import ru.ginger.exception.UserNotFoundException
import ru.ginger.model.db.{User, UserModel}
import slick.dbio.Effect.{Read, Write}
import ru.ginger.common.utils.ValuesImplicits._

trait UserDao {
  def list(offset: Int, limit: Int): IOAction[Seq[User], Read]
  def findBy(id: Option[UUID] = None, phone: Option[String] = None): IOAction[Option[User], Read]
  def getBy(id: Option[UUID] = None, phone: Option[String] = None): IOAction[User, Read]
  def countBy(id: Option[UUID] = None, phone: Option[String] = None): IOAction[Int, Read]
  def create(user: User): IOAction[Unit, Write]
  def update(user: User): IOAction[Int, Write]
  def remove(id: UUID): IOAction[Int, Write]
}

trait UserDaoComponent {
  def userDao: UserDao
}

class UserDaoImpl extends UserDao with UserQueryRepository {
  this: ExecutionContextComponent
    with DatabaseModelComponent[UserModel] =>

  import api._
  import models._

  override def list(offset: Int, limit: Int): IOAction[Seq[User], Read] =
    models.user.drop(offset).take(limit).result

  override def findBy(id: Option[UUID], phone: Option[String]): IOAction[Option[User], Read] =
    userBy(id, phone).result.headOption

  override def getBy(id: Option[UUID], phone: Option[String]): IOAction[User, Read] =
    findBy(id, phone).flatMap {
      case Some(value) => DBIO.successful(value)
      case None => DBIO.failed(new UserNotFoundException(id, phone))
    }

  override def countBy(id: Option[UUID] = None, phone: Option[String] = None): IOAction[Int, Read] = {
    userBy(id, phone).size.result
  }

  override def create(user: User): IOAction[Unit, Write] =
    (models.user += user).map(_ => ())

  override def update(user: User): IOAction[Int, Write] =
    userBy(user.id.some)
      .map(u => (u.name, u.surname, u.phone, u.role))
      .update((user.name, user.surname, user.phone, user.role))

  override def remove(id: UUID): IOAction[Int, Write] =
    userBy(id.some).delete
}
