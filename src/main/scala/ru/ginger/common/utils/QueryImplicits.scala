package ru.ginger.common.utils

import slick.lifted.{Query, Rep}

object QueryImplicits {
  implicit class QueryPimp[X, Y](query: Query[X, Y, Seq]) {
    def filterOption[T](value: Option[T])(f: (X,T) => Rep[Boolean]): Query[X, Y, Seq] = {
      value match {
        case Some(v) => query.filter(f(_, v))
        case None => query
      }
    }
  }
}
