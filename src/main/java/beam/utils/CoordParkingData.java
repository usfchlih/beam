package beam.utils;

import akka.protobuf.ByteString;
import beam.analysis.via.CSVWriter;
import beam.utils.coordmodel.*;
import com.csvreader.CsvReader;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.geotools.data.FeatureReader;
import org.hsqldb.lib.StringUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fetch Parking data from Coord API
 * access_key may change after a period of time
 */
public class CoordParkingData {
    public static void main(String[] args) throws IOException {
        String filename = "";

        StringBuilder result = new StringBuilder();
        /*String latitude = "37.761479";
        String longitude = "-122.448245";
        String radius = "7.8";
        String duration = "1";
        String access_key = "sandbox-DOGkYsosaV-oFVBVoh-gYKIFtpFdpJycYX5Tc2-_AWA";
        String api = "https://api.sandbox.coord.co/v1/search/curbs/bylocation/all_rules?latitude=" + latitude + "&longitude=" + longitude + "&radius_km=" + radius + "&access_key=" + access_key;
        String line;
        URL url = new URL(api);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        rd.close();

        GsonBuilder builder = new GsonBuilder();
        CoordDetail o = builder.create().fromJson(result.toString(), CoordDetail.class);
*/
/*
    String startLongitude;
    String startLatitude;
    String endLongitude;
    double feePerHour;
    String endLatitude;
    double totalNoParkingLength;
    double totalNoStoppingLength;
    double totalPaidParkingLength;
    double totalMaxParkingDuration;
    */
        result = getDataFromFile();

        CoordDetail coordDetail = new GsonBuilder().create().fromJson(result.toString(), CoordDetail.class);

        StringBuilder stringBuilder = new StringBuilder();

        Map<String, OutputFormat> csvMap = new HashMap<>();
        /*
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
        */
//        stringBuilder.append(header);

        List<Feature> features = coordDetail.getFeatures();
        if (features == null || features.isEmpty()) {
            throw new IOException("File is Empty or Invalid");
        }
        Double startLong = features.get(0).getGeometry().getCoordinates().get(0).get(0);
        Double startLat = features.get(0).getGeometry().getCoordinates().get(0).get(1);
        Double endLong = features.get(features.size() - 1).getGeometry().getCoordinates().get(features.get(features.size() - 1).getGeometry().getCoordinates().size() - 1).get(0);
        Double endLat = features.get(features.size() - 1).getGeometry().getCoordinates().get(features.get(features.size() - 1).getGeometry().getCoordinates().size() - 1).get(1);
        stringBuilder.append("StartLong:").append(startLong).append(",StartLat:").append(startLat).append("EndLong:").append(endLong).append(",EndLat:").append(endLat);

        for (Feature feature : coordDetail.getFeatures()) {
            String curbId = feature.getProperties().getMetadata().getCurbId();
            OutputFormat outputFormat = null;
            if (!csvMap.isEmpty())
                outputFormat = csvMap.get(curbId);

            if (outputFormat == null)
                outputFormat = new OutputFormat();


            double startMeters = feature.getProperties().getMetadata().getDistanceStartMeters() == null ? 0.0 : feature.getProperties().getMetadata().getDistanceStartMeters();
            double endMeters = feature.getProperties().getMetadata().getDistanceEndMeters();
            long curbLength = Math.round((endMeters - startMeters) * 3.280839895013123);

            System.out.print("\n" + curbLength + " FT ");

            for (Rule rule : feature.getProperties().getRules()) {
                if (rule.getPermitted().isEmpty()) {
                    System.out.print(" " + OutputFormat.LengthHeading.NO_STOPPING);
                    addLengthToMap(curbLength,outputFormat,OutputFormat.LengthHeading.NO_STOPPING);
                } else {
                    for (Permitted permitted : rule.getPermitted()) {
                        switch (permitted) {
                            case PARK:
                                List<Price> prices = rule.getPrice();
                                Double pricePerHour = prices.get(0).getPricePerHour().getAmount();
                                if (pricePerHour == null || pricePerHour == 0) {

                                }
                                break;
                        }
                    }
                }
            }

            System.out.println("\nOUTPUT:\n" + stringBuilder);
            csvMap.put(curbId, outputFormat);
        }


        /*CSVWriter csv = new CSVWriter("sample.csv");
        csv.getBufferedWriter().append(stringBuilder.toString());
        csv.getBufferedWriter().flush();
        csv.closeFile();*/
    }

    private static StringBuilder getDataFromFile() {
        try (FileReader fileReader = new FileReader(new File("/home/abid/DEVELOPMENT/north/FILES/c2Y6MTQzODY.json")); BufferedReader rdr = new BufferedReader(fileReader)) {
            StringBuilder result = new StringBuilder();
//            CSVWriter csv = new CSVWriter("/home/abid/complete.json");
//            csv.getBufferedWriter().append(result.toString());
//            csv.getBufferedWriter().flush();
//            csv.closeFile();;

            String line = null;

            while ((line = rdr.readLine()) != null) {
                result.append(line);
            }
            return result;
        } catch (IOException e) {
            System.err.println("ERROR:" + e);
            return null;
        }
    }

    private static void addLengthToMap(double length, OutputFormat outputFormat, OutputFormat.LengthHeading lengthHeading) {
        Double prevLength = outputFormat.getPermittedTypeLengthMap().get(lengthHeading);
        if (prevLength == null) {
            outputFormat.getPermittedTypeLengthMap().put(lengthHeading, length);
        } else {
            outputFormat.getPermittedTypeLengthMap().put(lengthHeading, outputFormat.getPermittedTypeLengthMap().get(lengthHeading) + prevLength);
        }
    }

}