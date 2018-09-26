package main.util;

public class ScheduleItem {
    private String task;
    private String fromTime;
    private String toTime;
    private String taskCompleted;

    public ScheduleItem() {
        this.task = "";
        this.fromTime = "";
        this.toTime = "";
        this.taskCompleted = "";
    }

    public ScheduleItem(String task, String fromTime, String toTime, String taskCompleted) {
        this.task = task;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.taskCompleted = taskCompleted;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public String getTaskCompleted() {
        return taskCompleted;
    }

    public void setTaskCompleted(String taskCompleted) {
        this.taskCompleted = taskCompleted;
    }
}
