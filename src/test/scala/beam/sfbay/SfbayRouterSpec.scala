package beam.sfbay

import java.io.File
import java.time.ZonedDateTime

import akka.actor._
import akka.testkit.{ImplicitSender, TestKit}
import beam.agentsim.agents.vehicles.BeamVehicle.StreetVehicle
import beam.agentsim.events.SpaceTime
import beam.router.BeamRouter._
import beam.router.RoutingModel.BeamLegWithNext
import beam.router.gtfs.FareCalculator
import beam.router.{BeamRouter, Modes, RoutingModel}
import beam.sim.BeamServices
import beam.sim.common.GeoUtilsImpl
import beam.sim.config.BeamConfig
import beam.utils.DateUtils
import com.typesafe.config.ConfigFactory
import org.matsim.api.core.v01.{Coord, Id}
import org.matsim.core.config.ConfigUtils
import org.matsim.core.controler.MatsimServices
import org.matsim.core.scenario.ScenarioUtils
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

import scala.collection.concurrent.TrieMap
import scala.concurrent.duration._
import scala.language.postfixOps

class SfbayRouterSpec extends TestKit(ActorSystem("router-test")) with WordSpecLike
  with ImplicitSender with MockitoSugar with BeforeAndAfterAll {

  var router: ActorRef = _

  override def beforeAll: Unit = {
    val beamConfig = BeamConfig(ConfigFactory.parseFile(new File("production/application-sfbay/beam.conf")).resolve())

    // Have to mock a lot of things to get the router going
    val services: BeamServices = mock[BeamServices]
    val scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig())
    scenario.getPopulation.addPerson(scenario.getPopulation.getFactory.createPerson(Id.createPersonId("56658-0")))
    scenario.getPopulation.addPerson(scenario.getPopulation.getFactory.createPerson(Id.createPersonId("66752-0")))
    scenario.getPopulation.addPerson(scenario.getPopulation.getFactory.createPerson(Id.createPersonId("80672-0")))
    when(services.beamConfig).thenReturn(beamConfig)
    when(services.geo).thenReturn(new GeoUtilsImpl(services))
    val matsimServices = mock[MatsimServices]
    when(matsimServices.getScenario).thenReturn(scenario)
    when(services.matsimServices).thenReturn(matsimServices)
    when(services.dates).thenReturn(DateUtils(beamConfig.beam.routing.baseDate,ZonedDateTime.parse(beamConfig.beam.routing.baseDate).toLocalDateTime,ZonedDateTime.parse(beamConfig.beam.routing.baseDate)))
    val tupleToNext = new TrieMap[Tuple3[Int, Int, Long],BeamLegWithNext]
    when(services.transitLegsByStopAndDeparture).thenReturn(tupleToNext)

    val fareCalculator = system.actorOf(FareCalculator.props(beamConfig.beam.routing.r5.directory))
    router = system.actorOf(BeamRouter.props(services, fareCalculator))

    within(1200000 seconds) { // Router and fare calculator can take a while to initialize
      fareCalculator ! Identify(0)
      expectMsgType[ActorIdentity]
      router ! InitializeRouter
      expectMsg(RouterInitialized)
    }
  }

  override def afterAll: Unit = {
    shutdown()
  }

  "A router must" must {
    "respond with a route to a first reasonable RoutingRequest" in {
      val origin = new BeamRouter.Location(583152.4334365112, 4139386.503815964)
      val destination = new BeamRouter.Location(572710.8214231567, 4142569.0802786923)
      val time = RoutingModel.DiscreteTime(25740)
      router ! RoutingRequest(RoutingRequestTripInfo(origin, destination, time, Vector(Modes.BeamMode.TRANSIT), Vector(StreetVehicle(Id.createVehicleId("body-667520-0"), new SpaceTime(new Coord(origin.getX, origin.getY), time.atTime), Modes.BeamMode.WALK, asDriver = true)), Id.createPersonId("667520-0")))
      val response = expectMsgType[RoutingResponse]
      assert(response.itineraries.nonEmpty)
    }

    "respond with a route to another reasonable RoutingRequest" in {
      val origin = new BeamRouter.Location(626575.0322098453, 4181202.599243111)
      val destination = new BeamRouter.Location(607385.7148858022, 4172426.3760835854)
      val time = RoutingModel.DiscreteTime(25860)
      router ! RoutingRequest(RoutingRequestTripInfo(origin, destination, time, Vector(Modes.BeamMode.TRANSIT), Vector(StreetVehicle(Id.createVehicleId("body-56658-0"), new SpaceTime(new Coord(origin.getX, origin.getY), time.atTime), Modes.BeamMode.WALK, asDriver = true)), Id.createPersonId("56658-0")))
      val response = expectMsgType[RoutingResponse]
      assert(response.itineraries.nonEmpty)
    }

    "respond with a route to yet another reasonable RoutingRequest" in {
      val origin = new BeamRouter.Location(583117.0300037456, 4168059.6668392466)
      val destination = new BeamRouter.Location(579985.712067158, 4167298.6137483735)
      val time = RoutingModel.DiscreteTime(20460)
      router ! RoutingRequest(RoutingRequestTripInfo(origin, destination, time, Vector(Modes.BeamMode.TRANSIT), Vector(StreetVehicle(Id.createVehicleId("body-80672-0"), new SpaceTime(new Coord(origin.getX, origin.getY), time.atTime), Modes.BeamMode.WALK, asDriver = true)), Id.createPersonId("80672-0")))
      val response = expectMsgType[RoutingResponse]
      assert(response.itineraries.nonEmpty)
    }

  }

}