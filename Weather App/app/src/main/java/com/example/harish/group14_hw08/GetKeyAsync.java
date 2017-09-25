package com.example.harish.group14_hw08;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by haris on 4/7/2017.
 */
/*
*
* Homework 08
* GetKeyAsync.java
* Group 14 : Harish Pendyala
*
* */
public class GetKeyAsync extends AsyncTask<String,Void,City > {

    getKey activity;

    public GetKeyAsync(getKey activity) {
        this.activity = activity;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected City doInBackground(String... strings) {
        Log.d("demo", "City code URL"+strings[0]);

        City city = null;
        try{
            URL url=new URL(strings[0].replace(" ","%20"));
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statuscode=con.getResponseCode();
            Log.d("demo", "City Status: "+statuscode);
            if(statuscode==HttpURLConnection.HTTP_OK)
            {
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String line;
                while((line = br.readLine()) != null)
                    sb.append(line+"\n");

                Log.d("demo", "Inside status code");
                Log.d("demo", "Stream"+sb.toString());
                JSONArray root = new JSONArray(sb.toString());
                Log.d("demo", "Stream"+root.toString());
                city = parseKey(root);
            }

        }
        catch (Exception e){

        }
//        Log.d("demo", "City code"+city.toString());
        return city;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    City parseKey(JSONArray root) throws JSONException {
        Log.d("demo", "City code parsing");

        Log.d("demo", "ROOT "+root);
        JSONObject newObj = root.getJSONObject(0);
        String key=newObj.getString("Key").toString().trim();
        String cityname=newObj.getString("LocalizedName").toString().trim();
        JSONObject countryObj=newObj.getJSONObject("Country");
        String id=countryObj.getString("ID").toString().trim();

//        Log.d("demo", "Key "+key);
//        Log.d("demo", "City Name "+cityname);
//        Log.d("demo", "Country "+id);
        City city=new City(key, cityname, id, null,null,"FALSE");
        Log.d("demo", "City "+city.toString());
        return city;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(City  city) {
        activity.setKey(city);
    }

    public interface getKey{
        public void setKey(City city);
    }
}

