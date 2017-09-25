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
import java.util.ArrayList;

/**
 * Created by haris on 4/8/2017.
 */
/*
*
* Homework 08
* GetDayWeatherAsync.java
* Group 14 : Harish Pendyala
*
* */
public class GetDayWeatherAsync extends AsyncTask<String,Void,Day > {

    loadWeather activity;
    String citykey, cityname, country;

    public GetDayWeatherAsync(loadWeather activity) {
        this.activity = activity;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Day doInBackground(String... strings) {
        Log.d("demo", "City code URL"+strings[0]);
        citykey=strings[1];
        cityname=strings[2];
        country=strings[3];
        Day day = null;
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

                Log.d("demo", "Inside status code");
                Log.d("demo", "Stream"+sb.toString());
                JSONObject root = new JSONObject(sb.toString());
                Log.d("demo", "Stream"+root.toString());
                day = parseWeather(root);
            }

        }
        catch (Exception e){

        }
//        Log.d("demo", "City code"+city.toString());
        return day;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    Day parseWeather(JSONObject root) throws JSONException {
        ArrayList<DayWeather> al = new ArrayList<>();
        Log.d("demo", "City code parsing");

        Log.d("demo", "ROOT "+root);
        JSONObject newObj = root.getJSONObject("Headline");
        String headline=newObj.getString("Text").toString().trim();
        String url=newObj.getString("MobileLink").toString().trim();
        Log.d("demo", "Headline "+headline);
        JSONArray ja = root.getJSONArray("DailyForecasts");
        Log.d("demo", "JA Array1 "+ja);
        for (int i=0; i<ja.length();i++){
            DayWeather dw;
            Log.d("demo", "JA Array "+ja.get(i));
            JSONObject jo = (JSONObject) ja.get(i);
            String date =  jo.getString("Date");
            JSONObject jo1 = jo.getJSONObject("Temperature");
            JSONObject jo12 = jo1.getJSONObject("Minimum");
            Double min = Double.valueOf(jo12.getString("Value"));
            min = min-32;
            min = min/1.8;
            String minimum = String.format("%.2f", min);
            JSONObject jo13 = jo1.getJSONObject("Maximum");
            Double max = Double.valueOf(jo13.getString("Value"));
            max = max-32;
            max = max/1.8;
            String maximum = String.format("%.2f", max);
            JSONObject jo2 = jo.getJSONObject("Day");
            String dayicon = jo2.getString("Icon");
            String daytext = jo2.getString("IconPhrase");
            JSONObject jo3 = jo.getJSONObject("Night");
            String nighticon = jo3.getString("Icon");
            String nighttext = jo3.getString("IconPhrase");
            String dayUrl = ((JSONObject) ja.get(i)).getString("MobileLink");
            dw = new DayWeather(date,maximum,minimum,dayicon,daytext,nighticon,nighttext,dayUrl);
            al.add(dw);

        }
        Day day = new Day(citykey,cityname,country,headline,url,al);

        Log.d("demo", "Headline "+headline);
//        Log.d("demo", "Desc "+desc);
//        Log.d("demo", "ICOn "+icon);
//        Log.d("demo", "Temp "+temp);
//        CityWeather cw=new CityWeather(city,country,desc,date,icon,temp);
//        Log.d("demo", "City "+cw.toString());
        return day;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Day day) {
        activity.setWeather(day);
    }

    public interface loadWeather{
        public void setWeather(Day day);
    }
}
