package beam.utils.scripts;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class ConvertXmlToCsv {
    public static void main(String[] args) {
        try {
            File xmlFile = new File("test/input/beamville/physsim-network.xml");
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
        }
    }
}
