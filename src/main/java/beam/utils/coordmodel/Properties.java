package beam.utils.coordmodel;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Properties {

    @SerializedName("metadata")
    @Expose
    private Metadata metadata;
    @SerializedName("rules")
    @Expose
    private List<Rule> rules = null;
    @SerializedName("temporary_rules")
    @Expose
    private Object temporaryRules;

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public Object getTemporaryRules() {
        return temporaryRules;
    }

    public void setTemporaryRules(Object temporaryRules) {
        this.temporaryRules = temporaryRules;
    }

}
