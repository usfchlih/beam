
package beam.utils.coordmodel;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

/**
 * @author abid
 */
public enum Permitted {

    @SerializedName("park")
    PARK("park"),
    @SerializedName("load_goods")
    LOAD_GOODS("load_goods"),
    @SerializedName("load_passengers")
    LOAD_PASSENGERS("load_passengers"),
    @SerializedName("none")
    NONE("none");
    private final String value;
    private static final Map<String, Permitted> CONSTANTS = new HashMap<>();

    static {
        for (Permitted c : values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    private Permitted(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static Permitted fromValue(String value) {
        Permitted constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
