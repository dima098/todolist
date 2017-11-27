package ru.ginger.protocol

import play.api.libs.json.Writes

case class SuccessResponse[T: Writes](result: T)
