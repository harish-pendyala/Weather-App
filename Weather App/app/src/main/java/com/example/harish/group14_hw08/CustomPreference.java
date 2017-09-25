package com.example.harish.group14_hw08;

/*
*
* Homework 08
* CustomPreference.java
* Group 14 : Harish Pendyala
*
* */

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by haris on 4/8/2017.
 */

public class CustomPreference extends DialogPreference implements GetKeyAsync.getKey {
    EditText editTextCity,editCountry;

    public CustomPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        setDialogLayoutResource(R.layout.curr_city);
        final SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        String city=sharedPreferences.getString(MainActivity.CURR_CITY,"");
        String country=sharedPreferences.getString(MainActivity.CURR_COUNTRY,"");
        if(!city.equals("")){
            setDialogTitle("Update City Details");
            setPositiveButtonText("Change");

        } else {
            setDialogTitle("Enter City Details");
            setPositiveButtonText("Set");

        }

    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        setDialogTitle("Enter Current City Details");
        final SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
        String city=sharedPreferences.getString(MainActivity.CURR_CITY,"");
        String country=sharedPreferences.getString(MainActivity.CURR_COUNTRY,"");
        //setDialogTitle("");
        editTextCity= (EditText) view.findViewById(R.id.cityname);
        editCountry= (EditText) view.findViewById(R.id.countryname);
        editTextCity.setTextColor(Color.BLACK);
        editCountry.setTextColor(Color.BLACK);
        if(!city.equals("")){
            setDialogTitle("Update City Details");
            editTextCity.setText(city);
            editCountry.setText(country);
            setPositiveButtonText("Change");

        } else {
            setDialogTitle("Enter City Details");
            editTextCity.setHint("Enter Your city");
            editCountry.setHint("Enter Your Country");
            editTextCity.setHintTextColor(Color.BLACK);
            editCountry.setHintTextColor(Color.BLACK);
            setPositiveButtonText("Set");

        }

    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult){
            String city=editTextCity.getText().toString().trim();
            String country=editCountry.getText().toString().trim();
            new GetKeyAsync(this).execute("http://dataservice.accuweather.com/locations/v1/US/search?apikey="+MainActivity.MY_API_KEY+"&q="+city);


        }


    }

    @Override
    public void setKey(City city) {
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String currCity=sharedPreferences.getString(MainActivity.CURR_CITY,"");
        if (city != null){
            if(currCity.equals("")){
                Toast.makeText(getContext(),"Current City Saved", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getContext(),"Current City Updated",Toast.LENGTH_LONG).show();
            }

            editor.putString(MainActivity.CURR_CITY,city.getCityname());
            editor.putString(MainActivity.CURR_COUNTRY,city.getCountry());
            editor.putString(MainActivity.CURR_KEY,city.getCitykey());
            editor.commit();

        }
        else {
            Toast.makeText(getContext(),"Invalid City, not saved",Toast.LENGTH_LONG).show();
        }

    }
}