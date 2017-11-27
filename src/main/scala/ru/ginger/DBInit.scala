package ru.ginger

import java.util.UUID
import ru.ginger.model.db.User
import ru.ginger.model.{Role}
import ru.ginger.wiring.ComponentWiring._
import scala.concurrent.Await
import scala.concurrent.duration.Duration


object DBInit extends ExecutionContextComponentImpl with DatabaseModelComponentImpl with DatabaseRunnerComponentImpl with UserDaoComponentImpl {

  import api._
  import models._

  def init(): Unit = {

    val user = User(
      id = UUID.randomUUID(),
      role = Role.Admin,
      name = "АДМИН",
      surname = "АДМИН",
      phone = "1234567890"
    )

    val future = databaseRunner.run {
      for {
        _ <- (models.task.schema ++ models.taskList.schema ++ models.user.schema).create
        _ <- userDao.create(user)
      } yield ()
    }

    Await.result(future, Duration.Inf)

    println("-----------------DEFAULT USER-----------------")
    println(user)
    println("----------------------------------------------")
  }

}
