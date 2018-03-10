package beam.integration

import beam.sim.BeamHelper
import com.typesafe.config.ConfigValueFactory
import org.scalatest.{Matchers, WordSpecLike}

/**
  * Created by fdariasm on 29/08/2017
  *
  */

class TransitPriceSpec extends WordSpecLike with Matchers with BeamHelper with IntegrationSpecCommon {

  "Running beam with modeChoice ModeChoiceMultinomialLogit and increasing transitPrice value" must {
    "create more entries for mode choice transit as value increases" in {
      val inputTransitPrice = Seq(0.1, 1.0)
      val modeChoice = inputTransitPrice.map(tc => new StartWithCustomConfig(
        baseConfig
          .withValue("beam.agentsim.agents.modalBehaviors.modeChoiceClass", ConfigValueFactory.fromAnyRef("ModeChoiceMultinomialLogit"))
          .withValue("beam.agentsim.tuning.transitPrice", ConfigValueFactory.fromAnyRef(tc))
      ).groupedCount)

      val tc = modeChoice
        .map(_.get("transit"))
        .filter(_.isDefined)
        .map(_.get)

      //      val z1 = tc.drop(1)
      //      val z2 = tc.dropRight(1)
      //      val zip = z2 zip z1

      //      println("Transit")
      //      println(tc)
      //      println(z1)
      //      println(z2)
      //      println(zip)

      isOrdered(tc)((a, b) => a >= b) shouldBe true
    }
  }


}
