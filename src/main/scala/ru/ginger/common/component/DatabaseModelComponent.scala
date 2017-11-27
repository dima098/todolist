package ru.ginger.common.component

import ru.ginger.model.db.AbstractDBModel
import slick.dbio.{DBIOAction, Effect, NoStream}
import slick.jdbc

trait DatabaseModelComponent[+A <: AbstractDBModel] {
  val models: A
  val api = jdbc.H2Profile.api
}

object DatabaseModelComponent {
  type IOAction[+R, -E <: Effect] = DBIOAction[R, NoStream, E]
}