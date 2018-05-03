package beam.sim.metrics

import java.nio.LongBuffer

import akka.actor.{Actor, Props}
import beam.sim.metrics.MetricsPrinter.{Print, Subscribe}
import com.typesafe.scalalogging.LazyLogging
import kamon.Kamon
import kamon.metric.SubscriptionsDispatcher.TickMetricSnapshot
import kamon.metric.instrument.CollectionContext
import kamon.metric.instrument.Time.{Milliseconds, Nanoseconds}
import kamon.metric.{Entity, EntitySnapshot}

class MetricsPrinter(val includes: Seq[String], val excludes: Seq[String]) extends Actor with LazyLogging {
  var iterationNumber = 0
  var metricStore: Map[Entity, EntitySnapshot] = null
  val collectionContext = new CollectionContext {
    val buffer: LongBuffer = LongBuffer.allocate(100000)
  }

  def receive = {
    case Subscribe(category, selection) =>
      Kamon.metrics.subscribe(category, selection, self)

    case tickSnapshot: TickMetricSnapshot =>
      if(metricStore == null) metricStore = tickSnapshot.metrics
      else {
        metricStore = metricStore.map(m => {
          var snap = m._2
          val ms = tickSnapshot.metrics.filter(_._1 == m._1)
          ms.foreach(cm => {
            snap = snap.merge(cm._2, collectionContext)
          })
          (m._1, snap)
        })
//        println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
//        metricStore.map(_._2.histogram("histogram").get.numberOfMeasurements).foreach(println)
//        println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
      }

    case Print(ins, exs) =>
      if(metricStore != null) {
        val counters = metricStore.filterKeys(_.category == "counter")
        val histograms = metricStore.filterKeys(h => h.category == "histogram" && ins.contains(h.name))

        var text =
          s"""
             |=======================================================================================
             | Performance Benchmarks (iteration no: ${iterationNumber})
          """.stripMargin

        if(ins == null) {
          histograms.foreach { case (e, s) => text += toHistogramString(e, s) }
          counters.foreach { case (e, s) => text += toCounterString(e, s) }
        } else {
          ins.foreach { case i =>
            histograms.filterKeys(_.name == i).foreach { case (e, s) =>
              text += toHistogramString(e, s)
            }
          }
        }

        text +=
          """
            |
            |=======================================================================================
          """.stripMargin

        logger.info(text)

        metricStore = null
        iterationNumber = iterationNumber + 1
      }
  }

  private def toHistogramString(e: Entity, s: EntitySnapshot): String = {
    val hs = s.histogram("histogram").get.scale(Nanoseconds, Milliseconds)
    val num = hs.numberOfMeasurements
    val ttime = hs.sum
    val p99_9 = hs.percentile(99.9)

    s"""
       | ${e.name} -> count: $num; average time: ${ttime / num} [ms]; 99.9 percentile: $p99_9 [ms]; total time: ${ttime / 1000} [s]""".stripMargin
  }

  private def toCounterString(e: Entity, s: EntitySnapshot): String = {
    val cs = s.counter("counter").get

    s"""
       | ${e.name} -> count: ${cs.count}""".stripMargin
  }
}

object MetricsPrinter {
  case class Subscribe(category: String, selection: String)
  case class Print(includes: Seq[String], excludes: Seq[String])
  def props() = Props(new MetricsPrinter(null, null))
}