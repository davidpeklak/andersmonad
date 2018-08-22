package andersmonad.impl

import andersmonad.ErrorMonad

import scala.collection.generic.CanBuildFrom
import scala.collection.mutable

object EitherErrorMonad extends ErrorMonad[({ type T[A] = Either[Throwable, A] })#T] {
  def pure[A](a: => A): Either[Throwable, A] = Right(a)

  def map[A, B](m: Either[Throwable, A])(f: A => B): Either[Throwable, B] = m.right.map(f)

  def flatMap[A, B](m: Either[Throwable, A])(f: A => Either[Throwable, B]): Either[Throwable, B] = m.right.flatMap(f)

  def foreach[A](m: Either[Throwable, A])(e: A => Unit): Unit = m.right.foreach(e)

  def onFailure[A, B](m: Either[Throwable, A])(pf: PartialFunction[Throwable, B]): Unit = m.left.foreach(t => if (pf.isDefinedAt(t)) {pf(t)})

  def onSuccess[A, B](m: Either[Throwable, A])(pf: PartialFunction[A, B]): Unit = m.right.foreach(t => if (pf.isDefinedAt(t)) {pf(t)})

  def traverse[A, B, S[X] <: TraversableOnce[X]](s: S[A])(f: A => Either[Throwable, B])(implicit cbf: CanBuildFrom[S[A], B, S[B]]): Either[Throwable, S[B]] = {
    val builder: mutable.Builder[B, S[B]] = cbf()
    s
      .map(f)
      .foldLeft(Right(builder): Either[Throwable, mutable.Builder[B, S[B]]]){ (acc, e) => for (_ <- acc.right; x <- e.right) yield builder += x}
      .right.map(_.result())
  }
}
