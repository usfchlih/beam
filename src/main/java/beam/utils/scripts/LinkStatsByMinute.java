package beam.utils.scripts;

import org.matsim.api.core.v01.network.Network;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.trafficmonitoring.TravelTimeCalculator;

import java.io.File;

public class LinkStatsByMinute {

    private static String BASE_PATH = new File("").getAbsolutePath();
    private static final String linkstatsFileName = BASE_PATH + "/test/input/equil-square/test-data/linkstats.txt.gz";

    private Network network;

    private BeamCalcLinkStatsMinute linkStats;
    private VolumesAnalyzerMinute volumes;


    public LinkStatsByMinute(Network network) {
        this.network = network;
        linkStats = new BeamCalcLinkStatsMinute(network);
    }


    public void notifyIterationEnds(int iteration, TravelTimeCalculator travelTimeCalculator) {

        linkStats.addData(volumes, travelTimeCalculator.getLinkTravelTimes());


            String fileName = this.linkstatsFileName;
            System.out.println("FileName -> " + fileName);
            linkStats.writeFile(fileName);

    }

    public void notifyIterationStarts(EventsManager eventsManager) {

        this.linkStats.reset();
        volumes = new VolumesAnalyzerMinute(60, 24 * 60 * 60 - 1, network);
        eventsManager.addHandler(volumes);


    }
}
