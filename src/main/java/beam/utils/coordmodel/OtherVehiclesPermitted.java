
package beam.utils.coordmodel;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;

/**
 * @author abid
 */
public enum OtherVehiclesPermitted {

    @SerializedName("park")
    PARK("park"),
    @SerializedName("load_goods")
    LOAD_GOODS("load_goods"),
    @SerializedName("load_passengers")
    LOAD_PASSENGERS("load_passengers"),
    @SerializedName("none")
    NONE("none");
    private final String value;
    private static final Map<String, OtherVehiclesPermitted> CONSTANTS = new HashMap<>();

    static {
        for (OtherVehiclesPermitted c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private OtherVehiclesPermitted(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static OtherVehiclesPermitted fromValue(String value) {
        OtherVehiclesPermitted constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
