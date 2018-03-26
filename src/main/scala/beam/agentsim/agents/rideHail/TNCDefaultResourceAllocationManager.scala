package beam.agentsim.agents.rideHail
import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import beam.agentsim.agents.TriggerUtils.schedule
import beam.agentsim.agents.modalBehaviors.DrivesVehicle.StartLegTrigger
import beam.agentsim.agents.rideHail.RideHailingManager.{RideHailingAgentLocation, RideHailingInquiry, TravelProposal}
import beam.agentsim.agents.vehicles.AccessErrorCodes.UnknownRideHailReservationError
import beam.agentsim.agents.vehicles._
import beam.agentsim.events.SpaceTime
import beam.router.BeamRouter.{Location, RoutingResponse}
import beam.router.RoutingModel.{BeamTime, BeamTrip}
import com.google.common.cache.{Cache, CacheBuilder}
import org.matsim.api.core.v01.Id

class TNCDefaultResourceAllocationManager extends TNCResourceAllocationManager {
  override def getNonBindingTravelProposalAsEstimate(startLocation: SpaceTime, endLocation: SpaceTime): RideHailingManager.TravelProposal = ???




  override def routeRequestsResultCallBack(batchRequestId: Long, responses: Vector[RoutingResponse]) = {

    routeRequestsResultCallBack (null, null)
  }

  override def repositionIdleVehicles(): Unit = {}

  override def allocatePassengers(inquiryIds: Vector[Id[RideHailingManager.RideHailingInquiry]]): Unit = {}

  override def allocatePassenger(inquiryId: Id[RideHailingManager.RideHailingInquiry], surgePricingManager: RideHailSurgePricingManager,departAt: BeamTime): Unit = {
    val (travelPlanOpt: Option[(TravelProposal, BeamTrip)], customerAgent: ActorRef, closestRHA: Option[RideHailingAgentLocation]) = findClosestRideHailingAgents(inquiryId, customerPickUp)

    closestRHA match {
      case Some((closestRideHailingAgent)) =>
        val travelProposal = travelPlanOpt.get._1
        surgePricingManager.addRideCost(departAt.atTime, travelProposal.estimatedPrice.doubleValue(), customerPickUp)


        val tripPlan = travelPlanOpt.map(_._2)
        handleReservation(inquiryId, vehiclePersonIds, customerPickUp, destination, customerAgent,
          closestRideHailingAgent, travelProposal, tripPlan)
      // We have an agent nearby, but it's not the one we originally wanted
      case _ =>
        customerAgent ! ReservationResponse(Id.create(inquiryId.toString, classOf[ReservationRequest]), Left
        (UnknownRideHailReservationError))
    }
  }


  private def handleReservation(inquiryId: Id[RideHailingInquiry], vehiclePersonId: VehiclePersonId,
                                customerPickUp: Location, destination: Location,
                                customerAgent: ActorRef, closestRideHailingAgentLocation: RideHailingAgentLocation,
                                travelProposal: TravelProposal, trip2DestPlan: Option[BeamTrip]): Unit = {

    // Modify RH agent passenger schedule and create BeamAgentScheduler message that will dispatch RH agent to do the
    // pickup
    val passengerSchedule = PassengerSchedule()
    passengerSchedule.addLegs(travelProposal.responseRideHailing2Pickup.itineraries.head.toBeamTrip.legs) // Adds
    // empty trip to customer
    passengerSchedule.addPassenger(vehiclePersonId, trip2DestPlan.get.legs.filter(_.mode == CAR)) // Adds customer's
    // actual trip to destination
    putIntoService(closestRideHailingAgentLocation)
    lockedVehicles -= closestRideHailingAgentLocation.vehicleId

    // Create confirmation info but stash until we receive ModifyPassengerScheduleAck
    val triggerToSchedule = schedule[StartLegTrigger](passengerSchedule.schedule.firstKey.startTime,
      closestRideHailingAgentLocation.rideHailAgent, passengerSchedule.schedule.firstKey)
    pendingModifyPassengerScheduleAcks.put(inquiryId, ReservationResponse(Id.create(inquiryId.toString,
      classOf[ReservationRequest]), Right(ReserveConfirmInfo(trip2DestPlan.head.legs.head, trip2DestPlan.last.legs
      .last, vehiclePersonId, triggerToSchedule))))
    closestRideHailingAgentLocation.rideHailAgent ! ModifyPassengerSchedule(passengerSchedule, Some(inquiryId))
  }



  private def findClosestRideHailingAgents(inquiryId: Id[RideHailingInquiry], customerPickUp: Location) = {

    val travelPlanOpt = Option(pendingInquiries.asMap.remove(inquiryId))
    val customerAgent = sender()
    /**
      * 1. customerAgent ! ReserveRideConfirmation(availableRideHailingAgentSpatialIndex, customerId, travelProposal)
      * 2. availableRideHailingAgentSpatialIndex ! PickupCustomer
      */
    val nearbyRideHailingAgents = availableRideHailingAgentSpatialIndex.getDisk(customerPickUp.getX, customerPickUp.getY,
      radius).asScala.toVector
    val closestRHA: Option[RideHailingAgentLocation] = nearbyRideHailingAgents.filter(x =>
      lockedVehicles(x.vehicleId)).find(_.vehicleId.equals(travelPlanOpt.get._1.responseRideHailing2Pickup
      .itineraries.head.vehiclesInTrip.head))
    (travelPlanOpt, customerAgent, closestRHA)
  }


  override def bufferReservationRequests(): Boolean = ???
}
