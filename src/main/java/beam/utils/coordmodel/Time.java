
package beam.utils.coordmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * An interval of time that repeats every week on certain days. For instance, a TimeAndDay
 * could represent 7AM-9PM on weekdays as
 * `{"days": [1, 2, 3, 4, 5], "time_of_day_start": "07:00", "time_end": "19:00"}`
 * (day 0 is Sunday).
 *
 * @author abid
 */
public class Time {

    /**
     * The day of year (as a `mm-dd` string where 1 < mm < 12 and 1 < dd < 31)
     * this condition stops being valid.
     * 
     * 
     */
    @SerializedName("day_of_year_end")
    @Expose
    private String dayOfYearEnd;
    /**
     * The day of year (as a `mm-dd` string where 1 < mm < 12 and 1 < dd < 31)
     * this condition starts to apply.
     * 
     * 
     */
    @SerializedName("day_of_year_start")
    @Expose
    private String dayOfYearStart;
    /**
     * The days of week this condition applies (0=Sunday)
     * 
     */
    @SerializedName("days")
    @Expose
    private List<Integer> days = null;
    /**
     * The time of day (as a 24-hour `HH:MM` string) this TimeAndDay ends.
     * If this `TimeAndDay` ends at midnight, `time_of_day_end` will be `24:00`.
     * 
     * 
     */
    @SerializedName("time_of_day_end")
    @Expose
    private String timeOfDayEnd;
    /**
     * The time of day (as a 24-hour `HH:MM` string) this TimeAndDay begins.
     * If this `TimeAndDay` starts at midnight, `time_of_day_start` will be `00:00`.
     * 
     * 
     */
    @SerializedName("time_of_day_start")
    @Expose
    private String timeOfDayStart;

    /**
     * The day of year (as a `mm-dd` string where 1 < mm < 12 and 1 < dd < 31)
     * this condition stops being valid.
     * 
     * 
     */
    public String getDayOfYearEnd() {
        return dayOfYearEnd;
    }

    /**
     * The day of year (as a `mm-dd` string where 1 < mm < 12 and 1 < dd < 31)
     * this condition stops being valid.
     * 
     * 
     */
    public void setDayOfYearEnd(String dayOfYearEnd) {
        this.dayOfYearEnd = dayOfYearEnd;
    }

    /**
     * The day of year (as a `mm-dd` string where 1 < mm < 12 and 1 < dd < 31)
     * this condition starts to apply.
     * 
     * 
     */
    public String getDayOfYearStart() {
        return dayOfYearStart;
    }

    /**
     * The day of year (as a `mm-dd` string where 1 < mm < 12 and 1 < dd < 31)
     * this condition starts to apply.
     * 
     * 
     */
    public void setDayOfYearStart(String dayOfYearStart) {
        this.dayOfYearStart = dayOfYearStart;
    }

    /**
     * The days of week this condition applies (0=Sunday)
     * 
     */
    public List<Integer> getDays() {
        return days;
    }

    /**
     * The days of week this condition applies (0=Sunday)
     * 
     */
    public void setDays(List<Integer> days) {
        this.days = days;
    }

    /**
     * The time of day (as a 24-hour `HH:MM` string) this TimeAndDay ends.
     * If this `TimeAndDay` ends at midnight, `time_of_day_end` will be `24:00`.
     * 
     * 
     */
    public String getTimeOfDayEnd() {
        return timeOfDayEnd;
    }

    /**
     * The time of day (as a 24-hour `HH:MM` string) this TimeAndDay ends.
     * If this `TimeAndDay` ends at midnight, `time_of_day_end` will be `24:00`.
     * 
     * 
     */
    public void setTimeOfDayEnd(String timeOfDayEnd) {
        this.timeOfDayEnd = timeOfDayEnd;
    }

    /**
     * The time of day (as a 24-hour `HH:MM` string) this TimeAndDay begins.
     * If this `TimeAndDay` starts at midnight, `time_of_day_start` will be `00:00`.
     * 
     * 
     */
    public String getTimeOfDayStart() {
        return timeOfDayStart;
    }

    /**
     * The time of day (as a 24-hour `HH:MM` string) this TimeAndDay begins.
     * If this `TimeAndDay` starts at midnight, `time_of_day_start` will be `00:00`.
     * 
     * 
     */
    public void setTimeOfDayStart(String timeOfDayStart) {
        this.timeOfDayStart = timeOfDayStart;
    }

}
