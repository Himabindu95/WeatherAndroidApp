package com.example.weatherapp.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weatherapp.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeeklyWeatherFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.weeklyweather_fragment, container, false);

        if(getArguments() != null) {
            Log.i("WeeklyWeatherFragment", getArguments().getString("json"));

            try {
                JSONObject json = new JSONObject(getArguments().getString("json"));

                TextView textView = (TextView) view.findViewById(R.id.weekly_summary);
                textView.setText(json.getJSONObject("daily").getString("summary"));


                String icon = json.getJSONObject("daily").getString("icon");

                ImageView imageView = (ImageView) view.findViewById(R.id.weekly_icon);

                if (icon.equals("clear-day")){
                    imageView.setImageResource(R.drawable.weather_sunny);
                }
                if(icon.equals("clear-night")){
                    imageView.setImageResource(R.drawable.weather_night);
                }
                if (icon.equals("rain")){
                    imageView.setImageResource(R.drawable.weather_rainy);
                }
                if (icon.equals("sleet")){
                    imageView.setImageResource(R.drawable.weather_snowy_rainy);
                }
                if (icon.equals("wind")){
                    imageView.setImageResource(R.drawable.weather_windy_variant);
                }
                if (icon.equals("fog")){
                    imageView.setImageResource(R.drawable.weather_fog);
                }
                if (icon.equals("cloudy")){
                    imageView.setImageResource(R.drawable.weather_cloudy);
                }
                if (icon.equals("partly-cloudy-night")){
                    imageView.setImageResource(R.drawable.weather_night_partly_cloudy);
                }
                if (icon.equals("partly-cloudy-day")){
                    imageView.setImageResource(R.drawable.weather_partly_cloudy);
                }

                List<Entry> entries1 = new ArrayList<>();
                List<Entry> entries2 = new ArrayList<>();
                for (int i = 0; i < 8; i++) {
                    JSONObject hourlyWeather = (JSONObject) json.getJSONObject("daily").getJSONArray("data").getJSONObject(i);

                    Float fmin = (float) hourlyWeather.getDouble("temperatureLow");// Minimum temperature from json
                    Float fmax = (float) hourlyWeather.getDouble("temperatureHigh"); // Maximum temperature from json
                    entries1.add(new Entry(i, fmin));
                    entries2.add(new Entry(i, fmax));
                }

                LineDataSet dataSet1 = new LineDataSet(entries1, "Minimum Temperature");
                dataSet1.setColor(Color.parseColor("#BB86FC"));
                LineDataSet dataSet2 = new LineDataSet(entries2, "Maximum Temperature");
                dataSet2.setColor(Color.parseColor("#FAAB1A"));

                List<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(dataSet1);
                dataSets.add(dataSet2);
                LineData data = new LineData(dataSets);
                LineChart chart = (LineChart) view.findViewById(R.id.chart);
                chart.setData(data);
                chart.getLegend().setTextSize(16f);
                chart.getAxisLeft().setDrawGridLines(false);
                chart.getAxisRight().setDrawGridLines(false);
                chart.getXAxis().setDrawGridLines(false);
                chart.getAxisRight().setTextColor(Color.parseColor("#FFFFFF")); // left y-axis
                chart.getAxisLeft().setTextColor(Color.parseColor("#FFFFFF")); // left y-axis
                chart.getXAxis().setTextColor(Color.parseColor("#FFFFFF"));
                chart.getLegend().setTextColor(Color.parseColor("#FFFFFF"));
                chart.invalidate();
            }catch(JSONException e) {
                e.printStackTrace();
            }
        }

        return view;
    }
}
