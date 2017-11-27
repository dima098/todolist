package ru.ginger.dao

import java.util.UUID

import ru.ginger.common.component.DatabaseModelComponent
import ru.ginger.model.db.UserModel
import ru.ginger.common.utils.QueryImplicits._

trait UserQueryRepository {
  this: DatabaseModelComponent[UserModel] =>

  import api._

  def userBy(id: Option[UUID] = None, phone: Option[String] = None) =
    models.user
      .filterOption(id)(_.id === _.bind)
      .filterOption(phone)(_.phone === _.bind)
}
