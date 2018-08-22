package example

package object either {
  type ThrowEither[A] = Either[Throwable, A]
}
