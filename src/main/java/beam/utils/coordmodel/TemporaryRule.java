
package beam.utils.coordmodel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * A temporary rule that applies along a particular segment of curb. Rather than recurring
 * on a weekly basis, temporary rules are scheduled once. These **always** take precedence
 * over regular rules.
 * @author abid
 */
public class TemporaryRule {

    /**
     * When this rule stops applying, in ISO 8601 format.
     */
    @SerializedName("end")
    @Expose
    private Date end;
    /**
     * The longest a vehicle may remain at this curb while engaged in a permitted use, in
     * hours.
     * <p>
     * If a new rule starts applying before `max_duration_h` has elapsed, the new rule's
     * max_duration_h takes effect, but counting from when the vehicle first arrived.
     * For instance, If a curb had 2-hour parking until 5pm but 3-hour parking thereafter, and
     * a vehicle arrived at 4pm, they could continue parking until 7pm.
     */
    @SerializedName("max_duration_h")
    @Expose
    private Double maxDurationH;
    /**
     * The uses that are permitted for vehicles not of this segment's primary vehicle type.
     */
    @SerializedName("other_vehicles_permitted")
    @Expose
    private List<OtherVehiclesPermitted> otherVehiclesPermitted = null;
    /**
     * All the uses that are permitted, including the primary use.
     */
    @SerializedName("permitted")
    @Expose
    private List<Permitted> permitted = null;
    /**
     * The price a vehicle must pay while on this segment. In general, this price applies regardless
     * of use or vehicle type.
     * <p>
     * If a new rule starts applying, that rule's prices take effect, but counting from when the
     * vehicle first arrived. For instance, if a curb had:
     * * Parking at ${1} an hour until 8am;
     * * Parking at ${4} for the first hour and ${5} for the second hour thereafter,
     * A vehicle arriving at 7am would pay ${1} for the first hour and ${5} for the second.
     */
    @SerializedName("price")
    @Expose
    private List<Price> price = null;
    @SerializedName("primary")
    @Expose
    private TemporaryRule.Primary primary;
    /**
     * When this rule starts to apply, in ISO 8601 format.
     */
    @SerializedName("start")
    @Expose
    private Date start;
    @SerializedName("vehicle_type")
    @Expose
    private TemporaryRule.VehicleType vehicleType = TemporaryRule.VehicleType.fromValue("all");

    /**
     * When this rule stops applying, in ISO 8601 format.
     */
    public Date getEnd() {
        return end;
    }

    /**
     * When this rule stops applying, in ISO 8601 format.
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * The longest a vehicle may remain at this curb while engaged in a permitted use, in
     * hours.
     * <p>
     * If a new rule starts applying before `max_duration_h` has elapsed, the new rule's
     * max_duration_h takes effect, but counting from when the vehicle first arrived.
     * For instance, If a curb had 2-hour parking until 5pm but 3-hour parking thereafter, and
     * a vehicle arrived at 4pm, they could continue parking until 7pm.
     */
    public Double getMaxDurationH() {
        return maxDurationH;
    }

    /**
     * The longest a vehicle may remain at this curb while engaged in a permitted use, in
     * hours.
     * <p>
     * If a new rule starts applying before `max_duration_h` has elapsed, the new rule's
     * max_duration_h takes effect, but counting from when the vehicle first arrived.
     * For instance, If a curb had 2-hour parking until 5pm but 3-hour parking thereafter, and
     * a vehicle arrived at 4pm, they could continue parking until 7pm.
     */
    public void setMaxDurationH(Double maxDurationH) {
        this.maxDurationH = maxDurationH;
    }

    /**
     * The uses that are permitted for vehicles not of this segment's primary vehicle type.
     */
    public List<OtherVehiclesPermitted> getOtherVehiclesPermitted() {
        return otherVehiclesPermitted;
    }

    /**
     * The uses that are permitted for vehicles not of this segment's primary vehicle type.
     */
    public void setOtherVehiclesPermitted(List<OtherVehiclesPermitted> otherVehiclesPermitted) {
        this.otherVehiclesPermitted = otherVehiclesPermitted;
    }

    /**
     * All the uses that are permitted, including the primary use.
     */
    public List<Permitted> getPermitted() {
        return permitted;
    }

    /**
     * All the uses that are permitted, including the primary use.
     */
    public void setPermitted(List<Permitted> permitted) {
        this.permitted = permitted;
    }

    /**
     * The price a vehicle must pay while on this segment. In general, this price applies regardless
     * of use or vehicle type.
     * <p>
     * If a new rule starts applying, that rule's prices take effect, but counting from when the
     * vehicle first arrived. For instance, if a curb had:
     * * Parking at ${1} an hour until 8am;
     * * Parking at ${4} for the first hour and ${5} for the second hour thereafter,
     * A vehicle arriving at 7am would pay ${1} for the first hour and ${5} for the second.
     */
    public List<Price> getPrice() {
        return price;
    }

    /**
     * The price a vehicle must pay while on this segment. In general, this price applies regardless
     * of use or vehicle type.
     * <p>
     * If a new rule starts applying, that rule's prices take effect, but counting from when the
     * vehicle first arrived. For instance, if a curb had:
     * * Parking at ${1} an hour until 8am;
     * * Parking at ${4} for the first hour and ${5} for the second hour thereafter,
     * A vehicle arriving at 7am would pay ${1} for the first hour and ${5} for the second.
     */
    public void setPrice(List<Price> price) {
        this.price = price;
    }

    public TemporaryRule.Primary getPrimary() {
        return primary;
    }

    public void setPrimary(TemporaryRule.Primary primary) {
        this.primary = primary;
    }

    /**
     * When this rule starts to apply, in ISO 8601 format.
     */
    public Date getStart() {
        return start;
    }

    /**
     * When this rule starts to apply, in ISO 8601 format.
     */
    public void setStart(Date start) {
        this.start = start;
    }

    public TemporaryRule.VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(TemporaryRule.VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public enum Primary {

        @SerializedName("park")
        PARK("park"),
        @SerializedName("load_goods")
        LOAD_GOODS("load_goods"),
        @SerializedName("load_passengers")
        LOAD_PASSENGERS("load_passengers"),
        @SerializedName("none")
        NONE("none");
        private final String value;
        private static final Map<String, TemporaryRule.Primary> CONSTANTS = new HashMap<>();

        static {
            for (TemporaryRule.Primary c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Primary(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static TemporaryRule.Primary fromValue(String value) {
            TemporaryRule.Primary constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum VehicleType {

        @SerializedName("all")
        ALL("all"),
        @SerializedName("taxi")
        TAXI("taxi"),
        @SerializedName("commercial")
        COMMERCIAL("commercial"),
        @SerializedName("truck")
        TRUCK("truck"),
        @SerializedName("motorcycle")
        MOTORCYCLE("motorcycle");
        private final String value;
        private static final Map<String, TemporaryRule.VehicleType> CONSTANTS = new HashMap<>();

        static {
            for (TemporaryRule.VehicleType c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private VehicleType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static TemporaryRule.VehicleType fromValue(String value) {
            TemporaryRule.VehicleType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
