
package beam.utils.coordmodel;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * The price, per hour, to park here. If 0, parking is free.
 *
 * @author abid
 */
public class PricePerHour {

    /**
     * The total amount in the _smallest denomination_ of the specified currency.
     */
    @SerializedName("amount")
    @Expose
    private Double amount;
    /**
     * The ISO4217 currency code.
     */
    @SerializedName("currency")
    @Expose
    private PricePerHour.Currency currency;

    /**
     * The total amount in the _smallest denomination_ of the specified currency.
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * The total amount in the _smallest denomination_ of the specified currency.
     *
     *
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * The ISO4217 currency code.
     *
     */
    public PricePerHour.Currency getCurrency() {
        return currency;
    }

    /**
     * The ISO4217 currency code.
     *
     */
    public void setCurrency(PricePerHour.Currency currency) {
        this.currency = currency;
    }

    public enum Currency {

        @SerializedName("USD")
        USD("USD");
        private final String value;
        private static final Map<String, PricePerHour.Currency> CONSTANTS = new HashMap<>();

        static {
            for (PricePerHour.Currency c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Currency(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static PricePerHour.Currency fromValue(String value) {
            PricePerHour.Currency constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
