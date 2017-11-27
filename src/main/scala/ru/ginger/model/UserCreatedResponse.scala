package ru.ginger.model

import java.util.UUID

import ru.ginger.protocol.UserCreatedResponseView

case class UserCreatedResponse(id: UUID)

object UserCreatedResponse {
  def toView(response: UserCreatedResponse): UserCreatedResponseView = {
    UserCreatedResponseView(response.id)
  }
}