
package beam.utils.coordmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Price {

    /**
     * The number of hours you must have parked here when this price stops
     * applying. If not set or non-positive, the price applies for any
     * duration.
     * 
     * @author abid
     */
    @SerializedName("end_duration_h")
    @Expose
    private Double endDurationH;
    /**
     * The increment of number of hours a person MUST pay for from this bucket if they wish to
     * utilize any duration in this bucket. For instance, if `minimum_hours_paid` is 1 in a 5
     * hour bucket, a parker must pay a whole hour's price for any fraction of an hour used.
     * 
     * 
     */
    @SerializedName("minimum_hours_paid")
    @Expose
    private Double minimumHoursPaid;
    /**
     * The price, per hour, to park here. If 0, parking is free.
     * 
     * (Required)
     * 
     */
    @SerializedName("price_per_hour")
    @Expose
    private PricePerHour pricePerHour;
    /**
     * The number of hours you must have parked here before this price starts
     * to apply.
     * 
     * 
     */
    @SerializedName("start_duration_h")
    @Expose
    private Double startDurationH;

    /**
     * The number of hours you must have parked here when this price stops
     * applying. If not set or non-positive, the price applies for any
     * duration.
     * 
     * 
     */
    public Double getEndDurationH() {
        return endDurationH;
    }

    /**
     * The number of hours you must have parked here when this price stops
     * applying. If not set or non-positive, the price applies for any
     * duration.
     * 
     * 
     */
    public void setEndDurationH(Double endDurationH) {
        this.endDurationH = endDurationH;
    }

    /**
     * The increment of number of hours a person MUST pay for from this bucket if they wish to
     * utilize any duration in this bucket. For instance, if `minimum_hours_paid` is 1 in a 5
     * hour bucket, a parker must pay a whole hour's price for any fraction of an hour used.
     * 
     * 
     */
    public Double getMinimumHoursPaid() {
        return minimumHoursPaid;
    }

    /**
     * The increment of number of hours a person MUST pay for from this bucket if they wish to
     * utilize any duration in this bucket. For instance, if `minimum_hours_paid` is 1 in a 5
     * hour bucket, a parker must pay a whole hour's price for any fraction of an hour used.
     * 
     * 
     */
    public void setMinimumHoursPaid(Double minimumHoursPaid) {
        this.minimumHoursPaid = minimumHoursPaid;
    }

    /**
     * The price, per hour, to park here. If 0, parking is free.
     * 
     * (Required)
     * 
     */
    public PricePerHour getPricePerHour() {
        return pricePerHour;
    }

    /**
     * The price, per hour, to park here. If 0, parking is free.
     * 
     * (Required)
     * 
     */
    public void setPricePerHour(PricePerHour pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    /**
     * The number of hours you must have parked here before this price starts
     * to apply.
     * 
     * 
     */
    public Double getStartDurationH() {
        return startDurationH;
    }

    /**
     * The number of hours you must have parked here before this price starts
     * to apply.
     * 
     * 
     */
    public void setStartDurationH(Double startDurationH) {
        this.startDurationH = startDurationH;
    }

}
