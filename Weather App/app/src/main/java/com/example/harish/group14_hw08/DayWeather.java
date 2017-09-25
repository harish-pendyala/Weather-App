package com.example.harish.group14_hw08;

/**
 * Created by haris on 4/8/2017.
 */
/*
*
* Homework 08
* DayWeather.java
* Group 14 : Harish Pendyala
*
* */
public class DayWeather {
    String date, max, min, dayicon, daytext, nighticon, nighttext, url;

    public DayWeather(String date, String max, String min, String dayicon, String daytext, String nighticon, String nighttext, String url) {
        this.date = date;
        this.max = max;
        this.min = min;
        this.dayicon = dayicon;
        this.daytext = daytext;
        this.nighticon = nighticon;
        this.nighttext = nighttext;
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getDayicon() {
        return dayicon;
    }

    public void setDayicon(String dayicon) {
        this.dayicon = dayicon;
    }

    public String getDaytext() {
        return daytext;
    }

    public void setDaytext(String daytext) {
        this.daytext = daytext;
    }

    public String getNighticon() {
        return nighticon;
    }

    public void setNighticon(String nighticon) {
        this.nighticon = nighticon;
    }

    public String getNighttext() {
        return nighttext;
    }

    public void setNighttext(String nighttext) {
        this.nighttext = nighttext;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DayWeather{" +
                "date='" + date + '\'' +
                ", max='" + max + '\'' +
                ", min='" + min + '\'' +
                ", dayicon='" + dayicon + '\'' +
                ", daytext='" + daytext + '\'' +
                ", nighticon='" + nighticon + '\'' +
                ", nighttext='" + nighttext + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
