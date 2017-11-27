package ru.ginger.model

object Role extends Enumeration {
  type Role = Role.Value

  val Admin = Value("admin")
  val Simple = Value("simple")
}
