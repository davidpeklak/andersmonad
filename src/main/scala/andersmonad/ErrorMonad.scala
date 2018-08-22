package andersmonad

import scala.collection.generic.CanBuildFrom

trait ErrorMonad[M[_]] {
  errorMonad =>

  def pure[A](a: => A): M[A]

  def map[A, B](m: M[A])(f: A => B): M[B]

  def flatMap[A, B](m: M[A])(f: A => M[B]): M[B]

  def foreach[A](m: M[A])(e: A => Unit): Unit

  def onFailure[A, B](m: M[A])(pf: PartialFunction[Throwable, B]): Unit

  def onSuccess[A, B](m: M[A])(pf: PartialFunction[A, B]): Unit

  def traverse[A, B, S[X] <: scala.TraversableOnce[X]](s: S[A])(f: A => M[B])(implicit cbf : CanBuildFrom[S[A], B, S[B]]): M[S[B]]

  implicit class ErrorMonadOps[A](m: M[A]) {
    def map[B](f: A => B): M[B] = errorMonad.map(m)(f)

    def flatMap[B](f: A => M[B]): M[B] = errorMonad.flatMap(m)(f)
  }
}



