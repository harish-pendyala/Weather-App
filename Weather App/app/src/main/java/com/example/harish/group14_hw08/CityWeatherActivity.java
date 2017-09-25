package com.example.harish.group14_hw08;
/*
*
* Homework 08
* CityWeatherActivity.java
* Group 14 : Harish Pendyala
*
* */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionBarContextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CityWeatherActivity extends AppCompatActivity implements GetKeyAsync.getKey,GetDayWeatherAsync.loadWeather {

    TextView title, headtext, headline, date, temp, condition, day, daydesc,
                night, nightdesc, moredetails, extended;
    ImageView dayimg, nightimg;
    RecyclerView rview;
    ProgressDialog pd;
    String city, country,tempUnits;
    Day weather;
    ArrayList<DayWeather> daysWeather;
    DayWeather dw;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String extendedURL, moreURL;
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference childref;
    String currCity,currCityName,currCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        ActionBar ab = getSupportActionBar();
        //ab.setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.icon);
        currCity =  prefs.getString(MainActivity.CURR_KEY,"");
        currCityName =  prefs.getString(MainActivity.CURR_CITY,"");
        currCountry =  prefs.getString(MainActivity.CURR_COUNTRY,"");
        tempUnits = prefs.getString("PREF_UNIT","");
        childref = dref.child("Cities");
        pd = new ProgressDialog(this);
        pd.setMessage("Loading Data");
        pd.setCancelable(false);

        pd.show();
        city = (String) getIntent().getExtras().get(MainActivity.CITY_STRING);
        country = (String) getIntent().getExtras().get(MainActivity.COUNTRY_STRING);
        new GetKeyAsync(this).execute("http://dataservice.accuweather.com/locations/v1/US/search?apikey="+MainActivity.MY_API_KEY+"&q="+city);

        title = (TextView) findViewById(R.id.titile);
        headtext = (TextView) findViewById(R.id.headlinetext);
        headline = (TextView) findViewById(R.id.headline);
        date = (TextView) findViewById(R.id.date);
        temp = (TextView) findViewById(R.id.temperature);
        condition = (TextView) findViewById(R.id.condition);
        day = (TextView) findViewById(R.id.day);
        daydesc = (TextView) findViewById(R.id.dayDesc);
        night = (TextView) findViewById(R.id.night);
        nightdesc = (TextView) findViewById(R.id.nightdesc);
        moredetails = (TextView) findViewById(R.id.moredetails);
        extended = (TextView) findViewById(R.id.extened);
        dayimg = (ImageView) findViewById(R.id.dayimage);
        nightimg = (ImageView) findViewById(R.id.nightimage);
        rview = (RecyclerView) findViewById(R.id.rview);


    }

    @Override
    public void setKey(City city) {
        if (city !=null){

            String key = city.getCitykey();
            new GetDayWeatherAsync(this).execute("http://dataservice.accuweather.com/forecasts/v1/daily/5day/"+key+"?apikey=qE8quuOSiRKIAHoCCQi9WGxiXJ6u3koG", key, city.getCityname(),city.getCountry());


        } else{
            Intent in = new Intent();
            in.putExtra(MainActivity.CITY_NOT_FOUND,"TRUE");
            setResult(Activity.RESULT_OK,in);
            this.finish();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menutwo,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item1:
                saveCity();
                break;
            case R.id.item2:
                setCurrentCity();
                break;
            case R.id.item3:
                Log.d("Click","Clicked");
                Intent intent = new Intent(CityWeatherActivity.this,SettingsActivity.class);
                startActivityForResult(intent,2001);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveCity(){
        City city = new City();
        final String key = weather.getCitykey();
        final String date = daysWeather.get(0).getDate();
        city.setCitykey(key);
        city.setCityname(weather.getCityname());
        city.setCountry(weather.getCountry());
        Double max = Double.valueOf(daysWeather.get(0).getMax());
        Double min = Double.valueOf(daysWeather.get(0).getMin());
        Double temp = (max + min)/2;
        final String t = String.valueOf(temp);
        city.setTemperature(t);
        city.setDate(daysWeather.get(0).getDate());
        city.setFavorite("FALSE");
        final City city1 = city;
        childref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    childref.child(key).child("temperature").setValue(t);
                    childref.child(key).child("date").setValue(date);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CityWeatherActivity.this,"City Updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    Map<String, Object> postValues = city1.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/" + key, postValues);
                    childref.updateChildren(childUpdates);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CityWeatherActivity.this,"City Saved", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public void setCurrentCity(){
        if (currCity.equals("")){
            editor.putString(MainActivity.CURR_KEY, weather.getCitykey());
            editor.putString(MainActivity.CURR_CITY, weather.getCityname());
            editor.putString(MainActivity.CURR_COUNTRY, weather.getCountry());
            editor.commit();
            Toast.makeText(CityWeatherActivity.this,"Current City Saved", Toast.LENGTH_SHORT).show();

        } else {
            editor.remove(MainActivity.CURR_CITY);
            editor.remove(MainActivity.CURR_KEY);
            editor.remove(MainActivity.CURR_COUNTRY);
            editor.commit();
            editor.putString(MainActivity.CURR_KEY, weather.getCitykey());
            editor.putString(MainActivity.CURR_CITY, weather.getCityname());
            editor.putString(MainActivity.CURR_COUNTRY, weather.getCountry());
            editor.commit();
            Toast.makeText(CityWeatherActivity.this,"Current City Updated", Toast.LENGTH_SHORT).show();
        }

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
                    Toast.makeText(CityWeatherActivity.this,"Units Changed to "+tempUnits, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    @Override
    public void setWeather(Day day) {
        pd.dismiss();
        weather = day;
        daysWeather = day.getDw();
        dw = daysWeather.get(0);
        setData();
        Log.d("demo", "Parse Complete"+day.toString());

    }
    public void setData( )  {
        title.setText("Daily forecast for "+weather.getCityname()+", "+weather.getCountry());
        headline.setText(weather.getHeadline());
        extendedURL = weather.getUrl();
        moreURL = dw.getUrl();
        moredetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(moreURL));
                startActivity(i);
            }
        });
        extended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(extendedURL));
                startActivity(i);
            }
        });

        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(dw.getDate());
            String dateString = new SimpleDateFormat("MMMM dd, yyyy").format(date1);
            date.setText("Forecast on "+ dateString);
        } catch (ParseException e) {
            date.setText("Forecast on null");
        }
        if (tempUnits.equals("C"))
            temp.setText("Temperature: "+dw.getMax()+"\u00B0 "+tempUnits+" / "+dw.getMin()+"\u00B0 "+tempUnits);
        else {
            Double temp1 = Double.parseDouble(dw.getMax());
            temp1 *= 1.8;
            temp1 += 32;
            String max = String.format("%.2f", temp1);
            Double temp2 = Double.parseDouble(dw.getMin());
            temp2 *= 1.8;
            temp2 += 32;
            String min = String.format("%.2f", temp2);
            temp.setText("Temperature: "+max+"\u00B0 "+tempUnits+" / "+min+"\u00B0 "+tempUnits);
        }
        daydesc.setText(dw.getDaytext());
        nightdesc.setText(dw.getNighttext());
        int icon1 = Integer.parseInt(dw.getDayicon());;
        String img1 = dw.getDayicon();
        if (icon1 <10){
            img1 = "0"+icon1;
        }
        String imgUrl = "http://developer.accuweather.com/sites/default/files/"+img1+"-s.png";
        Picasso.with( this).load(imgUrl).into(dayimg);
        int icon2 = Integer.parseInt(dw.getNighticon());;
        String img2 = dw.getNighticon();
        if (icon2 <10){
            img2 = "0"+icon2;
        }
        String imgUrl1 = "http://developer.accuweather.com/sites/default/files/"+img2+"-s.png";
        Picasso.with( this).load(imgUrl1).into(nightimg);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rview.setLayoutManager(mLayoutManager);
        DayListAdapter adapter = new DayListAdapter(daysWeather,this);
        rview.setAdapter(adapter);

    }
    public void onDayClick(View v){
        int pos = (int) v.getTag();
        dw=daysWeather.get(pos);
        setData();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent in = new Intent();
        in.putExtra(MainActivity.CITY_NOT_FOUND,"FALSE");
        setResult(Activity.RESULT_OK,in);
        this.finish();
    }
}
