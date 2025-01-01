package com.pratik.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Task
{
    public Task()
    {

    }
    private int taskId;
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date targetDate;

    public enum Status {
        TODO,
        WIP,
        DONE
    }

    private Status status;

    public int getTaskId()
    {
        return taskId;
    }

    public void setTaskId(int taskId)
    {
        this.taskId = taskId;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getTargetDate()
    {
        return targetDate;
    }

    public void setTargetDate(Date targetDate)
    {
        this.targetDate = targetDate;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

}
