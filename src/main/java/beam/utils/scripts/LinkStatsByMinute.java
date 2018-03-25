package beam.utils.scripts;

import org.matsim.api.core.v01.network.Network;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.trafficmonitoring.TravelTimeCalculator;

public class LinkStatsByMinute {


    private Network network;

    private BeamCalcLinkStatsMinute linkStats;
    private VolumesAnalyzerMinute volumes;

    private String outputFileName;


    public LinkStatsByMinute(Network network, String outputFileName) {
        this.network = network;
        linkStats = new BeamCalcLinkStatsMinute(network);
        this.outputFileName = outputFileName;
    }


    public void notifyIterationEnds(int iteration, TravelTimeCalculator travelTimeCalculator) {

        linkStats.addData(volumes, travelTimeCalculator.getLinkTravelTimes());


            System.out.println("FileName -> " + outputFileName);
            linkStats.writeFile(outputFileName);

    }

    public void notifyIterationStarts(EventsManager eventsManager) {

        this.linkStats.reset();
        volumes = new VolumesAnalyzerMinute(60, 24 * 60 * 60 - 1, network);
        eventsManager.addHandler(volumes);


    }
}
