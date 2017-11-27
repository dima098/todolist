package ru.ginger.configuration

import ru.ginger.common.component.ConfigurationComponent

import scala.concurrent.duration.Duration

trait ApplicationConfiguration {
  this: ConfigurationComponent =>

  import ApplicationConfiguration.Keys._

  def port: Int = configuration.getInt(portKey)
  def address: String = configuration.getString(addressKey)
  def sessionTtl: Duration = Duration(configuration.getString(sessionTtlKey))
  def confirmationCodeTtl: Duration = Duration(configuration.getString(confirmationCodeTtlKey))
}

object ApplicationConfiguration {
  object Keys {
    val portKey = "todo-list.http.port"
    val addressKey = "todo-list.http.address"
    val sessionTtlKey = "todo-list.session.ttl"
    val confirmationCodeTtlKey = "todo-list.confirmation.code.ttl"
  }
}
