package common.utils

import org.mockito.stubbing.OngoingStubbing
import ru.ginger.common.component.DatabaseModelComponent.IOAction
import slick.dbio.{DBIOAction, Effect}
import scala.concurrent.Future

trait OngoingStubbingImplicits {
  implicit class IOActionOngoingStubbingPimp[T, E <: Effect](stub: OngoingStubbing[IOAction[T, E]]) {
    def thenReturnIOAction(v: T): OngoingStubbing[IOAction[T, E]] = {
      stub.thenReturn(DBIOAction.successful(v))
    }
  }

  implicit class FutureOngoingStubbingPimp[T](stub: OngoingStubbing[Future[T]]) {
    def thenReturnFuture(v: T): OngoingStubbing[Future[T]] = {
      stub.thenReturn(Future.successful(v))
    }
  }
}

object OngoingStubbingImplicits extends OngoingStubbingImplicits