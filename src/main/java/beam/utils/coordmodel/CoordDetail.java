
package beam.utils.coordmodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * A collection of segment rules features.
 *
 * @author abid
 */
public class CoordDetail {

    @SerializedName("features")
    @Expose
    private List<Feature> features = null;
    @SerializedName("type")
    @Expose
    private CoordDetail.Type type;

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public CoordDetail.Type getType() {
        return type;
    }

    public void setType(CoordDetail.Type type) {
        this.type = type;
    }

    public enum Type {

        @SerializedName("FeatureCollection")
        FEATURE_COLLECTION("FeatureCollection");
        private final String value;
        private static final Map<String, CoordDetail.Type> CONSTANTS = new HashMap<>();

        static {
            for (CoordDetail.Type c: values()) {
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

        public static CoordDetail.Type fromValue(String value) {
            CoordDetail.Type constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
