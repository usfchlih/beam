package beam.agentsim.agents.rideHail

import beam.agentsim.events.SpaceTime
import org.matsim.api.core.v01.events.Event
import org.matsim.core.api.experimental.events.EventsManager
import org.matsim.core.events.handler.BasicEventHandler



case class WaitingEvent(location: SpaceTime, waitingDuration: Double)

class LocationWaitingTimeMatrix(val waitingEvents: Set[WaitingEvent]){

  /*
  TODO: if code is slow, implement version with bins (which has a larger memory foot print)

  timeBinDurationInSec:Double

  val waitingEventBins: ArrayBuffer[Set[WaitingEvent]] = new ArrayBuffer[Set[WaitingEvent]]()

  waitingEvents.foreach{waitingEvent: WaitingEvent =>
    waitingEventBins.
  }

*/

  def getWaitingEventsAtTime(time: Double):Set[WaitingEvent] ={
    waitingEvents.filter(waitingEvent => (time >= waitingEvent.location.time && time <= waitingEvent.location.time + waitingEvent.waitingDuration))
  }
}




