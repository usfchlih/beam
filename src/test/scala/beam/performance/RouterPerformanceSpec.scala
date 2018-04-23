package beam.performance

import java.time.ZonedDateTime

import akka.actor._
import akka.testkit.{ImplicitSender, TestKit}
import beam.agentsim.agents.vehicles.VehicleProtocol.StreetVehicle
import beam.agentsim.events.SpaceTime
import beam.router.BeamRouter._
import beam.router.Modes.BeamMode.{CAR, RIDE_HAIL, WALK}
import beam.router.RoutingModel.{BeamLeg, BeamPath, BeamTrip}
import beam.router.gtfs.FareCalculator
import beam.router.gtfs.FareCalculator.BeamFareSegment
import beam.router.osm.TollCalculator
import beam.router.r5.NetworkCoordinator
import beam.router.{BeamRouter, Modes, RoutingModel}
import beam.sim.BeamServices
import beam.sim.common.{GeoUtils, GeoUtilsImpl}
import beam.sim.config.{BeamConfig, MatSimBeamConfigBuilder}
import beam.sim.metrics.Metrics.{MetricLevel, isMetricsEnable, level}
import beam.sim.metrics.{Metrics, MetricsSupport}
import beam.tags.Performance
import beam.utils.{BeamConfigUtils, DateUtils}
import com.typesafe.config.ConfigFactory
import kamon.Kamon
import org.matsim.api.core.v01.population.{Activity, Plan}
import org.matsim.api.core.v01.{Coord, Id, Scenario}
import org.matsim.core.events.EventsManagerImpl
import org.matsim.core.scenario.ScenarioUtils
import org.matsim.vehicles.VehicleUtils
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest._
import org.scalatest.mockito.MockitoSugar

import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.language.postfixOps

class RouterPerformanceSpec extends TestKit(ActorSystem("router-test", ConfigFactory.parseString(
  """
  akka.loglevel="OFF"
  akka.test.timefactor=10
  """))) with WordSpecLike with Matchers with Inside with LoneElement
  with ImplicitSender with MockitoSugar with BeforeAndAfterAllConfigMap with MetricsSupport {

  var router: ActorRef = _
  var geo: GeoUtils = _
  var scenario: Scenario = _

  override def beforeAll(configMap: ConfigMap): Unit = {
    val confPath = configMap.getWithDefault("config", "test/input/sf-light/sf-light-25k.conf")

    val config = BeamConfigUtils.parseFileSubstitutingInputDirectory(confPath).resolve()
    val beamConfig = BeamConfig(config)
    level = beamConfig.beam.metrics.level
    if (isMetricsEnable()) Kamon.start(config.withFallback(ConfigFactory.defaultReference()))
    // Have to mock some things to get the router going
    val services: BeamServices = mock[BeamServices]
    when(services.beamConfig).thenReturn(beamConfig)
    geo = new GeoUtilsImpl(services)
    when(services.geo).thenReturn(geo)
    when(services.dates).thenReturn(DateUtils(ZonedDateTime.parse(beamConfig.beam.routing.baseDate).toLocalDateTime, ZonedDateTime.parse(beamConfig.beam.routing.baseDate)))
    val networkCoordinator: NetworkCoordinator = new NetworkCoordinator(beamConfig, VehicleUtils.createVehiclesContainer())
    networkCoordinator.loadNetwork()

    val fareCalculator = mock[FareCalculator]
    when(fareCalculator.getFareSegments(any(), any(), any(), any(), any())).thenReturn(Vector[BeamFareSegment]())
    val tollCalculator = mock[TollCalculator]
    when(tollCalculator.calcToll(any())).thenReturn(0.0)
    val matsimConfig = new MatSimBeamConfigBuilder(config).buildMatSamConf()
    scenario = ScenarioUtils.loadScenario(matsimConfig)
    router = system.actorOf(BeamRouter.props(services, networkCoordinator.transportNetwork, networkCoordinator.network, new EventsManagerImpl(), scenario.getTransitVehicles, fareCalculator, tollCalculator))


    within(60 seconds) { // Router can take a while to initialize
      router ! Identify(0)
      expectMsgType[ActorIdentity]
    }
  }

  override def afterAll(configMap: ConfigMap): Unit = {



    shutdown()
    if (isMetricsEnable()) Kamon.shutdown()
  }

  "A router" must {

    "respond with a car route for each trip" taggedAs(Performance) in {
      var counter = 0
      scenario.getPopulation.getPersons.values().forEach(person => {
        val activities = planToVec(person.getSelectedPlan)

        activities.sliding(2).foreach(pair => {
          increment("test-perf-request-total", Metrics.VerboseLevel)
          countOccurrence("test-perf-request-count", Metrics.VerboseLevel)
          val origin = pair(0).getCoord
          val destination = pair(1).getCoord
          val time = RoutingModel.DiscreteTime(pair(0).getEndTime.toInt)
          latency("test-perf-request-time", Metrics.VerboseLevel) {
          router ! RoutingRequest(origin, destination, time, Vector(), Vector(
            StreetVehicle(Id.createVehicleId("116378-2"), new SpaceTime(origin, 0), Modes.BeamMode.CAR, asDriver = true)))
            val response = expectMsgType[RoutingResponse]
            assert(response.itineraries.exists(_.tripClassifier == CAR))
        }
        })
      })
    }

    def planToVec(plan: Plan): Vector[Activity] = {
      scala.collection.immutable.Vector.empty[Activity] ++ plan.getPlanElements.asScala.filter(p => p
        .isInstanceOf[Activity]).map(p => p.asInstanceOf[Activity])
    }
  }
}
