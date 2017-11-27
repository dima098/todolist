package ru.ginger.exception

import ru.ginger.common.exception.BaseException

class ConfirmationCodeNotFoundException(phone: String, code: String) extends BaseException(s"[ CODE = $code ] hasn't found for [ PHONE = $phone ]")
