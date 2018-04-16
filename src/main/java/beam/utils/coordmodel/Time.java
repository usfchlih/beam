package beam.utils.coordmodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Time {

    @SerializedName("days")
    @Expose
    private List<Integer> days = null;
    @SerializedName("time_of_day_end")
    @Expose
    private String timeOfDayEnd;
    @SerializedName("time_of_day_start")
    @Expose
    private String timeOfDayStart;

    public List<Integer> getDays() {
        return days;
    }

    public void setDays(List<Integer> days) {
        this.days = days;
    }

    public String getTimeOfDayEnd() {
        return timeOfDayEnd;
    }

    public void setTimeOfDayEnd(String timeOfDayEnd) {
        this.timeOfDayEnd = timeOfDayEnd;
    }

    public String getTimeOfDayStart() {
        return timeOfDayStart;
    }

    public void setTimeOfDayStart(String timeOfDayStart) {
        this.timeOfDayStart = timeOfDayStart;
    }

}
