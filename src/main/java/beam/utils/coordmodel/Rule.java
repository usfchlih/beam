package beam.utils.coordmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Rule {

    @SerializedName("other_vehicles_permitted")
    @Expose
    private List<Object> otherVehiclesPermitted = null;
    @SerializedName("permitted")
    @Expose
    private List<String> permitted = null;
    @SerializedName("price")
    @Expose
    private List<Price> price = null;
    @SerializedName("primary")
    @Expose
    private String primary;
    @SerializedName("times")
    @Expose
    private List<Time> times = null;
    @SerializedName("vehicle_type")
    @Expose
    private String vehicleType;
    @SerializedName("max_duration_h")
    @Expose
    private double maxDurationH;

    public List<Object> getOtherVehiclesPermitted() {
        return otherVehiclesPermitted;
    }

    public void setOtherVehiclesPermitted(List<Object> otherVehiclesPermitted) {
        this.otherVehiclesPermitted = otherVehiclesPermitted;
    }

    public List<String> getPermitted() {
        return permitted;
    }

    public void setPermitted(List<String> permitted) {
        this.permitted = permitted;
    }

    public List<Price> getPrice() {
        return price;
    }

    public void setPrice(List<Price> price) {
        this.price = price;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public List<Time> getTimes() {
        return times;
    }

    public void setTimes(List<Time> times) {
        this.times = times;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getMaxDurationH() {
        return maxDurationH;
    }

    public void setMaxDurationH(double maxDurationH) {
        this.maxDurationH = maxDurationH;
    }

}
