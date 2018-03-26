package beam.utils.scripts;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.TravelTimeCalculatorConfigGroup;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.trafficmonitoring.TravelTimeCalculator;

public class GenerateLinkStatsFromEvents {

    /*private static final String EVENTS_FILE_PATH = "C:\\ns\\beam-project\\beam8-issue-245\\test\\input\\beamville\\output\\beamville_2018-03-26_03-35-03\\ITERS\\it.0\\0.physSimEvents.xml";
    private static final String NETWORK_FILE_PATH = "C:\\ns\\beam-project\\beam8-issue-245\\test\\input\\beamville\\output\\beamville_2018-03-26_03-35-03\\physSimNetwork.xml";
    private static final String outputFileName = "C:\\ns\\beam-project\\beam8-issue-245\\test\\input\\beamville\\output\\beamville_2018-03-26_03-35-03\\linkstats-test-" + System.currentTimeMillis() + ".txt.gz";*/
    private static final String EVENTS_FILE_PATH = "C:\\ns\\work\\issue_245\\10k_run0.10.events.xml";
    private static final String NETWORK_FILE_PATH = "C:\\ns\\work\\issue_245\\network_SF_Bay_detailed.xml";
    private static final String outputFileName = "C:\\ns\\work\\issue_245\\linkstats-test-" + System.currentTimeMillis() + ".txt.gz";


    LinkStatsByMinute linkStatsByMinute;

    GenerateLinkStatsFromEvents(){

        Config config = ConfigUtils.createConfig();
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        Network network = scenario.getNetwork();
        MatsimNetworkReader matsimNetworkReader= new MatsimNetworkReader(network);
        matsimNetworkReader.readFile(NETWORK_FILE_PATH);

        TravelTimeCalculatorConfigGroup defaultTravelTimeCalculator= config.travelTimeCalculator();
        TravelTimeCalculator travelTimeCalculator = new TravelTimeCalculator(network, defaultTravelTimeCalculator);
        EventsManager eventsManager = EventsUtils.createEventsManager();
        eventsManager.addHandler(travelTimeCalculator);

        /**
         *  The following code reads the events and throws them to all the handlers registered with the
         *  events manager.
         */
        linkStatsByMinute = new LinkStatsByMinute(network, outputFileName);
        linkStatsByMinute.notifyIterationStarts(eventsManager);

        MatsimEventsReader matsimEventsReader = new MatsimEventsReader(eventsManager);
        matsimEventsReader.readFile(EVENTS_FILE_PATH);
        linkStatsByMinute.notifyIterationEnds(0, travelTimeCalculator);
    }


    public void init(){

    }

    public static void main(String[] args){
        GenerateLinkStatsFromEvents generateLinkStatsFromEvents = new GenerateLinkStatsFromEvents();
        generateLinkStatsFromEvents.init();
    }
}
