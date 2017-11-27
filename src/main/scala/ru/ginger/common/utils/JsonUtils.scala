package ru.ginger.common.utils

import play.api.libs.json._

object JsonUtils {
  def formatEnumeration[E <: Enumeration](enum: E): Format[E#Value] = {
    new Format[E#Value] {
      override def writes(value: E#Value): JsValue = JsString(value.toString)
      override def reads(json: JsValue): JsResult[E#Value] = Reads.enumNameReads(enum).reads(json)
    }
  }
}
