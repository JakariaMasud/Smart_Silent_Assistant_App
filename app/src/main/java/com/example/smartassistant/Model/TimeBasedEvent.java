package com.example.smartassistant.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "time_based_table")
public class TimeBasedEvent {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String type;
    private int period;
    private long selectedTime;
    private  int notificationBefore;
    private String time;

    public TimeBasedEvent() {
    }
    @Ignore
    public TimeBasedEvent(final long id, final String title, final String type, final int period, final long selectedTime, final int notificationBefore, final String time) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.period = period;
        this.selectedTime = selectedTime;
        this.notificationBefore = notificationBefore;
        this.time = time;
    }
    @Ignore
    public TimeBasedEvent(final String title, final String type, final int period, final long selectedTime, final int notificationBefore, final String time) {
        this.title = title;
        this.type = type;
        this.period = period;
        this.selectedTime = selectedTime;
        this.notificationBefore = notificationBefore;
        this.time = time;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(final String time) {
        this.time = time;
    }

    public int getNotificationBefore() {
        return this.notificationBefore;
    }

    public void setNotificationBefore(final int notificationBefore) {
        this.notificationBefore = notificationBefore;
    }

    public long getId() {
        return this.id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public int getPeriod() {
        return this.period;
    }

    public void setPeriod(final int period) {
        this.period = period;
    }

    public long getSelectedTime() {
        return this.selectedTime;
    }

    public void setSelectedTime(final long selectedTime) {
        this.selectedTime = selectedTime;
    }
}

