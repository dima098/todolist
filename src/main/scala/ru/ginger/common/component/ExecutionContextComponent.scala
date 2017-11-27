package ru.ginger.common.component

import scala.concurrent.ExecutionContext

trait ExecutionContextComponent {
  implicit def executionContext: ExecutionContext
}
