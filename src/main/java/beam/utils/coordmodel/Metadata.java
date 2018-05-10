
package beam.utils.coordmodel;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Identifying information for a particular segment of curb.
 *
 * @author abid
 */
public class Metadata {

    /**
     * The ID of the curb that this segment is on.
     */
    @SerializedName("curb_id")
    @Expose
    private String curbId;
    /**
     * The distance from the start of the curb where this segment ends, in meters.
     *
     *
     */
    @SerializedName("distance_end_meters")
    @Expose
    private Double distanceEndMeters;
    /**
     * The distance from the start of the curb where this segment begins, in meters.
     */
    @SerializedName("distance_start_meters")
    @Expose
    private Double distanceStartMeters;
    /**
     * The name of the street where this segment's curb ends. distance_end_meters is measured
     * from this street.
     */
    @SerializedName("end_street_name")
    @Expose
    private String endStreetName;
    /**
     * The side of the street this segment is on, as a compass direction.
     *
     *
     */
    @SerializedName("side_of_street")
    @Expose
    private Metadata.SideOfStreet sideOfStreet;
    /**
     * The name of the street where this segment's curb starts. `distance_start_meters` and
     * `distance_end_meters` are measured from the intersection with street.
     * <p>
     * If `start_street_name` is "DEAD END", this curb starts at a dead end. If
     * `start_street_name` is the same as `street_name`, this curb starts somewhere other than
     * an intersection between two named streets. For instance, this could mean that the street
     * splits.
     */
    @SerializedName("start_street_name")
    @Expose
    private String startStreetName;
    /**
     * The name of the street that this segment is on.
     *
     */
    @SerializedName("street_name")
    @Expose
    private String streetName;
    /**
     * The time zone (see https://www.iana.org/time-zones) that this rule's times are in.
     *
     *
     */
    @SerializedName("time_zone")
    @Expose
    private String timeZone;

    /**
     * The ID of the curb that this segment is on.
     *
     */
    public String getCurbId() {
        return curbId;
    }

    /**
     * The ID of the curb that this segment is on.
     *
     */
    public void setCurbId(String curbId) {
        this.curbId = curbId;
    }

    /**
     * The distance from the start of the curb where this segment ends, in meters.
     *
     *
     */
    public Double getDistanceEndMeters() {
        return distanceEndMeters;
    }

    /**
     * The distance from the start of the curb where this segment ends, in meters.
     *
     *
     */
    public void setDistanceEndMeters(Double distanceEndMeters) {
        this.distanceEndMeters = distanceEndMeters;
    }

    /**
     * The distance from the start of the curb where this segment begins, in meters.
     *
     *
     */
    public Double getDistanceStartMeters() {
        return distanceStartMeters;
    }

    /**
     * The distance from the start of the curb where this segment begins, in meters.
     *
     *
     */
    public void setDistanceStartMeters(Double distanceStartMeters) {
        this.distanceStartMeters = distanceStartMeters;
    }

    /**
     * The name of the street where this segment's curb ends. distance_end_meters is measured
     * from this street.
     *
     *
     */
    public String getEndStreetName() {
        return endStreetName;
    }

    /**
     * The name of the street where this segment's curb ends. distance_end_meters is measured
     * from this street.
     *
     *
     */
    public void setEndStreetName(String endStreetName) {
        this.endStreetName = endStreetName;
    }

    /**
     * The side of the street this segment is on, as a compass direction.
     *
     *
     */
    public Metadata.SideOfStreet getSideOfStreet() {
        return sideOfStreet;
    }

    /**
     * The side of the street this segment is on, as a compass direction.
     *
     *
     */
    public void setSideOfStreet(Metadata.SideOfStreet sideOfStreet) {
        this.sideOfStreet = sideOfStreet;
    }

    /**
     * The name of the street where this segment's curb starts. `distance_start_meters` and
     * `distance_end_meters` are measured from the intersection with street.
     *
     * If `start_street_name` is "DEAD END", this curb starts at a dead end. If
     * `start_street_name` is the same as `street_name`, this curb starts somewhere other than
     * an intersection between two named streets. For instance, this could mean that the street
     * splits.
     *
     *
     */
    public String getStartStreetName() {
        return startStreetName;
    }

    /**
     * The name of the street where this segment's curb starts. `distance_start_meters` and
     * `distance_end_meters` are measured from the intersection with street.
     *
     * If `start_street_name` is "DEAD END", this curb starts at a dead end. If
     * `start_street_name` is the same as `street_name`, this curb starts somewhere other than
     * an intersection between two named streets. For instance, this could mean that the street
     * splits.
     *
     *
     */
    public void setStartStreetName(String startStreetName) {
        this.startStreetName = startStreetName;
    }

    /**
     * The name of the street that this segment is on.
     *
     */
    public String getStreetName() {
        return streetName;
    }

    /**
     * The name of the street that this segment is on.
     *
     */
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    /**
     * The time zone (see https://www.iana.org/time-zones) that this rule's times are in.
     *
     *
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * The time zone (see https://www.iana.org/time-zones) that this rule's times are in.
     *
     *
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public enum SideOfStreet {

        @SerializedName("N")
        N("N"),
        @SerializedName("NE")
        NE("NE"),
        @SerializedName("E")
        E("E"),
        @SerializedName("SE")
        SE("SE"),
        @SerializedName("S")
        S("S"),
        @SerializedName("SW")
        SW("SW"),
        @SerializedName("W")
        W("W"),
        @SerializedName("NW")
        NW("NW");
        private final String value;
        private static final Map<String, Metadata.SideOfStreet> CONSTANTS = new HashMap<>();

        static {
            for (Metadata.SideOfStreet c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private SideOfStreet(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Metadata.SideOfStreet fromValue(String value) {
            Metadata.SideOfStreet constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
