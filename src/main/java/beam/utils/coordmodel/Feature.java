
package beam.utils.coordmodel;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A GeoJSON feature representing a segment's rules.
 *
 * @author abid
 */
public class Feature {

    /**
     * A GeoJSON LineString geometry.
     */
    @SerializedName("geometry")
    @Expose
    private Geometry geometry;
    @SerializedName("properties")
    @Expose
    private Properties properties;
    @SerializedName("type")
    @Expose
    private Feature.Type type;

    /**
     * A GeoJSON LineString geometry.
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * A GeoJSON LineString geometry.
     *
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Feature.Type getType() {
        return type;
    }

    public void setType(Feature.Type type) {
        this.type = type;
    }

    public enum Type {

        @SerializedName("Feature")
        FEATURE("Feature");
        private final String value;
        private static final Map<String, Feature.Type> CONSTANTS = new HashMap<>();

        static {
            for (Feature.Type c : values()) {
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

        public static Feature.Type fromValue(String value) {
            Feature.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
