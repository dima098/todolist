package common.component

import ru.ginger.common.component.ExecutionContextComponent
import scala.concurrent.ExecutionContext

trait FakeExecutionContextComponent extends ExecutionContextComponent {
  override implicit val executionContext: ExecutionContext = scala.concurrent.ExecutionContext.global
}
