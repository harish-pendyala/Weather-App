package com.example.harish.group14_hw08;

import java.util.ArrayList;

/**
 * Created by haris on 4/8/2017.
 */
/*
*
* Homework 08
* Day.java
* Group 14 : Harish Pendyala
*
* */

public class Day {
    String citykey, cityname, country, headline, url;
    ArrayList<DayWeather> dw;

    public Day(String citykey, String cityname, String country, String headline, String url, ArrayList<DayWeather> dw) {
        this.citykey = citykey;
        this.cityname = cityname;
        this.country = country;
        this.headline = headline;
        this.url = url;
        this.dw = dw;
    }

    public String getCitykey() {
        return citykey;
    }

    public void setCitykey(String citykey) {
        this.citykey = citykey;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<DayWeather> getDw() {
        return dw;
    }

    public void setDw(ArrayList<DayWeather> dw) {
        this.dw = dw;
    }

    @Override
    public String toString() {
        return "Day{" +
                "citykey='" + citykey + '\'' +
                ", cityname='" + cityname + '\'' +
                ", country='" + country + '\'' +
                ", headline='" + headline + '\'' +
                ", url='" + url + '\'' +
                ", dw=" + dw.toString() +
                '}';
    }
}
