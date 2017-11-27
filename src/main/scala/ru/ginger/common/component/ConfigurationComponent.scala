package ru.ginger.common.component

import com.typesafe.config.Config

trait ConfigurationComponent {
  def configuration: Config
}
