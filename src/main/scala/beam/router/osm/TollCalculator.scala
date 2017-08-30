package beam.router.osm

import java.io.File
import java.nio.file.{Files, Path, Paths}

import com.conveyal.osmlib.{OSM, Way}

import scala.collection.JavaConverters._
import scala.collection.mutable.Map

object TollCalculator {

  case class Toll(charges: Vector[Charge],
                  vehicleTypes: Set[String],
                  isExclusionType: Boolean = false)

  case class Charge(amount: Double,
                    currency: String,
                    item: String,
                    timeUnit: Option[String] = None,
                    month: Option[ChargeDate] = None,
                    day: Option[ChargeDate]= None,
                    hour: Option[ChargeDate] = None)

  object Charge {
    def apply(charge: String): Charge = {
      charge.split(";").foreach(_.split(" "))
//      new Charge(amount, currency, item, timeUnit, month, day, hour)
      null
    }
  }

  trait ChargeDate { val on: String  }
  case class DiscreteDate(override val on: String) extends ChargeDate
  case class DateRange(override val on: String, till: String) extends ChargeDate


  val MIN_TOLL = 1.0
  var ways: Map[java.lang.Long, Toll] = _

  def fromDirectory(directory: Path): Unit = {
    /**
      * Checks whether its a osm.pbf feed and has fares data.
      *
      * @param file specific file to check.
      * @return true if a valid pbf.
      */
    def hasOSM(file: File): Boolean = file.getName.endsWith(".pbf")

    def loadOSM(osmSourceFile: String): Unit = {
      val dir = new File(osmSourceFile).getParentFile

      // Load OSM data into MapDB
      val osm = new OSM(new File(dir, "osm.mapdb").getPath)
      osm.readFromFile(osmSourceFile)
      readTolls(osm)
      osm.close()
    }

    def readTolls(osm: OSM) = {
      val ways = osm.ways.asScala.filter(ns => ns._2.tags != null && ns._2.tags.asScala.exists(t => (t.key == "toll" && t.value != "no") || t.key.startsWith("toll:")) && ns._2.tags.asScala.exists(_.key == "charge"))
      ways.map(w => (w._2, w._2.tags.asScala.filter(_.key == "charge")))
      //osm.nodes.values().asScala.filter(ns => ns.tags != null && ns.tags.size() > 1 && ns.tags.asScala.exists(t => (t.key == "fee" && t.value == "yes") || t.key == "charge") && ns.tags.asScala.exists(t => t.key == "toll" || (t.key == "barrier" && t.value == "toll_booth")))
    }

    if (Files.isDirectory(directory)) {
      directory.toFile.listFiles(hasOSM(_)).map(_.getAbsolutePath).headOption.foreach(loadOSM(_))
    }
  }

  def calcToll(osmIds: Vector[Long]): Double = {
    // TODO OSM data has no fee information, so using $1 as min toll, need to change with valid toll price
    ways.filter(w => osmIds.contains(w._1)).map(_._2.charges.map(_.amount).sum).sum
   
  }

  def main(args: Array[String]): Unit = {
    fromDirectory(Paths.get(args(0)))
  }
}
