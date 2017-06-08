// generated by tscfg 0.8.0 on Fri May 19 22:00:28 EEST 2017
// source: src/main/resources/beam-template.conf

package beam.agentsim.config

import scala.collection.JavaConverters._

case class BeamConfig(
  beam : BeamConfig.Beam
)
object BeamConfig {
  case class Beam(
    agentsim     : BeamConfig.Beam.Agentsim,
    events       : BeamConfig.Beam.Events,
    outputs      : BeamConfig.Beam.Outputs,
    routing      : BeamConfig.Beam.Routing,
    sharedInputs : java.lang.String
  )
  object Beam {
    case class Agentsim(
      numAgents      : scala.Int,
      simulationName : java.lang.String
    )
    object Agentsim {
      def apply(c: com.typesafe.config.Config): BeamConfig.Beam.Agentsim = {
        BeamConfig.Beam.Agentsim(
          numAgents      = if(c.hasPathOrNull("numAgents")) c.getInt("numAgents") else 100,
          simulationName = if(c.hasPathOrNull("simulationName")) c.getString("simulationName") else "development"
        )
      }
    }
          
    case class Events(
      filterDist          : scala.Int,
      pathTraversalEvents : scala.List[java.lang.String]
    )
    object Events {
      def apply(c: com.typesafe.config.Config): BeamConfig.Beam.Events = {
        BeamConfig.Beam.Events(
          filterDist          = if(c.hasPathOrNull("filterDist")) c.getInt("filterDist") else 10000,
          pathTraversalEvents = $_L$_str(c.getList("pathTraversalEvents"))
        )
      }
    }
          
    case class Outputs(
      defaultLoggingLevel     : scala.Int,
      eventsFileOutputFormats : java.lang.String,
      explodeEventsIntoFiles  : scala.Boolean,
      outputDirectory         : java.lang.String,
      overrideLoggingLevels   : scala.List[BeamConfig.Beam.Outputs.OverrideLoggingLevels$Elm],
      writeEventsInterval     : scala.Int,
      writePlansInterval      : scala.Int
    )
    object Outputs {
      case class OverrideLoggingLevels$Elm(
        classname : java.lang.String,
        value     : scala.Int
      )
      object OverrideLoggingLevels$Elm {
        def apply(c: com.typesafe.config.Config): BeamConfig.Beam.Outputs.OverrideLoggingLevels$Elm = {
          BeamConfig.Beam.Outputs.OverrideLoggingLevels$Elm(
            classname = if(c.hasPathOrNull("classname")) c.getString("classname") else "beam.playground.metasim.events.ActionEvent",
            value     = if(c.hasPathOrNull("value")) c.getInt("value") else 1
          )
        }
      }
            
      def apply(c: com.typesafe.config.Config): BeamConfig.Beam.Outputs = {
        BeamConfig.Beam.Outputs(
          defaultLoggingLevel     = if(c.hasPathOrNull("defaultLoggingLevel")) c.getInt("defaultLoggingLevel") else 1,
          eventsFileOutputFormats = if(c.hasPathOrNull("eventsFileOutputFormats")) c.getString("eventsFileOutputFormats") else "csv",
          explodeEventsIntoFiles  = c.hasPathOrNull("explodeEventsIntoFiles") && c.getBoolean("explodeEventsIntoFiles"),
          outputDirectory         = if(c.hasPathOrNull("outputDirectory")) c.getString("outputDirectory") else "test/output",
          overrideLoggingLevels   = $_LBeamConfig_Beam_Outputs_OverrideLoggingLevels$Elm(c.getList("overrideLoggingLevels")),
          writeEventsInterval     = if(c.hasPathOrNull("writeEventsInterval")) c.getInt("writeEventsInterval") else 1,
          writePlansInterval      = if(c.hasPathOrNull("writePlansInterval")) c.getInt("writePlansInterval") else 0
        )
      }
      private def $_LBeamConfig_Beam_Outputs_OverrideLoggingLevels$Elm(cl:com.typesafe.config.ConfigList): scala.List[BeamConfig.Beam.Outputs.OverrideLoggingLevels$Elm] = {
        cl.asScala.map(cv => BeamConfig.Beam.Outputs.OverrideLoggingLevels$Elm(cv.asInstanceOf[com.typesafe.config.ConfigObject].toConfig)).toList
      }
    }
          
    case class Routing(
      gtfs : BeamConfig.Beam.Routing.Gtfs,
      otp  : BeamConfig.Beam.Routing.Otp
    )
    object Routing {
      case class Gtfs(
        crs           : java.lang.String,
        operatorsFile : java.lang.String,
        outputDir     : java.lang.String
      )
      object Gtfs {
        def apply(c: com.typesafe.config.Config): BeamConfig.Beam.Routing.Gtfs = {
          BeamConfig.Beam.Routing.Gtfs(
            crs           = if(c.hasPathOrNull("crs")) c.getString("crs") else "epsg:26910",
            operatorsFile = if(c.hasPathOrNull("operatorsFile")) c.getString("operatorsFile") else "src/main/resources/GTFSOperators.csv",
            outputDir     = if(c.hasPathOrNull("outputDir")) c.getString("outputDir") else "test/output/gtfs"
          )
        }
      }
            
      case class Otp(
        directory : java.lang.String,
        routerIds : scala.List[java.lang.String]
      )
      object Otp {
        def apply(c: com.typesafe.config.Config): BeamConfig.Beam.Routing.Otp = {
          BeamConfig.Beam.Routing.Otp(
            directory = if(c.hasPathOrNull("directory")) c.getString("directory") else "/home/dserdiuk/Projects/upwork/7Summits/beam/test/input/beam/sf-bay//model-inputs/otp",
            routerIds = $_L$_str(c.getList("routerIds"))
          )
        }
      }
            
      def apply(c: com.typesafe.config.Config): BeamConfig.Beam.Routing = {
        BeamConfig.Beam.Routing(
          gtfs = BeamConfig.Beam.Routing.Gtfs(c.getConfig("gtfs")),
          otp  = BeamConfig.Beam.Routing.Otp(c.getConfig("otp"))
        )
      }
    }
          
    def apply(c: com.typesafe.config.Config): BeamConfig.Beam = {
      BeamConfig.Beam(
        agentsim     = BeamConfig.Beam.Agentsim(c.getConfig("agentsim")),
        events       = BeamConfig.Beam.Events(c.getConfig("events")),
        outputs      = BeamConfig.Beam.Outputs(c.getConfig("outputs")),
        routing      = BeamConfig.Beam.Routing(c.getConfig("routing")),
        sharedInputs = if(c.hasPathOrNull("sharedInputs")) c.getString("sharedInputs") else "test/input/beam/sf-bay/"
      )
    }
  }
        
  def apply(c: com.typesafe.config.Config): BeamConfig = {
    BeamConfig(
      beam = BeamConfig.Beam(c.getConfig("beam"))
    )
  }

  private def $_L$_str(cl:com.typesafe.config.ConfigList): scala.List[java.lang.String] = {
    cl.asScala.map(cv => $_str(cv)).toList
  }
  private def $_expE(cv:com.typesafe.config.ConfigValue, exp:java.lang.String) = {
    val u: Any = cv.unwrapped
    new java.lang.RuntimeException(cv.origin.lineNumber +
      ": expecting: " + exp + " got: " +
      (if (u.isInstanceOf[java.lang.String]) "\"" + u + "\"" else u))
  }
  private def $_str(cv:com.typesafe.config.ConfigValue) =
    java.lang.String.valueOf(cv.unwrapped())
}
      
