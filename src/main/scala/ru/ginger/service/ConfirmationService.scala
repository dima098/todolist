package ru.ginger.service

import ru.ginger.common.component.{CacheComponent, ConfigurationComponent, ExecutionContextComponent}
import ru.ginger.configuration.ApplicationConfiguration

import scala.concurrent.Future
import scala.util.Random

trait ConfirmationService {
  def code(phone: String): Future[String]
  def check(phone: String, code: String): Future[Boolean]
}

trait ConfirmationServiceComponent {
  def confirmationService: ConfirmationService
}

class ConfirmationServiceImpl extends ConfirmationService with ApplicationConfiguration {
  this: ExecutionContextComponent
    with ConfigurationComponent
    with CacheComponent[String, String] =>

  override def code(phone: String): Future[String] = {
    val generatedCode = Random.nextInt().toString
    cache.put(phone, generatedCode, confirmationCodeTtl)
  }

  override def check(phone: String, code: String): Future[Boolean] = {
    cache.find(phone).map(_.contains(code))
  }
}