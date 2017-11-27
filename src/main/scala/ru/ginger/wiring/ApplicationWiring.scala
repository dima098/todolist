package ru.ginger.wiring

import ru.ginger.common.Application
import ComponentWiring._

object ApplicationWiring {
  lazy val application = new Application("to-do-list", routes)
    with ExecutionContextComponentImpl
    with ActorSystemComponentImpl
    with ConfigurationComponentImpl
    with MaterializerComponentImpl
}
