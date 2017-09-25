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
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by haris on 4/8/2017.
 */
/*
*
* Homework 08
* DayListAdapter.java
* Group 14 : Harish Pendyala
*
* */

public class DayListAdapter extends RecyclerView.Adapter<DayListAdapter.ViewHolder>{

    ArrayList<DayWeather> myDataSet;
    Context mContext;
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference childref = dref.child("Cities");

    public Context getMContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView date;
        public ImageView dayImg;
        public DayListAdapter parent;
        public View view1;
        public ViewHolder(View v, final DayListAdapter parent) {
            super(v);
            this.parent = parent;
            date = (TextView)v.findViewById(R.id.daydate);
            dayImg = (ImageView) v.findViewById(R.id.dayimg);
            view1 = v;

        }
    }

    public DayListAdapter(ArrayList<DayWeather> days, Context mContext) {
        this.myDataSet = days;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View savedCityView = inflater.inflate(R.layout.day,parent,false);
        ViewHolder viewHolder = new ViewHolder(savedCityView,this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DayWeather dw = myDataSet.get(position);
        holder.view1.setTag(position);
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(dw.getDate());
            String dateString = new SimpleDateFormat("dd'th' MMM''yy").format(date1);
            holder.date.setText( dateString);
        } catch (ParseException e) {
            holder.date.setText("Null");
        }
        int icon1 = Integer.parseInt(dw.getDayicon());;
        String img1 = dw.getDayicon();
        if (icon1 <10){
            img1 = "0"+icon1;
        }
        String imgUrl = "http://developer.accuweather.com/sites/default/files/"+img1+"-s.png";
        Picasso.with(mContext).load(imgUrl).into(holder.dayImg);


    }

    @Override
    public int getItemCount() {
        return myDataSet.size();
    }


}

