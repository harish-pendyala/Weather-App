package com.example.harish.group14_hw08;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by haris on 4/7/2017.
 */
/*
*
* Homework 08
* GetWeatherAsync.java
* Group 14 : Harish Pendyala
*
* */

public class GetWeatherAsync extends AsyncTask<String,Void,CityWeather > {

    loadWeather activity;
    String city = null, country=null;

    public GetWeatherAsync(loadWeather activity) {
        this.activity = activity;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected CityWeather doInBackground(String... strings) {
        Log.d("demo", "City code URL"+strings[0]);
        CityWeather cw = null;
        city = strings[1];
        country = strings[2];
        try{
            URL url=new URL(strings[0]);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statuscode=con.getResponseCode();
            if(statuscode==HttpURLConnection.HTTP_OK)
            {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while((line = br.readLine()) != null)
                    sb.append(line+"\n");
//
//                Log.d("demo", "Inside status code");
//                Log.d("demo", "Stream"+sb.toString());
                JSONArray root = new JSONArray(sb.toString());
                //Log.d("demo", "Stream"+root.toString());
                cw = parseWeather(root);
            }

        }
        catch (Exception e){

        }
//        Log.d("demo", "City code"+city.toString());
        return cw;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    CityWeather parseWeather(JSONArray root) throws JSONException {
//        Log.d("demo", "City code parsing");
//
//        Log.d("demo", "ROOT "+root);
        JSONObject newObj = root.getJSONObject(0);
        String date=newObj.getString("LocalObservationDateTime").toString().trim();
        String desc=newObj.getString("WeatherText").toString().trim();
        String icon=newObj.getString("WeatherIcon").toString().trim();
        JSONObject tempObj=newObj.getJSONObject("Temperature").getJSONObject("Metric");
        String temp=tempObj.getString("Value").toString().trim();


        CityWeather cw=new CityWeather(city,country,desc,date,icon,temp);
       // Log.d("demo", "City "+cw.toString());
        return cw;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(CityWeather  city) {
        activity.setWeather(city);
    }

    public interface loadWeather{
        public void setWeather(CityWeather city);
    }
}
