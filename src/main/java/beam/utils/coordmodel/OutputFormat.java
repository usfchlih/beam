package beam.utils.coordmodel;

import sun.security.util.Length;

import java.util.*;

/**
 * Includes the columns for CSV
 *
 * @author abid
 */
public class OutputFormat {
    private String startLongitude;
    private String startLatitude;
    private String endLongitude;
    private double feePerHour;
    private String endLatitude;
    private double totalCurbLength;
    private double totalNoParkingLength;
    private double totalNoStoppingLength;
    private double totalPaidParkingLength;
    private double maxParkingDuration;
    private OutputFormat.LengthHeading lengthHeading;
    //    private Map<Integer, EnumMap<Permitted, String>> mapDayData;
    private Map<LengthHeading, Double> permittedTypeLengthMap;

    public OutputFormat() {
        permittedTypeLengthMap = new EnumMap<LengthHeading, Double>(LengthHeading.class);
    }

    public String getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(String startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }

    public double getFeePerHour() {
        return feePerHour;
    }

    public void setFeePerHour(double feePerHour) {
        this.feePerHour = feePerHour;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }

    public double getTotalCurbLength() {
        return totalCurbLength;
    }

    public void setTotalCurbLength(double totalCurbLength) {
        this.totalCurbLength = totalCurbLength;
    }

    public double getTotalNoParkingLength() {
        return totalNoParkingLength;
    }

    public void setTotalNoParkingLength(double totalNoParkingLength) {
        this.totalNoParkingLength = totalNoParkingLength;
    }

    public double getTotalNoStoppingLength() {
        return totalNoStoppingLength;
    }

    public void setTotalNoStoppingLength(double totalNoStoppingLength) {
        this.totalNoStoppingLength = totalNoStoppingLength;
    }

    public double getTotalPaidParkingLength() {
        return totalPaidParkingLength;
    }

    public void setTotalPaidParkingLength(double totalPaidParkingLength) {
        this.totalPaidParkingLength = totalPaidParkingLength;
    }

    public double getMaxParkingDuration() {
        return maxParkingDuration;
    }

    public void setMaxParkingDuration(double maxParkingDuration) {
        this.maxParkingDuration = maxParkingDuration;
    }

//    public EnumMap<Permitted, String> getDayData(Integer day) {
//        return mapDayData.get(day);
//    }

//    public void setDaydata(Integer dayNum, EnumMap<Permitted, String> val) {
//        this.mapDayData.put(dayNum, val);
//    }

    public Map<LengthHeading, Double> getPermittedTypeLengthMap() {
        return this.permittedTypeLengthMap;
    }


    public OutputFormat.LengthHeading getLengthHeading() {
        return this.lengthHeading;
    }

    public void setLengthHeading(OutputFormat.LengthHeading lengthHeading) {
        this.lengthHeading = lengthHeading;
    }

    public enum LengthHeading {

        NO_STOPPING("No Stopping"),
        NO_PARKING("No Parking"),
        PASSENGER_LOADING_ZONE("Passenger Loading Zone"),
        LOADING_ZONE("Loading Zone"),
        PAID_PARKING("Paid Parking"),
        FREE_PARKING("Free Parking");
        private final String value;
        private static final Map<String, OutputFormat.LengthHeading> CONSTANTS = new HashMap<>();

        static {
            for (OutputFormat.LengthHeading c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private LengthHeading(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static OutputFormat.LengthHeading fromValue(String value) {
            OutputFormat.LengthHeading constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }
}
