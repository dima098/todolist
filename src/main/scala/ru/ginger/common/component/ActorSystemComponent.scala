package ru.ginger.common.component

import akka.actor.ActorSystem

trait ActorSystemComponent {
  implicit def actorSystem: ActorSystem
}
