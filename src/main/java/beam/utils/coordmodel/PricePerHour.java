package beam.utils.coordmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PricePerHour {

    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("currency")
    @Expose
    private String currency;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
