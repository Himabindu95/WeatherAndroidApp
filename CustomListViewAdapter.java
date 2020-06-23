package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomListViewAdapter extends BaseAdapter {

    private Context context;
    private String weeklyDate[];
    private int flags[];
    private String[] weeklyTempHigh;
    private String[] weeklyTempLow;
    private LayoutInflater inflater;

    public CustomListViewAdapter(Context applicationContext, String[] weeklyDate, int[] flags, String[] weeklyTempHigh, String[] weeklyTempLow) {
        this.context = context;
        this.weeklyDate = weeklyDate;
        this.flags = flags;
        this.weeklyTempHigh = weeklyTempHigh;
        this.weeklyTempLow = weeklyTempLow;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return weeklyDate.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override

    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.activity_listview, viewGroup, false);
        TextView date =  view.findViewById(R.id.date);
        TextView tempHigh = view.findViewById(R.id.tempHigh);
        TextView tempLow =  view.findViewById(R.id.tempLow);
        ImageView weeklyIcon = view.findViewById(R.id.weeklyIcon);
        if(weeklyDate[i] != null){
            date.setText(weeklyDate[i]);
        }
        if(weeklyTempHigh[i] != null){
            tempHigh.setText(weeklyTempHigh[i]);
        }
        if(weeklyTempLow[i] != null){
            tempLow.setText(weeklyTempLow[i]);
        }
        weeklyIcon.setImageResource(flags[i]);

        return view;
    }
}