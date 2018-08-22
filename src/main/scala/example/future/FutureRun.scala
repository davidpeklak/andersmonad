package example.future

import andersmonad.ErrorMonad
import andersmonad.impl.FutureErrorMonad
import example.{BusinessLogic, Instrument, InstrumentService, PositionKeepingDao}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, ExecutionContext, Future}


object FuturePositionKeepingDao extends PositionKeepingDao[Future] {
  def getInstrumentList(): Future[Seq[Long]] = {
    slowFuture(Seq(1, 2, 3, 5, 8, 13))
  }
}

object FutureInstrumentService extends InstrumentService[Future] {
  def getInstrument(id: Long): Future[Instrument] = {
    slowFuture(if (id % 2 == 0) Instrument(id, id.toString) else Instrument(id, "funny " + id.toString))
  }
}

object FutureRun extends App {
  implicit val eM: ErrorMonad[Future] = new FutureErrorMonad {
    implicit val ec: ExecutionContext = global
  }

  val businessLogic = new BusinessLogic[Future](FuturePositionKeepingDao, FutureInstrumentService) {}

  val result = Await.result(businessLogic.retrieveAllFunnyRefs(), 1 minute)
  println(result)
}
