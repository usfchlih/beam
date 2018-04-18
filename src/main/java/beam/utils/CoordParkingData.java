package beam.utils;

import beam.analysis.via.CSVWriter;
import beam.utils.coordmodel.*;
import com.csvreader.CsvReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.hsqldb.lib.StringUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.List;

public class CoordParkingData {
    public static void main(String[] args) throws IOException {
        String filename = "";

        StringBuilder result = new StringBuilder();
        String latitude = "37.761479";
        String longitude = "-122.448245";
        String radius = "7.8";
        String duration = "1";
        String access_key = "sandbox-DOGkYsosaV-oFVBVoh-gYKIFtpFdpJycYX5Tc2-_AWA";
        String api = "https://api.sandbox.coord.co/v1/search/curbs/bylocation/all_rules?latitude=" + latitude + "&longitude=" + longitude + "&radius_km=" + radius + "&primary_use=park&access_key=" + access_key;
        String line;
        URL url = new URL(api);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();

//        GsonBuilder builder = new GsonBuilder();
//        CoordDetail o = builder.create().fromJson(result.toString(), CoordDetail.class);

//        CSVWriter csv = new CSVWriter("/home/abid/complete.json");
//        csv.getBufferedWriter().append(result.toString());
//        csv.getBufferedWriter().flush();
//        csv.closeFile();
//        FileReader fileReader = new FileReader(new File("/home/abid/complete.json"));
//        BufferedReader rdr = new BufferedReader(fileReader);
//        line = null;
//        StringBuffer stringBuffer = new StringBuffer();
//        while ((line = rdr.readLine()) != null) {
//            stringBuffer.append(line);
//        }

        CoordDetail coordDetail = new GsonBuilder().create().fromJson(result.toString(), CoordDetail.class);

        StringBuffer stringBuffer = new StringBuffer();
        String header = "type,geometry.coordinates,geometry.type,properties.metadata.curb_id,properties.metadata.distance_end_meters,distance_start_meters,properties.metadata.end_street_name," +
                "properties.metadata.side_of_street,properties.metadata.start_street_name,properties.metadata.street_name,properties.metadata.time_zone,properties.temporary_rules," +
                "rules" +
                "[{" +
                "properties.rules.other_vehicles_permitted|properties.rules.vehicle_type|properties.rules.permitted{}|properties.rules.price.price_per_hour{}|properties.rules.primary|properties.rules.max_duration|" +
                "times" +
                "[{" +
                "properties.rules.time.days{}|properties.rules.times.time_of_day_start|properties.rules.times.time_of_day_end" +
                "}]" +
                "}]";
        stringBuffer.append(header);
        for (Feature feature : coordDetail.getFeatures()) {
            stringBuffer.append("\n" + feature.getType());
            String coord = "[";
            for (List<Double> coordinates : feature.getGeometry().getCoordinates()) {
                coord += "{" + coordinates.get(0) + "|" + coordinates.get(1) + "}";
            }
            stringBuffer.append(",").append(coord).append("]");
            stringBuffer.append(",").append(feature.getGeometry().getType());
            stringBuffer.append(",").append(feature.getProperties().getMetadata().getCurbId());
            stringBuffer.append(",").append(feature.getProperties().getMetadata().getDistanceEndMeters());
            stringBuffer.append(",").append(feature.getProperties().getMetadata().getDistanceStartMeters());
            stringBuffer.append(",").append(feature.getProperties().getMetadata().getEndStreetName());
            stringBuffer.append(",").append(feature.getProperties().getMetadata().getSideOfStreet());
            stringBuffer.append(",").append(feature.getProperties().getMetadata().getStartStreetName());
            stringBuffer.append(",").append(feature.getProperties().getMetadata().getStreetName());
            stringBuffer.append(",").append(feature.getProperties().getMetadata().getTimeZone());
            stringBuffer.append(",").append(feature.getProperties().getTemporaryRules());

            stringBuffer.append(",[");
            for (Rule rule : feature.getProperties().getRules()) {
                stringBuffer.append("{");
                if (rule.getOtherVehiclesPermitted() == null || rule.getOtherVehiclesPermitted().isEmpty()) {
                    stringBuffer.append("null");
                } else {
                    String otherVehiclesPermitted = "";
                    for (Object other : rule.getOtherVehiclesPermitted()) {
                        otherVehiclesPermitted += "|"+(String) other;
                    }
                    stringBuffer.append("|{").append(otherVehiclesPermitted.substring(1, otherVehiclesPermitted.length())).append("}");
                }
                stringBuffer.append("|").append(rule.getVehicleType());
                stringBuffer.append("|{").append(StringUtils.join(rule.getPermitted(), "|")).append("}");


                if (rule.getPrice() == null || rule.getPrice().isEmpty()) {
                    stringBuffer.append("|null");
                } else {
                    String pricePerHour = "";
                    for (Price price : rule.getPrice()) {
                        pricePerHour += "|" + price.getPricePerHour().getAmount() + price.getPricePerHour().getCurrency();
                    }
                    stringBuffer.append("|{" + pricePerHour.substring(1, pricePerHour.length()) + "}");
                }

                stringBuffer.append("|").append(rule.getPrimary());
                stringBuffer.append("|").append(rule.getMaxDurationH());

                if (rule.getTimes() == null || rule.getTimes().isEmpty()) {
                    stringBuffer.append("|[{null");
                    stringBuffer.append("|null");
                    stringBuffer.append("|null}]");
                } else {
                    stringBuffer.append("|[");
                    for (Time time : rule.getTimes()) {
                        stringBuffer.append("{");
                        if (time.getDays() == null || time.getDays().isEmpty()) {
                            stringBuffer.append("{}");
                        } else {
                            String days = "";
                            for (Integer day : time.getDays()) {
                                days += "|" + day;
                            }
                            stringBuffer.append("{" + days.substring(1, days.length()) + "}");
                        }
                        stringBuffer.append("|").append(time.getTimeOfDayStart());
                        stringBuffer.append("|").append(time.getTimeOfDayEnd());
                        stringBuffer.append("}");
                    }
                    stringBuffer.append("]");
                }
                stringBuffer.append("}");
            }
            stringBuffer.append("]");
        }


        CSVWriter csv = new CSVWriter("sample.csv");
        csv.getBufferedWriter().append(stringBuffer.toString());
        csv.getBufferedWriter().flush();
        csv.closeFile();
    }
}
