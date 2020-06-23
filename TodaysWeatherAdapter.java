package com.example.weatherapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TodaysWeatherAdapter extends BaseAdapter {

    private Context context;
    private final String[] weatherVariants;
    private final int[] images;
    private String summaryText;


    public TodaysWeatherAdapter (Context context, String[] weatherVariants, int[] images, String summaryText) {
        this.context = context;
        this.weatherVariants = weatherVariants;
        this.images = images;
        this.summaryText = summaryText;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            gridView = inflater.inflate(R.layout.todays_grid_layout, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_value);
            textView.setText(weatherVariants[position]);

            // set image based on selected text

            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);
            if(position == 4) {
                imageView.setScaleX(1.75f);
                imageView.setScaleY(1.75f);
                imageView.setPadding(0,70,0,0);
                imageView.setImageResource(images[4]);

            }
            else{
                imageView.setImageResource(images[position]);
            }




            String[] weatherLabels = {"Wind Speed","Pressure","Precipication","Temperature",summaryText,"Humidity","Visibility","Cloud Cover","Ozone"};

            TextView textView1 = (TextView) gridView
                    .findViewById(R.id.grid_item_label);
            if(position==4){
                textView1.setTextSize(16);
                textView1.setText(weatherLabels[4]);
            }
            else{
                textView1.setText(weatherLabels[position]);
            }


        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}