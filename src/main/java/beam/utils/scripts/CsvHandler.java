package beam.utils.scripts;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CsvHandler extends DefaultHandler {

    boolean origid = false;
    boolean type = false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("link")) {
            String id = attributes.getValue("id");
            String from = attributes.getValue("from");
            String to = attributes.getValue("to");
            String length = attributes.getValue("length");
            String freespeed = attributes.getValue("freespeed");
            String capacity = attributes.getValue("capacity");
            String permlanes = attributes.getValue("permlanes");
            String oneway = attributes.getValue("oneway");
            String modes = attributes.getValue("modes");
            System.out.print(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s", id, from, to, length, freespeed, capacity, permlanes, oneway, modes));
        } else if (qName.equalsIgnoreCase("attribute")) {
            switch (attributes.getValue("name")) {
                case "origid":
                    origid = true;
                    break;
                case "type":
                    type = true;
                    break;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("link")) {
            System.out.println("");
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (origid) {
            System.out.print("\t" + new String(ch, start, length));
            origid = false;
        } else if (type) {
            System.out.print("\t" + new String(ch, start, length));
            origid = false;
        } else
            System.out.print("\t");
    }
}
