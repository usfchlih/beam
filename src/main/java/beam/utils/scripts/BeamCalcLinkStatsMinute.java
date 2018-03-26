/* *********************************************************************** *
 * project: org.matsim.*
 * CalcLinkStats.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package beam.utils.scripts;


import org.apache.log4j.Logger;
import org.matsim.analysis.CalcLinkStats;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.utils.io.IOUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

public class BeamCalcLinkStatsMinute {

    private final static Logger log = Logger.getLogger(CalcLinkStats.class);

    private static class LinkData {
        public final double[][] volumes;
        public final double[][] ttimes;

        public LinkData(final double[][] linksVolumes, final double[][] linksTTimes) {
            this.volumes = linksVolumes.clone();
            this.ttimes = linksTTimes.clone();
        }
    }

    private static class LinkDataMap {
        public final Map<Integer, Map<Integer, Double>> volumes;
        public final Map<Integer, Map<Integer, Double>> ttimes;

        public LinkDataMap() {
            this.volumes = new TreeMap<>();
            this.ttimes = new TreeMap<>();
        }
    }

    private double volScaleFactor = 1.0;

    private int count = 0;
    //private final Map<Id<Link>, LinkData> linkData;
    private final Map<Id<Link>, LinkDataMap> linkData;
    private final int nofBins;
    private final Network network;

    private static final int MIN = 0;
    private static final int MAX = 1;
    private static final int SUM = 2;
    private static final String[] statType = {"MIN","MAX","AVG"};

    private static final int NOF_STATS = 3;

    private final int timeInterval;
    private static final int hourInterval = 3600; // hour interval
    private static final int minuteInterval = 60; // minute interval


    public BeamCalcLinkStatsMinute(final Network network) {
        this.network = network;
        this.linkData = new TreeMap<>();

        this.timeInterval = minuteInterval;
        this.nofBins = 24 * (3600/this.timeInterval);

        reset();
    }

    /**
     * @param network
     * @param vol_scale_factor scaling factor when reading in values from a file
     *
     *
     */
    public BeamCalcLinkStatsMinute(final Network network, double vol_scale_factor) {
        this(network);
        this.volScaleFactor = vol_scale_factor;
    }

    public void addData(final VolumesAnalyzerMinute analyzer, final TravelTime ttimes) {
        this.count++;
        // TODO verify ttimes has hourly timeBin-Settings

        // go through all links
        for (Id<Link> linkId : this.linkData.keySet()) {

            System.out.println("LinkId " + linkId.toString());
            // retrieve link from link ID
            Link link = this.network.getLinks().get(linkId);

            // get the volumes for the link ID from the analyzier
            double[] volumes = analyzer.getVolumesPerMinuteForLink(linkId);

            // get the destination container for the data from link data (could have gotten this through iterator right away)
            LinkDataMap data = this.linkData.get(linkId);

            System.out.println("LinkId " + linkId.toString());

            // go through all hours:
            for (int hour = 0; hour < this.nofBins; hour++) {
                Double volumePerHour = volumes[hour];

                if(volumePerHour != 0) {
                    // get travel time for hour
                    double ttime = ttimes.getLinkTravelTime(link, hour * timeInterval, null, null);

                    // the following has something to do with the fact that we are doing this for multiple iterations.  So there are variations.
                    // this collects min and max.  There is, however, no good control over how many iterations this is collected.
                    Map<Integer, Double> minVolumeList = data.volumes.get(MIN) == null ? new TreeMap<>() : data.volumes.get(MIN);
                    Map<Integer, Double> maxVolumeList = data.volumes.get(MAX) == null ? new TreeMap<>() : data.volumes.get(MAX);

                    Map<Integer, Double> minTtimesList = data.ttimes.get(MIN) == null ? new TreeMap<>() : data.ttimes.get(MIN);
                    Map<Integer, Double> maxTtimesList = data.ttimes.get(MAX) == null ? new TreeMap<>() : data.ttimes.get(MAX);

                    Map<Integer, Double> sumVolumeList = data.volumes.get(SUM) == null ? new TreeMap<>() : data.volumes.get(SUM);
                    Map<Integer, Double> sumTtimesList = data.ttimes.get(SUM) == null ? new TreeMap<>() : data.ttimes.get(SUM);


                    minVolumeList.put(hour, volumePerHour);
                    maxVolumeList.put(hour, volumePerHour);
                    minTtimesList.put(hour, ttime);
                    maxTtimesList.put(hour, ttime);
                    sumVolumeList.put(hour, volumePerHour);
                    sumTtimesList.put(hour, (ttime * volumePerHour));

                    data.volumes.put(MIN, minVolumeList);
                    data.volumes.put(MAX, maxVolumeList);
                    data.volumes.put(SUM, sumVolumeList);

                    data.ttimes.put(MIN, minTtimesList);
                    data.ttimes.put(MAX, maxTtimesList);
                    data.ttimes.put(SUM, sumTtimesList);
                }
            }

            this.linkData.put(linkId, data);
        }
    }

    public void reset() {
        this.linkData.clear();
        this.count = 0;
        log.info( " resetting `count' to zero.  This info is here since we want to check when this" +
                " is happening during normal simulation runs.  kai, jan'11") ;
        System.out.println("Going to reset");
        // initialize our data-table
        for (Link link : this.network.getLinks().values()) {
            //LinkData data = new LinkData(new double[NOF_STATS][this.nofBins + 1], new double[NOF_STATS][this.nofBins]);
            LinkDataMap data = new LinkDataMap();
            this.linkData.put(link.getId(), data);
        }

    }

    public void writeFile(final String filename) {
        BufferedWriter out = null;
        try {
            out = IOUtils.getBufferedWriter(filename);

            // write header
            out.write("LINK\tFROM\tTO\tHOUR\tLENGTH\tFREESPEED\tCAPACITY\tSTAT.TYPE\tVOLUME\tTRAVELTIME");

            out.write("\n");

            // write data
            for (Map.Entry<Id<Link>, LinkDataMap> entry : this.linkData.entrySet()) {

                // In case we use <= in the below loop we get the sums in the last 3 rows for a specific link
                for (int i = 0; i < this.nofBins; i++) {
                    for (int j= MIN; j<= SUM; j++){

                        Id<Link> linkId = entry.getKey();
                        LinkDataMap data = entry.getValue();

                        Map<Integer, Double> dataVolumes = data.volumes.get(j);

                        if(dataVolumes != null) {

                            Double dataVolumes2 = dataVolumes.get(i);

                            if(dataVolumes2 != null) {
                                Link link = this.network.getLinks().get(linkId);

                                out.write(linkId.toString());
                                out.write("\t" + link.getFromNode().getId().toString());
                                out.write("\t" + link.getToNode().getId().toString());

                                //WRITE HOUR
                                if (i < this.nofBins) {
                                    out.write("\t" + Double.toString(i) + " - " + Double.toString(i + 1));
                                } else {
                                    out.write("\t" + Double.toString(0) + " - " + Double.toString(this.nofBins));
                                }

                                out.write("\t" + Double.toString(link.getLength()));
                                out.write("\t" + Double.toString(link.getFreespeed()));
                                out.write("\t" + Double.toString(link.getCapacity()));

                                //WRITE STAT_TYPE
                                out.write("\t" + statType[j]);


                                //WRITE VOLUME
                                if (j == SUM) {
                                    out.write("\t" + Double.toString((data.volumes.get(j).get(i)) / this.count));
                                } else {
                                    out.write("\t" + Double.toString(data.volumes.get(j).get(i)));
                                }

                                //WRITE TRAVELTIME

                                if (j == MIN && i < this.nofBins) {
                                    String ttimesMin = Double.toString(data.ttimes.get(MIN).get(i));
                                    out.write("\t" + ttimesMin);

                                } else if (j == SUM && i < this.nofBins) {
                                    String ttimesMin = Double.toString(data.ttimes.get(MIN).get(i));
                                    if (data.volumes.get(SUM).get(i) == 0) {
                                        // nobody traveled along the link in this hour, so we cannot calculate an average
                                        // use the value available or the minimum instead (min and max should be the same, =freespeed)
                                        double ttsum = data.ttimes.get(SUM).get(i);
                                        if (ttsum != 0.0) {
                                            out.write("\t" + Double.toString(ttsum));
                                        } else {
                                            out.write("\t" + ttimesMin);
                                        }
                                    } else {
                                        double ttsum = data.ttimes.get(SUM).get(i);
                                        if (ttsum == 0) {
                                            out.write("\t" + ttimesMin);
                                        } else {
                                            out.write("\t" + Double.toString(ttsum / data.volumes.get(SUM).get(i)));
                                        }
                                    }
                                } else if (j == MAX && i < this.nofBins) {
                                    out.write("\t" + Double.toString(data.ttimes.get(MAX).get(i)));
                                }
                                out.write("\n");
                            }
                        }
                    }

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.warn("Could not close output-stream.", e);
                }
            }
        }
    }




}
