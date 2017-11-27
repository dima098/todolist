package ru.ginger.common

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import ru.ginger.common.component.{ActorSystemComponent, ConfigurationComponent, ExecutionContextComponent, MaterializerComponent}
import ru.ginger.configuration.ApplicationConfiguration

class Application(name: String, route: Route) extends ApplicationConfiguration {
  this: ConfigurationComponent
    with ActorSystemComponent
    with MaterializerComponent
    with ExecutionContextComponent =>

  def start(): Unit = {
    val bindingFuture = Http().bindAndHandle(route, address, port)
    println(s"Application is available at $address:$port")

    sys.addShutdownHook {
      bindingFuture.flatMap(_.unbind()).map { _ =>
        println("Application has been stopped")
        actorSystem.terminate()
      }
    }
  }
}
