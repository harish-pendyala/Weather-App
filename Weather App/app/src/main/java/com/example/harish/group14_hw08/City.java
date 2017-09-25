package com.example.harish.group14_hw08;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by haris on 04/06/17.
 */
/*
*
* Homework 08
* City.java
* Group 14 : Harish Pendyala
*
* */
public class City {

    private String citykey, cityname, country, temperature, date, favorite;

    public City(String citykey, String cityname, String country, String temperature, String date, String favorite) {
        this.citykey = citykey;
        this.cityname = cityname;
        this.country = country;
        this.temperature = temperature;

        this.date = date;
        this.favorite = favorite;
    }
    public City() {

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

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "City{" +
                "citykey='" + citykey + '\'' +
                ", cityname='" + cityname + '\'' +
                ", country='" + country + '\'' +
                ", temperature='" + temperature + '\'' +
                ", date='" + date + '\'' +
                ", favorite='" + favorite + '\'' +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("citykey", citykey);
        result.put("cityname", cityname);
        result.put("country", country);
        result.put("temperature", temperature);
        result.put("date", date);
        result.put("favorite", favorite);

        return result;
    }

}

