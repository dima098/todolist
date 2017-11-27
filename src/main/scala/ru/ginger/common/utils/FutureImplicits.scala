package ru.ginger.common.utils

import scala.concurrent.{ExecutionContext, Future}

object FutureImplicits {
  implicit class FuturePimp[T](value: Future[T]) {
    def toUnit(implicit executionContext: ExecutionContext): Future[Unit] = value.map(_ => ())
  }
}
