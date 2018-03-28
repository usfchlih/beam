package beam.utils.scripts;

import beam.analysis.via.CSVWriter;
import org.apache.commons.lang.StringUtils;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;

import java.io.BufferedWriter;
import java.io.IOException;

public class ConvertXmlToCsv {
    private static final String DELIMITER = "\t";
    public static void main(String[] args) {
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        Network network = scenario.getNetwork();
        MatsimNetworkReader matsimNetworkReader = new MatsimNetworkReader(network);
        matsimNetworkReader.readFile("test/input/beamville/physsim-network.xml");
        CSVWriter csvWriter = new CSVWriter("test/input/beamville/output/physsim-network.csv");
        try (BufferedWriter bufferedWriter = csvWriter.getBufferedWriter()) {
            for (Link ss : network.getLinks().values()) {

                if (ss.getFromNode().getId() != null) bufferedWriter.append(ss.getId().toString());
                bufferedWriter.append(DELIMITER);
                if (ss.getFromNode().getId() != null) bufferedWriter.append(ss.getFromNode().getId().toString());
                bufferedWriter.append(DELIMITER);
                if (ss.getToNode().getId() != null) bufferedWriter.append(ss.getToNode().getId().toString());
                bufferedWriter.append(DELIMITER).append(String.valueOf(ss.getLength()));
                bufferedWriter.append(DELIMITER).append(String.valueOf(ss.getFreespeed()));
                bufferedWriter.append(DELIMITER).append(String.valueOf(ss.getCapacity()));
                bufferedWriter.append(DELIMITER).append(String.valueOf(ss.getNumberOfLanes()));
//            System.out.println("ONEWAY"+ss);
                String allowedModes = ss.getAllowedModes() == null ? "" : StringUtils.join(ss.getAllowedModes(), ",");
                bufferedWriter.append(DELIMITER).append(allowedModes);
                String origid = (ss.getAttributes().getAttribute("origid") == null) ? DELIMITER : DELIMITER + ss.getAttributes().getAttribute("origid");
                String type = (ss.getAttributes().getAttribute("type") == null) ? DELIMITER : DELIMITER + ss.getAttributes().getAttribute("type");
                bufferedWriter.append(origid).append(type);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            csvWriter.closeFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
