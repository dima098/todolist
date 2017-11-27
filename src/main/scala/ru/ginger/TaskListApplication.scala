package ru.ginger

import ru.ginger.wiring.ApplicationWiring._

object TaskListApplication extends App {
  DBInit.init()
  application.start()
}
