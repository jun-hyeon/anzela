package com.example.angela;

public class ListData {

    String title;
    String hcount;
    String date;

    public ListData(String title, String hcount, String date) {
        this.title = title;
        this.hcount = hcount;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHcount() {
        return hcount;
    }

    public void setHcount(String hcount) {
        this.hcount = hcount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
