package ru.ginger.model.db

import java.util.UUID
import ru.ginger.model.Role
import ru.ginger.model.Role.Role
import ru.ginger.protocol.{UserRequest, UserView}

case class User(id: UUID,
                role: Role,
                name: String,
                surname: String,
                phone: String)

trait UserModel extends AbstractDBModel {

  import slick.jdbc.MySQLProfile.api._

  implicit lazy val roleMappedColumnType: BaseColumnType[Role] =
    MappedColumnType.base[Role, String](_.toString, Role.withName)

  class UserTable(tag: Tag) extends Table[User](tag, "TODO_USER") {

    def id = column[UUID]("ID")
    def role = column[Role]("ROLE")
    def name = column[String]("NAME")
    def surname = column[String]("SURNAME")
    def phone = column[String]("PHONE")

    override def * = (id, role, name, surname, phone) <> ((User.apply _).tupled, User.unapply)
  }

  val user: TableQuery[UserTable] = TableQuery[UserTable]
}

object User {
  def makeFrom(id: UUID, request: UserRequest): User = {

    User(
      id = id,
      role = request.role,
      name = request.name,
      surname = request.surname,
      phone = request.phone
    )
  }

  def toView(user: User): UserView = {
    UserView(
      id = user.id,
      role = user.role,
      name = user.name,
      surname = user.surname,
      phone = user.phone
    )
  }
}