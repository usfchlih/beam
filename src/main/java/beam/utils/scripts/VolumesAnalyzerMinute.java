package beam.utils.scripts;

import org.matsim.analysis.VolumesAnalyzer;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;

public class VolumesAnalyzerMinute extends VolumesAnalyzer {

    private int timeBinSize;
    private int maxTime;
    private int maxSlotIndex;


    public VolumesAnalyzerMinute(final int timeBinSize, final int maxTime, final Network network){
        super(timeBinSize, maxTime, network);

        this.timeBinSize = timeBinSize;
        this.maxTime = maxTime;
        this.maxSlotIndex = (this.maxTime/this.timeBinSize) + 1;
    }

    public double[] getVolumesPerMinuteForLink(final Id<Link> linkId) {
        //if (3600.0 % this.timeBinSize != 0) log.error("Volumes per hour and per link probably not correct!");

        double[] volumes = new double[24 * 60];

        int[] volumesForLink = this.getVolumesForLink(linkId);
        if (volumesForLink == null) return volumes;

        int slotsPerHour = (int)(3600.0 / this.timeBinSize);
        for (int hour = 0; hour < (24 * 60); hour++) {
            double time = hour * 3600.0;
            for (int i = 0; i < slotsPerHour; i++) {
                volumes[hour] += volumesForLink[this.getTimeSlotIndex(time)];
                time += this.timeBinSize;
            }
        }
        return volumes;
    }

    private int getTimeSlotIndex(final double time) {
        if (time > this.maxTime) {
            return this.maxSlotIndex;
        }
        return ((int)time / this.timeBinSize);
    }
}
