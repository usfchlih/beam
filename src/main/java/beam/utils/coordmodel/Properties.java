
package beam.utils.coordmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties {

    /**
     * Identifying information for a particular segment of curb.
     * @author abid
     */
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;
    @SerializedName("rules")
    @Expose
    private List<Rule> rules = null;
    /**
     * Rules that only apply temporarily (usually due to construction or events). Temporary rules
     * **always** take priority over regular rules. Note that temporary rules in the future may
     * be subject to change.
     * 
     * 
     */
    @SerializedName("temporary_rules")
    @Expose
    private List<TemporaryRule> temporaryRules = null;

    /**
     * Identifying information for a particular segment of curb.
     * 
     */
    public Metadata getMetadata() {
        return metadata;
    }

    /**
     * Identifying information for a particular segment of curb.
     * 
     */
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    /**
     * Rules that only apply temporarily (usually due to construction or events). Temporary rules
     * **always** take priority over regular rules. Note that temporary rules in the future may
     * be subject to change.
     * 
     * 
     */
    public List<TemporaryRule> getTemporaryRules() {
        return temporaryRules;
    }

    /**
     * Rules that only apply temporarily (usually due to construction or events). Temporary rules
     * **always** take priority over regular rules. Note that temporary rules in the future may
     * be subject to change.
     * 
     * 
     */
    public void setTemporaryRules(List<TemporaryRule> temporaryRules) {
        this.temporaryRules = temporaryRules;
    }

}
