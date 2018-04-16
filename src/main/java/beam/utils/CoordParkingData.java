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
//        URL url = new URL("https://api.sandbox.coord.co/v1/search/parking/location");
        String latitude = "37.761479";
        String longitude = "-122.448245";
        String radius = "7.8";
        String duration = "1";
        String access_key = "sandbox-DOGkYsosaV-oFVBVoh-gYKIFtpFdpJycYX5Tc2-_AWA";
        String api = "https://api.sandbox.coord.co/v1/search/curbs/bylocation/all_rules?latitude=" + latitude + "&longitude=" + longitude + "&radius_km=" + radius + "&primary_use=park&access_key=" + access_key;
        String line;
        URL url = new URL(api);
//        URL url = new URL("https://api.sandbox.coord.co/v1/search/parking/location?latitude=" + latitude + "&longitude=" + longitude + "&radius_km=" + radius + "&duration_m=" + duration + "&access_key=" + access_key);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();

        GsonBuilder builder = new GsonBuilder();
        CoordDetail o = builder.create().fromJson(result.toString(), CoordDetail.class);

//        CSVWriter csv = new CSVWriter("/home/abid/complete.json");
//        csv.getBufferedWriter().append(result.toString().replaceAll("\\{\"geometry", "\n{\"geometry"));
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

//        CoordDetail coordDetail = new GsonBuilder().create().fromJson(stringBuffer.toString(), CoordDetail.class);
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
//                "properties.rules.other_vehicles_permitted,properties.rules.vehicle_type,properties.rules.permitted,properties.rules.price.price_per_hour,properties.rules.primary," +
//                "properties.rules.max_duration_h,properties.rules.times.days,properties.rules.times.time_of_day_start,properties.rules.times.time_of_day_end\n";
        stringBuffer.append(header);
        for (Feature feature : coordDetail.getFeatures()) {
            stringBuffer.append("\nfeature.type:" + feature.getType());
            String coord = "[";
            for (List<Double> coordinates : feature.getGeometry().getCoordinates()) {
                coord += "{" + coordinates.get(0) + "|" + coordinates.get(1) + "}";
            }
            stringBuffer.append(",geometry.coordinates:").append(coord).append("]");
            stringBuffer.append(",geometry.Type:").append(feature.getGeometry().getType());
            stringBuffer.append(",properties.metadata.CurbId:").append(feature.getProperties().getMetadata().getCurbId());
            stringBuffer.append(",properties.metadata.DisatanceEndMeters:").append(feature.getProperties().getMetadata().getDistanceEndMeters());
            stringBuffer.append(",properties.metadata.DistanceStartMeters:").append(feature.getProperties().getMetadata().getDistanceStartMeters());
            stringBuffer.append(",properties.metadata.EndStreetName:").append(feature.getProperties().getMetadata().getEndStreetName());
            stringBuffer.append(",properties.metadata.SideOfStreet:").append(feature.getProperties().getMetadata().getSideOfStreet());
            stringBuffer.append(",properties.metadata.StartStreetName:").append(feature.getProperties().getMetadata().getStartStreetName());
            stringBuffer.append(",properties.metadata.StreetName:").append(feature.getProperties().getMetadata().getStreetName());
            stringBuffer.append(",properties.metadata.TimeZone:").append(feature.getProperties().getMetadata().getTimeZone());
            stringBuffer.append(",Properties.TemporaryRules:").append(feature.getProperties().getTemporaryRules());

            stringBuffer.append(",rules[");
            for (Rule rule : feature.getProperties().getRules()) {
                stringBuffer.append("{");
                if (rule.getOtherVehiclesPermitted() == null || rule.getOtherVehiclesPermitted().isEmpty()) {
                    stringBuffer.append("properties.rules.other_vehicles_permitted:null");
                } else {
                    String otherVehiclesPermitted = "";
                    for (Object other : rule.getOtherVehiclesPermitted()) {
                        otherVehiclesPermitted += "|"+(String) other;
                    }
                    stringBuffer.append("|properties.rules.other_vehicles_permitted:{").append(otherVehiclesPermitted.substring(1, otherVehiclesPermitted.length())).append("}");
                }
                stringBuffer.append("|properties.rules.vehicle_type:").append(rule.getVehicleType());
                stringBuffer.append("|properties.rules.permitted:{").append(StringUtils.join(rule.getPermitted(), "|")).append("}");


                if (rule.getPrice() == null || rule.getPrice().isEmpty()) {
                    stringBuffer.append("|properties.rules.price.price_per_hour:null");
                } else {
                    String pricePerHour = "";
                    for (Price price : rule.getPrice()) {
                        pricePerHour += "|" + price.getPricePerHour().getAmount() + price.getPricePerHour().getCurrency();
                    }
                    stringBuffer.append("|properties.rules.price.price_per_hour:{" + pricePerHour.substring(1, pricePerHour.length()) + "}");
                }

                stringBuffer.append("|properties.rules.primary:").append(rule.getPrimary());
                stringBuffer.append("|properties.rules.max_duration:").append(rule.getMaxDurationH());

                if (rule.getTimes() == null || rule.getTimes().isEmpty()) {
                    stringBuffer.append("|times[{properties.rules.time.days:null");
                    stringBuffer.append("|properties.rules.times.time_of_day_start:null");
                    stringBuffer.append("|properties.rules.times.time_of_day_end:null}]");
                } else {
                    stringBuffer.append("|times[");
                    for (Time time : rule.getTimes()) {
                        stringBuffer.append("{");
                        if (time.getDays() == null || time.getDays().isEmpty()) {
                            stringBuffer.append("properties.rules.time.days:{}");
                        } else {
                            String days = "";
                            for (Integer day : time.getDays()) {
                                days += "|" + day;
                            }
                            stringBuffer.append("properties.rules.time.days:{" + days.substring(1, days.length()) + "}");
                        }
                        stringBuffer.append("|properties.rules.times.time_of_day_start:").append(time.getTimeOfDayStart());
                        stringBuffer.append("|properties.rules.times.time_of_day_end:").append(time.getTimeOfDayEnd());
                        stringBuffer.append("}");
                    }
                    stringBuffer.append("]");
                }
                stringBuffer.append("}");
            }
            stringBuffer.append("]");
        }


        CSVWriter csv = new CSVWriter("/home/abid/test.csv");
        csv.getBufferedWriter().append(stringBuffer.toString());
        csv.getBufferedWriter().flush();
        csv.closeFile();
    }
}
