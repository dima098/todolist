package common.utils

import org.scalatest.Matchers
import org.scalatest.concurrent.{Futures, ScalaFutures}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.time.{Seconds, Span}

trait SpecBase extends MockitoSugar with Futures with ScalaFutures with Matchers with OngoingStubbingImplicits {
  override implicit val patienceConfig: PatienceConfig = PatienceConfig.apply(Span(5, Seconds))
}