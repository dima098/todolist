package common.component

import com.typesafe.config.{Config, ConfigFactory}
import ru.ginger.common.component.ConfigurationComponent
import collection.JavaConverters._

trait FakeConfigurationComponent extends ConfigurationComponent {
  override def configuration: Config = ConfigFactory.parseMap(configurationValues.asJava)
  def configurationValues: Map[String, Any] = Map.empty
}
