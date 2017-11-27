package ru.ginger.common.utils

object ValuesImplicits {
  implicit class ValuesPimp[A](value: A) {
    def some: Option[A] = Some(value)
  }
}
