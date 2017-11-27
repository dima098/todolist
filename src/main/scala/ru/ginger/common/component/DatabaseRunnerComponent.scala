package ru.ginger.common.component

import ru.ginger.common.component.DatabaseModelComponent.IOAction
import slick.dbio.Effect.Transactional
import slick.jdbc.H2Profile._
import slick.jdbc.H2Profile.api._

trait DatabaseRunnerComponent {
  def databaseRunner: Backend#Database

  def transactionally[R, E <: Effect](ioAction: IOAction[R, E]): IOAction[R, E with Transactional] = {
    ioAction.transactionally
  }
}
