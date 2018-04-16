package beam.utils.coordmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Price {

    @SerializedName("price_per_hour")
    @Expose
    private PricePerHour pricePerHour;

    public PricePerHour getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(PricePerHour pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

}
