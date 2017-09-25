/*
*
* Homework 08
* MainActivity.java
* Group 14 : Harish Pendyala,Hemanth
*
* */
package com.example.harish.group14_hw08;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GetKeyAsync.getKey, GetWeatherAsync.loadWeather{

    private RecyclerView mRecyclerView;
    final static String CITY_STRING = "CITY";
    final static String CITY_NOT_FOUND= "NOCITY";
    final static String COUNTRY_STRING = "COUNTRY";
    final static String CURR_CITY = "CURR_CITY";
    final static String CURR_COUNTRY = "CURR_COUNTRY";
    final static String MY_API_KEY = "qE8quuOSiRKIAHoCCQi9WGxiXJ6u3koG";
    final static String CURR_KEY = "CURR_KEY";
    City cityWeather = null;
    String tempUnits, currCity, currCityName,currCountry;
    ArrayList<City>  myDataset,savedCities;
    TextView messageText, headerText;
    LinearLayout setcity;
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference childref;
    ProgressBar pb;
    TextView title, description, temperature, date;
    ImageView icon;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String cFlag = "ABC";
    View nocity, cityexists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        childref = dref.child("Cities");
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        ActionBar ab = getSupportActionBar();
        //ab.setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.icon);
//        ab.setDisplayShowHomeEnabled(true);
//        ab.setIcon(R.mipmap.icon);

        myDataset = new ArrayList<>();
        savedCities = new ArrayList<>();

        editor.remove(CURR_CITY);
        editor.commit();
        editor.remove(CURR_KEY);
        editor.commit();
        editor.remove(CURR_COUNTRY);
        editor.commit();

        tempUnits = prefs.getString("PREF_UNIT","");
        currCity =  prefs.getString(CURR_KEY,"");
        currCityName =  prefs.getString(CURR_CITY,"");
        currCountry =  prefs.getString(CURR_COUNTRY,"");

        messageText = (TextView)findViewById(R.id.messageText);
        headerText = (TextView)findViewById(R.id.scHeader);
        setcity = (LinearLayout) findViewById(R.id.setcity);
        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        nocity = inflator.inflate(R.layout.nocity,setcity,false);
        pb = (ProgressBar) nocity.findViewById(R.id.nocityprog);
        pb.setVisibility(View.GONE);

        cityexists = inflator.inflate(R.layout.city,setcity,false);
        title = (TextView) cityexists.findViewById(R.id.city);
        description = (TextView) cityexists.findViewById(R.id.desc);
        temperature = (TextView) cityexists.findViewById(R.id.temp);
        date = (TextView) cityexists.findViewById(R.id.timeupdated);
        icon = (ImageView) cityexists.findViewById(R.id.tempimg);

        if (currCity.equals("")){
            setcity.removeAllViews();

            setcity.addView(nocity);
            Button setCity = (Button) nocity.findViewById(R.id.setcity);
            setCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                    alert.setTitle("Enter City Details");
                    LinearLayout layout = new LinearLayout(MainActivity.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    final EditText city = new EditText(getApplicationContext());
                    city.setHint("Enter Your City");
                    city.setHintTextColor(Color.BLACK);
                    city.setTextColor(Color.BLACK);
                    layout.addView(city);

                    final EditText country = new EditText(getApplicationContext());
                    country.setHint("Enter Your Country");
                    country.setTextColor(Color.BLACK);
                    country.setHintTextColor(Color.BLACK);
                    layout.addView(country);

                    alert.setView(layout);


                    alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String cityname = city.getText().toString();
                            String countryname = country.getText().toString();
                            Log.d("demo", "City: "+cityname);
                            Log.d("demo", "Country: "+countryname);
                            if (cityname.length() >0 && countryname.length() >0){
                                pb.setVisibility(View.VISIBLE);
                                cFlag = "DEF";
                                getCityKey(cityname,countryname);


                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MainActivity.this,"Enter the proper details", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }

                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });

                    alert.show();
                }
            });
        } else {
            setcity.removeAllViews();
            setcity.addView(cityexists);
            cityWeather = new City(currCity,currCityName,currCountry,null,null,"FALSE");


        }


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mRecyclerView = (RecyclerView) findViewById(R.id.savedCitiesListView);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        childref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demo5", String.valueOf(dataSnapshot.getChildrenCount()));
                ArrayList<City> cities= new ArrayList<>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    City city1 = ds.getValue(City.class);
                    cities.add(city1);
                }
                Log.d("demo6",cities.toString());
                savedCities = cities;
                setData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("demo","Cities cannot be fetched");
            }
        });

        //setSavedCitiesRecyclerView(myDataset);

        setData();

        ((Button)findViewById(R.id.searchButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText cityText = (EditText) findViewById(R.id.cityField);
                EditText countryText = (EditText) findViewById(R.id.countryField);
                String city = cityText.getText().toString();
                String country = countryText.getText().toString();
                if ( city.length()>0 && country.length()>0){
                    Log.d("demo", "Button Clicked");
                    Intent intent = new Intent(MainActivity.this,CityWeatherActivity.class);
                    intent.putExtra(CITY_STRING,city);
                    intent.putExtra(COUNTRY_STRING,country);
                    startActivityForResult(intent,1001);
                }
                cityText.setText("");
                countryText.setText("");
            }
        });
    }
    void setSavedCitiesRecyclerView(ArrayList<City> cities){
        // specify an adapter (see also next example)
        Log.d("Status","Setting Adapter"+cities.size());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        SavedCitiesListAdapter  adapter= new SavedCitiesListAdapter(cities,this,tempUnits);
       mRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                Log.d("Click","Clicked");
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                startActivityForResult(intent,2001);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2001) {
            if (resultCode== Activity.RESULT_OK){
                String str = (String) data.getExtras().get("TEMP");
                if (str.equals("OK")){
                    tempUnits = prefs.getString("PREF_UNIT","");
                    setData();
                    Toast.makeText(MainActivity.this,"Units Changed to "+tempUnits, Toast.LENGTH_SHORT).show();
                }

            }

        }
        if(requestCode==1001) {

            if(resultCode== Activity.RESULT_OK ) {
                String flag = data.getExtras().getString(CITY_NOT_FOUND);
                //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                tempUnits = prefs.getString("PREF_UNIT","");
                setData();
                if (flag.equals("TRUE")){
                    Toast.makeText(MainActivity.this,"City not found", Toast.LENGTH_SHORT).show();
                } else {

                }

            }
        }
    }

    protected void getCityKey(String city, String country){

        new GetKeyAsync(this).execute("http://dataservice.accuweather.com/locations/v1/US/search?apikey="+MY_API_KEY+"&q="+city);

    }

    protected void saveCity(City cw ){

        String key = cw.getCitykey();
        Map<String, Object> postValues = cw.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + key, postValues);
        childref.updateChildren(childUpdates);


    }
    protected void fetchWeather(City city){
        cityWeather = city;

        new GetWeatherAsync(this).execute("http://dataservice.accuweather.com/currentconditions/v1/"+city.getCitykey()+"?apikey="+MY_API_KEY, city.getCityname(),city.getCountry());

    }
    protected void storeCity(City city){
        editor.putString(CURR_KEY, city.getCitykey());
        editor.commit();
        editor.putString(CURR_CITY, city.getCityname());
        editor.commit();
        editor.putString(CURR_COUNTRY, city.getCountry());
        editor.commit();

        currCity =  prefs.getString(CURR_KEY,"");
        currCityName =  prefs.getString(CURR_CITY,"");
        currCountry =  prefs.getString(CURR_COUNTRY,"");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menuone,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public ArrayList<City> getAllSavedCities(){
        return null;
    }

    void setData(){
        Log.d("demo", "inside set data");
        tempUnits = prefs.getString("PREF_UNIT","");
        currCity =  prefs.getString(CURR_KEY,"");
        currCityName =  prefs.getString(CURR_CITY,"");
        currCountry =  prefs.getString(CURR_COUNTRY,"");
        setCurrentCity();
        sortData();
        if (savedCities.size()>0){
            Log.d("demo", "Size"+savedCities.size());
            messageText.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.VISIBLE);
            headerText.setVisibility(View.VISIBLE);
            setSavedCitiesRecyclerView(savedCities);
        }
        else{
            messageText.setText(getString(R.string._no_cities));

            messageText.setVisibility(View.VISIBLE);
            headerText.setVisibility(View.INVISIBLE);
        }
    }
    public void setCurrentCity(){
        if (currCity.equals("")){

        }else {

            new GetWeatherAsync(this).execute("http://dataservice.accuweather.com/currentconditions/v1/"+currCity+"?apikey="+MY_API_KEY, currCityName,currCountry);


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Resume","Resumed");
        setData();
    }

    void sortData(){
        ArrayList<City> tempList = new ArrayList<>();
        ArrayList<City> tempList1 = new ArrayList<>();
        ArrayList<City> newList = new ArrayList<>();
        for (City city:savedCities){
            if (city.getFavorite().equalsIgnoreCase("TRUE")){
                tempList.add(city);
            }else {
                tempList1.add(city);
            }
        }
        for (City city:tempList){
            newList.add(city);
        }
        for (City city:tempList1){
            newList.add(city);
        }
        savedCities = newList;
    }

    @Override
    public void setKey(City city) {

        pb.setVisibility(View.GONE);

        if (city !=null){
            //String key =city.getCitykey();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this,"Current City details saved", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d("demo", "City:"+city.toString());
            storeCity(city);
            fetchWeather(city);

        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this,"City not found", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public void setWeather(CityWeather cw) {
        Log.d("demo", "C Flag: "+cFlag);
        if (cFlag.equals("DEF")){
            Log.d("demo", "With "+cw.toString());
            String temp = cw.getTemparature();
            String date = cw.getDate();
            cityWeather.setTemperature(temp);
            cityWeather.setDate(date);
            saveCity(cityWeather);
            cFlag = "ABC";
            setData();
            //setCurrCityData(cw);

        }else {
            setCurrCityData(cw);
        }

    }

    public void setCurrCityData(CityWeather cw) {
        setcity.removeAllViews();
        setcity.addView(cityexists);
        String title1 = cw.getCity()+", "+cw.getCountry();
        title.setText(title1);
        description.setText(cw.getDescription());
        int icon1 = Integer.parseInt(cw.getIcon());
        String img = cw.getIcon();
        if (icon1 <10){
            img = "0"+icon1;
        }
        String imgUrl = "http://developer.accuweather.com/sites/default/files/"+img+"-s.png";
        Picasso.with(MainActivity.this).load(imgUrl).into(icon);
        if (tempUnits.equals("C"))
            temperature.setText("Temperature: "+cw.getTemparature()+"\u00B0 "+tempUnits);
        else {
            Double temp1 = Double.parseDouble(cw.getTemparature());
            temp1 *= 1.8;
            temp1 += 32;
            String min = String.format("%.2f", temp1);
            temperature.setText("Temperature: "+min+"\u00B0 "+tempUnits);
        }

        String dateString=cw.getDate();
        PrettyTime prettyTime = new PrettyTime();
        try {
            Date date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(cw.getDate());
            date.setText("Updated "+prettyTime.format(date1));
        } catch (ParseException e) {
            date.setText("Updated: null");
        }
    }
}
