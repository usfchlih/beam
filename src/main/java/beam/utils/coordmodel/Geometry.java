
package beam.utils.coordmodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * A GeoJSON LineString geometry.
 *
 * @author abid
 */
public class Geometry {

    @SerializedName("coordinates")
    @Expose
    private List<List<Double>> coordinates = null;
    @SerializedName("type")
    @Expose
    private Geometry.Type type;

    public List<List<Double>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<Double>> coordinates) {
        this.coordinates = coordinates;
    }

    public Geometry.Type getType() {
        return type;
    }

    public void setType(Geometry.Type type) {
        this.type = type;
    }

    public enum Type {

        @SerializedName("LineString")
        LINE_STRING("LineString");
        private final String value;
        private static final Map<String, Geometry.Type> CONSTANTS = new HashMap<>();

        static {
            for (Geometry.Type c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Geometry.Type fromValue(String value) {
            Geometry.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
