package ru.ginger.protocol

import ru.ginger.model.Role.Role

case class UserRequest(role: Role,
                       name: String,
                       surname: String,
                       phone: String)