package com.evantimms.todolist.datamodel;

import java.time.LocalDate;

public class TodoItem {

    private String shortDes;
    private String extendedDes;
    private LocalDate deadline;

    public TodoItem(String shortDes, String extendedDes, LocalDate deadline) {
        this.shortDes = shortDes;
        this.extendedDes = extendedDes;
        this.deadline = deadline;
    }

    public String getShortDes() {
        return shortDes;
    }

    public void setShortDes(String shortDes) {
        this.shortDes = shortDes;
    }

    public String getExtendedDes() {
        return extendedDes;
    }

    public void setExtendedDes(String extendedDes) {
        this.extendedDes = extendedDes;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
