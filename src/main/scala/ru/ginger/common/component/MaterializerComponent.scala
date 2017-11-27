package ru.ginger.common.component

import akka.stream.Materializer

trait MaterializerComponent {
  implicit def materializer: Materializer
}
