package ru.ginger.protocol

case class LoginConfirmationRequest(phone: String, smsCode: String)