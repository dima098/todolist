package ru.ginger.protocol

import java.util.UUID
import ru.ginger.model.Role.Role

case class UserView(id: UUID,
                    role: Role,
                    name: String,
                    surname: String,
                    phone: String)
