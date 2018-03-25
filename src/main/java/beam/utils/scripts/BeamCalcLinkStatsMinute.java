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
import org.matsim.analysis.VolumesAnalyzer;
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

    private double volScaleFactor = 1.0;

    private int count = 0;
    private final Map<Id<Link>, LinkData> linkData;
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

    public void addData(final VolumesAnalyzer analyzer, final TravelTime ttimes) {
        this.count++;
        // TODO verify ttimes has hourly timeBin-Settings

        // go through all links
        for (Id<Link> linkId : this.linkData.keySet()) {

            // retrieve link from link ID
            Link link = this.network.getLinks().get(linkId);

            // get the volumes for the link ID from the analyzier
            double[] volumes = analyzer.getVolumesPerHourForLink(linkId);

            // get the destination container for the data from link data (could have gotten this through iterator right away)
            LinkData data = this.linkData.get(linkId);

            // prepare the sum variables (for volumes);
            long sumVolumes = 0; // daily (0-24) sum

            // go through all hours:
            for (int hour = 0; hour < this.nofBins; hour++) {

                // get travel time for hour
                double ttime = ttimes.getLinkTravelTime(link, hour*timeInterval, null, null);

                // add for daily sum:
                sumVolumes += volumes[hour];

                // the following has something to do with the fact that we are doing this for multiple iterations.  So there are variations.
                // this collects min and max.  There is, however, no good control over how many iterations this is collected.
                if (this.count == 1) {
                    data.volumes[MIN][hour] = volumes[hour];
                    data.volumes[MAX][hour] = volumes[hour];
                    data.ttimes[MIN][hour] = ttime;
                    data.ttimes[MAX][hour] = ttime;
                } else {
                    if (volumes[hour] < data.volumes[MIN][hour]) data.volumes[MIN][hour] = volumes[hour];
                    if (volumes[hour] > data.volumes[MAX][hour]) data.volumes[MAX][hour] = volumes[hour];
                    if (ttime < data.ttimes[MIN][hour]) data.ttimes[MIN][hour] = ttime;
                    if (ttime > data.ttimes[MAX][hour]) data.ttimes[MAX][hour] = ttime;
                }

                // this is the regular summing up for each hour
                data.volumes[SUM][hour] += volumes[hour];
                data.ttimes[SUM][hour] += volumes[hour] * ttime;
            }
            // dataVolumes[.][nofBins] are daily (0-24) values
            if (this.count == 1) {
                data.volumes[MIN][this.nofBins] = sumVolumes;
                data.volumes[SUM][this.nofBins] = sumVolumes;
                data.volumes[MAX][this.nofBins] = sumVolumes;
            } else {
                if (sumVolumes < data.volumes[MIN][this.nofBins]) data.volumes[MIN][this.nofBins] = sumVolumes;
                data.volumes[SUM][this.nofBins] += sumVolumes;
                if (sumVolumes > data.volumes[MAX][this.nofBins]) data.volumes[MAX][this.nofBins] = sumVolumes;
            }
        }
    }

    public void reset() {
        this.linkData.clear();
        this.count = 0;
        log.info( " resetting `count' to zero.  This info is here since we want to check when this" +
                " is happening during normal simulation runs.  kai, jan'11") ;

        // initialize our data-table
        for (Link link : this.network.getLinks().values()) {
            LinkData data = new LinkData(new double[NOF_STATS][this.nofBins + 1], new double[NOF_STATS][this.nofBins]);
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
            for (Map.Entry<Id<Link>, LinkData> entry : this.linkData.entrySet()) {

                for (int i = 0; i <= this.nofBins; i++) {
                    for (int j= MIN; j<= SUM; j++){
                        Id<Link> linkId = entry.getKey();
                        LinkData data = entry.getValue();
                        Link link = this.network.getLinks().get(linkId);

                        out.write(linkId.toString());
                        out.write("\t" + link.getFromNode().getId().toString());
                        out.write("\t" + link.getToNode().getId().toString());

                        //WRITE HOUR
                        if (i < this.nofBins){
                            out.write("\t" + Double.toString(i) +" - "+ Double.toString(i+1));
                        }
                        else
                        {
                            out.write("\t" + Double.toString(0) +" - "+ Double.toString(this.nofBins));
                        }

                        out.write("\t" + Double.toString(link.getLength()));
                        out.write("\t" + Double.toString(link.getFreespeed()));
                        out.write("\t" + Double.toString(link.getCapacity()));

                        //WRITE STAT_TYPE
                        out.write("\t" + statType[j]);

                        //WRITE VOLUME
                        if (j == SUM ){
                            out.write("\t" + Double.toString((data.volumes[j][i]) / this.count));
                        }
                        else {
                            out.write("\t" + Double.toString(data.volumes[j][i]));
                        }

                        //WRITE TRAVELTIME

                        if (j == MIN && i< this.nofBins){
                            String ttimesMin = Double.toString(data.ttimes[MIN][i]);
                            out.write("\t" + ttimesMin);

                        }
                        else if ( j == SUM && i< this.nofBins){
                            String ttimesMin = Double.toString(data.ttimes[MIN][i]);
                            if (data.volumes[SUM][i] == 0) {
                                // nobody traveled along the link in this hour, so we cannot calculate an average
                                // use the value available or the minimum instead (min and max should be the same, =freespeed)
                                double ttsum = data.ttimes[SUM][i];
                                if (ttsum != 0.0) {
                                    out.write("\t" + Double.toString(ttsum));
                                } else {
                                    out.write("\t" + ttimesMin);
                                }
                            } else {
                                double ttsum = data.ttimes[SUM][i];
                                if (ttsum == 0) {
                                    out.write("\t" + ttimesMin);
                                } else {
                                    out.write("\t" + Double.toString(ttsum / data.volumes[SUM][i]));
                                }
                            }
                        }
                        else if (j == MAX && i< this.nofBins){
                            String ttimesMin = Double.toString(data.ttimes[MIN][i]);
                            out.write("\t" + Double.toString(data.ttimes[MAX][i]));
                        }
                        out.write("\n");
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
