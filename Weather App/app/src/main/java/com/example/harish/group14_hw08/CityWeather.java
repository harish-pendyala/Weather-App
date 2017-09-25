package com.example.harish.group14_hw08;

/**
 * Created by haris on 4/7/2017.
 */
/*
*
* Homework 08
* CityWeather.java
* Group 14 : Harish Pendyala
*
* */
public class CityWeather {
    String city, country, description, date, icon, temparature;

    public CityWeather() {
    }

    public CityWeather(String city, String country, String description, String date, String icon, String temparature) {

        this.city = city;
        this.country = country;
        this.description = description;
        this.date = date;
        this.icon = icon;
        this.temparature = temparature;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemparature() {
        return temparature;
    }

    public void setTemparature(String temparature) {
        this.temparature = temparature;
    }

    @Override
    public String toString() {
        return "CityWeather{" +
                "city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", icon='" + icon + '\'' +
                ", temparature='" + temparature + '\'' +
                '}';
    }
}
