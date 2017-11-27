package ru.ginger.wiring

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import com.typesafe.config.{Config, ConfigFactory}
import ru.ginger.common.component._
import ru.ginger.model.db.{TaskListModel, TaskModel, UserModel}
import slick.jdbc.H2Profile.api._
import slick.jdbc.H2Profile._
import scala.concurrent.ExecutionContext

trait CommonComponentWiring {
  private lazy val executionContextImpl = scala.concurrent.ExecutionContext.global
  trait ExecutionContextComponentImpl extends ExecutionContextComponent {
    override implicit val executionContext: ExecutionContext = executionContextImpl
  }

  lazy val modelsImpl = new TaskModel with TaskListModel with UserModel {}
  trait DatabaseModelComponentImpl extends DatabaseModelComponent[modelsImpl.type] {
    override val models: modelsImpl.type = modelsImpl
  }

  private lazy val databaseImpl = Database.forConfig("h2Configuration")
  trait DatabaseRunnerComponentImpl extends DatabaseRunnerComponent {
    override def databaseRunner: Backend#Database = databaseImpl
  }

  private lazy val configurationImpl = ConfigFactory.load()
  trait ConfigurationComponentImpl extends ConfigurationComponent {
    override val configuration: Config = configurationImpl
  }

  private implicit lazy val actorSystemImpl = ActorSystem()
  trait ActorSystemComponentImpl extends ActorSystemComponent {
    override implicit def actorSystem: ActorSystem = actorSystemImpl
  }

  private lazy val materializerImpl = ActorMaterializer()
  trait MaterializerComponentImpl extends MaterializerComponent {
    override implicit val materializer: Materializer = materializerImpl
  }
}
