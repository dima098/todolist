package ru.ginger.model

import java.time.Instant
import java.util.UUID

import ru.ginger.protocol.SessionView

case class Session(sessionId: String,
                   userId: UUID,
                   ip: String,
                   createdAt: Instant)

object Session {
  def apply(userId: UUID, ip: String): Session = {
    new Session(
      sessionId = UUID.randomUUID().toString + UUID.randomUUID().toString,
      userId = userId,
      ip = ip,
      createdAt = Instant.now()
    )
  }

  def toView(session: Session): SessionView = {
    SessionView(session.sessionId)
  }
}
