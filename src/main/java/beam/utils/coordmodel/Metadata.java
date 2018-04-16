package beam.utils.coordmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("curb_id")
    @Expose
    private String curbId;
    @SerializedName("distance_end_meters")
    @Expose
    private double distanceEndMeters;
    @SerializedName("distance_start_meters")
    @Expose
    private double distanceStartMeters;
    @SerializedName("end_street_name")
    @Expose
    private String endStreetName;
    @SerializedName("side_of_street")
    @Expose
    private String sideOfStreet;
    @SerializedName("start_street_name")
    @Expose
    private String startStreetName;
    @SerializedName("street_name")
    @Expose
    private String streetName;
    @SerializedName("time_zone")
    @Expose
    private String timeZone;

    public String getCurbId() {
        return curbId;
    }

    public void setCurbId(String curbId) {
        this.curbId = curbId;
    }

    public double getDistanceEndMeters() {
        return distanceEndMeters;
    }

    public void setDistanceEndMeters(double distanceEndMeters) {
        this.distanceEndMeters = distanceEndMeters;
    }

    public double getDistanceStartMeters() {
        return distanceStartMeters;
    }

    public void setDistanceStartMeters(double distanceStartMeters) {
        this.distanceStartMeters = distanceStartMeters;
    }

    public String getEndStreetName() {
        return endStreetName;
    }

    public void setEndStreetName(String endStreetName) {
        this.endStreetName = endStreetName;
    }

    public String getSideOfStreet() {
        return sideOfStreet;
    }

    public void setSideOfStreet(String sideOfStreet) {
        this.sideOfStreet = sideOfStreet;
    }

    public String getStartStreetName() {
        return startStreetName;
    }

    public void setStartStreetName(String startStreetName) {
        this.startStreetName = startStreetName;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

}
