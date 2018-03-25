package beam.agentsim.agents.rideHail

import akka.actor.ActorRef
import beam.agentsim.agents.rideHail.RideHailingManager._
import beam.agentsim.events.SpaceTime
import beam.router.BeamRouter.RoutingResponse
import beam.router.RoutingModel.BeamTime
import org.matsim.api.core.v01.Id

trait TNCResourceAllocationManager {

  // all methods can use RHM.getFleetInfo

// TODO: set this always in Ride Hail Manager with each query
  var sender:ActorRef

  val radius: Double // TODO: set this in constructor!

  def getNonBindingTravelProposalAsEstimate(startLocation:SpaceTime, endLocation:SpaceTime):TravelProposal // we take over current implementation
    // TODO: also give back
     // can use RHM.getEstimatedLinkTravelTimesWithAdditionalLoad


  def allocatePassengers(inquiryIds: Vector[Id[RideHailingInquiry]]) // input: information of batch of tnc requests (from
    // use RHM.assignTNC


  def allocatePassenger(inquiryId: Id[RideHailingInquiry], surgePricingManager: RideHailSurgePricingManager, departAt: BeamTime)



  def repositionIdleVehicles()

  def bufferReservationRequests(): Boolean

  def routeRequestsResultCallBack (batchRequestId: Long)



    // use RHM.moveIdleTNCTo to implement

}

object TNCResourceAllocationManager{
  val DEFAULT_MANAGER="DEFAULT_ALLOCATION_MANAGER"
  val STANFORD_ALLOCATION_MANAGER_V1="STANFORD_ALLOCATION_MANAGER_V1"
}
