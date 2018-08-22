package example

import andersmonad.ErrorMonad

trait PositionKeepingDao[M[_]] {
  def getInstrumentList(): M[Seq[Long]]
}

case class Instrument(id: Long, ref: String)

trait InstrumentService[M[_]] {
  def getInstrument(id: Long): M[Instrument]
}

class BusinessLogic[M[_]]
(positionKeepingDao: PositionKeepingDao[M],
 instrumentService: InstrumentService[M]
)(implicit eM: ErrorMonad[M]) {

  import eM.ErrorMonadOps

  private def isFunny(ref: String): Boolean = {
    ref.contains("funny")
  }

  def retrieveAllFunnyRefs(): M[Seq[String]] = {
    for {
      instrumentIds <- positionKeepingDao.getInstrumentList()
      instruments <- eM.traverse(instrumentIds)(instrumentService.getInstrument)
    } yield {
      instruments
        .map(_.ref)
        .filter(isFunny)
    }
  }

}