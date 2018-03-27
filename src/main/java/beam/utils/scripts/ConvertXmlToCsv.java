package beam.utils.scripts;

import org.apache.commons.lang.StringUtils;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;

public class ConvertXmlToCsv {
    public static void main(String[] args) {
//        try {
        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        Network network = scenario.getNetwork();
        MatsimNetworkReader matsimNetworkReader = new MatsimNetworkReader(network);
        matsimNetworkReader.readFile("test/input/beamville/physsim-network.xml");
        for (Link ss : network.getLinks().values()) {
            System.out.print(ss.getId().toString());
            System.out.print("\t" + ss.getFromNode().getId().toString());
            System.out.print("\t" + ss.getToNode().getId().toString());
            System.out.print("\t" + ss.getLength());
            System.out.print("\t" + ss.getFreespeed());
            System.out.print("\t" + ss.getCapacity());
            System.out.print("\t" + ss.getNumberOfLanes());
//            System.out.println("ONEWAY"+ss);
            String allowedModes = ss.getAllowedModes() == null ? "" : StringUtils.join(ss.getAllowedModes(), ",");
            System.out.print("\t" + allowedModes);
            String origid = (ss.getAttributes().getAttribute("origid") == null) ? "\t" : "\t" + ss.getAttributes().getAttribute("origid");
            String type = (ss.getAttributes().getAttribute("type") == null) ? "\t" : "\t" + ss.getAttributes().getAttribute("type");
            System.out.print(origid + type);
            System.out.println();
        }
            /*File xmlFile = new File("test/input/beamville/physsim-network.xml");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            CsvHandler userhandler = new CsvHandler();
            saxParser.parse(xmlFile, userhandler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }*/
    }


}
