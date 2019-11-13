package kr.co.lbstech.wish.model;

import java.util.Calendar;

public class Schedule {

    private Calendar time;
    private String task;
    private boolean alarmEnable;

    public Schedule(Calendar time, String task, boolean alarmEnable){
        this.time = time;
        this.task = task;
        this.alarmEnable = alarmEnable;
    }

    public String getTime() {
        int hour = time.get(Calendar.HOUR_OF_DAY);
        int min = time.get(Calendar.MINUTE);
        return  (hour < 10 ? "0"+hour : hour+"") + ":" + (min < 10 ? "0" + min : min+"");
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isAlarmEnable() {
        return alarmEnable;
    }

    public void setAlarmEnable(boolean alarmEnable) {
        this.alarmEnable = alarmEnable;
    }
}
