package andersmonad.impl

import andersmonad.ErrorMonad

import scala.collection.generic.CanBuildFrom
import scala.concurrent.{ExecutionContext, Future}

trait FutureErrorMonad extends ErrorMonad[Future] {
  implicit val ec: ExecutionContext

  def pure[A](a: => A): Future[A] = Future(a)

  def map[A, B](m: Future[A])(f: A => B): Future[B] = m.map(f)

  def flatMap[A, B](m: Future[A])(f: A => Future[B]): Future[B] = m.flatMap(f)

  def foreach[A](m: Future[A])(e: A => Unit): Unit = {
    m.foreach(e)
  }

  def onFailure[A, B](m: Future[A])(pf: PartialFunction[Throwable, B]): Unit = {
    m.onFailure(pf)
  }

  def onSuccess[A, B](m: Future[A])(pf: PartialFunction[A, B]): Unit = {
    m.onSuccess(pf)
  }

  def traverse[A, B, S[X] <: TraversableOnce[X]](s: S[A])(f: A => Future[B])(implicit cbf: CanBuildFrom[S[A], B, S[B]]): Future[S[B]] = {
    Future.traverse(s)(f)
  }
}
