package common.component

import ru.ginger.common.component.DatabaseRunnerComponent
import slick.jdbc.H2Profile.api._
import slick.jdbc.H2Profile._

trait FakeDatabaseRunnerComponent extends DatabaseRunnerComponent {
  override val databaseRunner: Backend#Database = Database.forConfig("h2Configuration")
}
