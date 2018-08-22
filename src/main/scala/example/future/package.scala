package example

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

package object future {

  def slowFuture[A](a: => A): Future[A] = Future {
    print(".")
    Thread.sleep(2000L)
    a
  }
}