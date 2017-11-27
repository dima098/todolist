package ru.ginger.exception

import java.util.UUID
import ru.ginger.common.exception.BaseException

class UserNotFoundException(userId: Option[UUID], phone: Option[String])
  extends BaseException(s"User hasn't found for [ ID = $userId ] and [ PHONE = $phone ]")
