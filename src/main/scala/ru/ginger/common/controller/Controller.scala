package ru.ginger.common.controller

import akka.http.scaladsl.server.{Directives, Route}
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.Writes
import ru.ginger.common.exception.BaseException
import ru.ginger.protocol.{ErrorResponse, SuccessResponse}
import scala.concurrent.Future
import scala.util.{Failure, Success}
import ru.ginger.format.JsonFormat._

trait Controller extends Directives with PlayJsonSupport {
  def route: Route

  def completeFuture[T : Writes](future: Future[T]): Route = {
    onComplete(future) {
      case Success(value) => complete(SuccessResponse(value))
      case Failure(error) => complete(makeErrorResponse(error))
    }
  }

  // internal

  private def makeErrorResponse(error: Throwable): ErrorResponse = {
    error match {
      case e: BaseException => ErrorResponse(e.message)
      case e => ErrorResponse("SERVER ERROR")
    }
  }
}
