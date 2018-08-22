package example.either

import andersmonad.ErrorMonad
import andersmonad.impl.EitherErrorMonad
import example.{BusinessLogic, Instrument, InstrumentService, PositionKeepingDao}

object EitherPositionKeepingDao extends PositionKeepingDao[ThrowEither] {
  def getInstrumentList(): Either[Throwable, Seq[Long]] = {
    Right(Seq(1, 2, 3, 5, 8, 13))
  }
}

object EitherInstrumentService extends InstrumentService[ThrowEither] {
  def getInstrument(id: Long): Either[Throwable, Instrument] = {
    Right(if (id % 2 == 0) Instrument(id, id.toString) else Instrument(id, "funny " + id.toString))
  }
}

object EitherRun extends App {
  implicit val eM: ErrorMonad[ThrowEither] = EitherErrorMonad

  val businessLogic = new BusinessLogic[ThrowEither](EitherPositionKeepingDao, EitherInstrumentService)

  val result = businessLogic.retrieveAllFunnyRefs()
  println(result)
}
