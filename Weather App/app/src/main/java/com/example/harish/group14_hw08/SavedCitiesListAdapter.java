package com.example.harish.group14_hw08;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Naveenkanumoori on 10/13/16.
 */
/*
*
* Homework 08
* SavedCitiesListAdapter.java
* Group 14 : Harish Pendyala
*
* */
public class SavedCitiesListAdapter extends RecyclerView.Adapter<SavedCitiesListAdapter.ViewHolder>{

    ArrayList<City> myDataSet;
    Context mContext;
    String unit;
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference childref = dref.child("Cities");

    public Context getMContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView city,temperature,date;
        public ImageView favorite;
        public SavedCitiesListAdapter parent;
        public ViewHolder(View v, final SavedCitiesListAdapter parent) {
            super(v);
            this.parent = parent;
            city = (TextView)v.findViewById(R.id.sCityCountry);
            temperature = (TextView)v.findViewById(R.id.sTemperature);
            date = (TextView)v.findViewById(R.id.sUpdatedTime);
            favorite = (ImageView) v.findViewById(R.id.sFavoriteImage);
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    parent.removeItem(city.getText().toString());
                    Toast.makeText(parent.mContext,"City Deleted: "+city.getText().toString(), Toast.LENGTH_LONG).show();
                    Log.d("Delete","City Deleted: "+city.getText().toString());
                    return false;
                }
            });
        }
    }

    public SavedCitiesListAdapter(ArrayList<City> cities, Context mContext, String unit) {
        this.myDataSet = cities;
        this.mContext = mContext;
        this.unit = unit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View savedCityView = inflater.inflate(R.layout.savedcity,parent,false);
        ViewHolder viewHolder = new ViewHolder(savedCityView,this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final City city = myDataSet.get(position);
        holder.city.setText(city.getCityname()+", "+city.getCountry());
        if (unit.equals("C"))
            holder.temperature.setText("Temperature: "+city.getTemperature()+"\u00B0 "+unit);
        else {
            Double temp = Double.parseDouble(city.getTemperature());
            temp *= 1.8;
            temp += 32;
            String min = String.format("%.2f", temp);
            holder.temperature.setText("Temperature: "+min+"\u00B0 "+unit);
        }
        String dateString=city.getDate();
        PrettyTime prettyTime = new PrettyTime();
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(city.getDate());
            holder.date.setText("Last Updated: "+prettyTime.format(date));
        } catch (ParseException e) {
            holder.date.setText("Last Updated: null");
        }

        Date convertedDate = new Date();

        /*try {
            convertedDate = dateFormat.parse(dateString);
            Log.d("Demo", "Date Format: "+convertedDate);
        } catch (ParseException e) {
            Log.d("Demo", "Exception: Hello");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

        PrettyTime p  = new PrettyTime();

        String datetime= p.format(convertedDate);

        //holder.date.setText("Last Updated: "+datetime);

        if (city.getFavorite().equalsIgnoreCase("TRUE")){
            holder.favorite.setImageResource(R.drawable.star_gold);
        }else {
            holder.favorite.setImageResource(R.drawable.star_gray);
        }

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value;
                if (city.getFavorite().equalsIgnoreCase("TRUE")){
                    value = "FALSE";
                }else {
                    value = "TRUE";
                }
                String key = city.getCitykey();
                childref.child(key).child("favorite").setValue(value);
                sortData();
                notifyDataSetChanged();
            }
        });

    }
    void sortData(){
        ArrayList<City> tempList = new ArrayList<>();
        ArrayList<City> tempList1 = new ArrayList<>();
        ArrayList<City> newList = new ArrayList<>();
        for (City city:myDataSet){
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
        myDataSet = newList;
    }

    @Override
    public int getItemCount() {
        return myDataSet.size();
    }

    void removeItem(String city){
        String[] city1 = city.split(",");
        for (int i =0; i< myDataSet.size(); i++){
            if (myDataSet.get(i).getCityname().equalsIgnoreCase(city1[0].trim())){
                City city2 = myDataSet.get(i);
                String key = city2.getCitykey();
                childref.child(key).removeValue();
                myDataSet.remove(i);
                notifyDataSetChanged();
                if (myDataSet.size()==0){

                    ((MainActivity)mContext).messageText.setVisibility(View.VISIBLE);
                    ((MainActivity)mContext).headerText.setVisibility(View.INVISIBLE);

                }
            }
        }
    }
}
