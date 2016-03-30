package com.srmn.xwork.enity;



import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kiler on 2016/1/12.
 */
public class DayTimeRange implements Serializable {

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private Date startTime = new Date(2000, 1, 1, 0, 0, 0);
    private Date endTime = new Date(2000, 1, 1, 23, 59, 59);

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


    @Override
    public String toString() {

        return timeFormat.format(this.getStartTime()) + "-" + timeFormat.format(this.getEndTime());
    }
}
