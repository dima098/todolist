package ru.ginger.service

import ru.ginger.common.component.{CacheComponent, ConfigurationComponent, ExecutionContextComponent}
import ru.ginger.configuration.ApplicationConfiguration
import ru.ginger.model.Session
import ru.ginger.common.utils.FutureImplicits._
import scala.concurrent.Future

trait SessionService {
  def find(sessionId: String, ip: String): Future[Option[Session]]
  def create(session: Session): Future[Session]
  def remove(sessionId: String): Future[Unit]
}

trait SessionServiceComponent {
  def sessionService: SessionService
}

class SessionServiceImpl extends SessionService with ApplicationConfiguration {
  this: ExecutionContextComponent
    with ConfigurationComponent
    with CacheComponent[String, Session] =>

  override def find(sessionId: String, ip: String): Future[Option[Session]] = {
    cache.find(sessionId).map {
      case Some(value) if value.ip == ip => Some(value)
      case _ => None
    }
  }

  override def create(session: Session): Future[Session] = {
    cache.put(session.sessionId, session, sessionTtl)
    println(session)
    Future.successful(session)
  }

  override def remove(sessionId: String): Future[Unit] = {
    cache.remove(sessionId).toUnit
  }
}
