package com.example.weatherapp.ui.main;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.weatherapp.R;
import com.example.weatherapp.TodaysWeatherAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class TodaysWeatherFragment extends Fragment {

    private GridView gridView;
    private String[] weather_variants;
    private int icon;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todaysweather_fragment, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView1);
        String windspeed, pressure, precipitation, temperature, humidity, visibility, cloudcover, ozone;

        if(getArguments() != null) {

            try {
                JSONObject json = new JSONObject(getArguments().getString("json"));
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                if(json.getJSONObject("currently").getString("windSpeed")!= null || json.getJSONObject("currently").getString("windSpeed") != ""){
                    windspeed = String.valueOf(df.format(json.getJSONObject("currently").getDouble("windSpeed")))+" mph";
                } else {
                    windspeed = "NA";
                }

                if(json.getJSONObject("currently").getString("pressure")!= null || json.getJSONObject("currently").getString("pressure") != ""){
                    pressure = String.valueOf(df.format(json.getJSONObject("currently").getDouble("pressure"))).replaceAll(",","")+" mb";
                } else {
                    pressure = "NA";
                }
                if(json.getJSONObject("currently").getString("precipIntensity")!= null || json.getJSONObject("currently").getString("precipIntensity") != ""){
                    precipitation = String.valueOf(df.format(json.getJSONObject("currently").getDouble("precipIntensity")))+" mmph";
                } else {
                    precipitation = "NA";
                }
                if(json.getJSONObject("currently").getString("temperature")!= null || json.getJSONObject("currently").getString("temperature") != ""){
                    temperature = String.valueOf(Math.round(json.getJSONObject("currently").getDouble("temperature")))+"\u00B0F";
                } else {
                    temperature = "NA";
                }
                if(json.getJSONObject("currently").getString("humidity")!= null || json.getJSONObject("currently").getString("humidity") != ""){
                    humidity = String.valueOf(Math.round((json.getJSONObject("currently").getDouble("humidity")*100)))+"%";
                } else {
                    humidity = "NA";
                }
                if(json.getJSONObject("currently").getString("visibility")!= null || json.getJSONObject("currently").getString("visibility") != ""){
                    visibility = String.valueOf(df.format(json.getJSONObject("currently").getDouble("visibility")))+" km";
                } else {
                    visibility = "NA";
                }
                if(json.getJSONObject("currently").getString("cloudCover")!= null || json.getJSONObject("currently").getString("cloudCover") != ""){
                    cloudcover = String.valueOf(Math.round(json.getJSONObject("currently").getDouble("cloudCover")))+"%";
                } else {
                    cloudcover = "NA";
                }
                if(json.getJSONObject("currently").getString("ozone")!= null || json.getJSONObject("currently").getString("ozone") != ""){
                    ozone = String.valueOf(df.format(json.getJSONObject("currently").getDouble("ozone")))+" DU";
                } else {
                    ozone = "NA";
                }

                String summaryIconText = json.getJSONObject("currently").getString("icon");
                String[] splitIconText = summaryIconText.split("-");

                String summaryText = String.join(" ", splitIconText);
                System.out.println("summaryText"+summaryText);
                if(summaryText.equals("partly cloudy day")){
                    summaryText = "cloudy day";
                }
                if(summaryText.equals("partly cloudy night")){
                    summaryText = "cloudy night";
                }

                weather_variants = new String[] { windspeed, pressure, precipitation, temperature,
                        "", humidity, visibility, cloudcover, ozone };

                if (summaryIconText.equals("clear-day")){
                    icon = R.drawable.weather_sunny;
                }
                if(summaryIconText.equals("clear-night")){
                    icon = R.drawable.weather_night;
                }
                if (summaryIconText.equals("rain")){
                    icon = R.drawable.weather_rainy;
                }
                if (summaryIconText.equals("sleet")){
                    icon = R.drawable.weather_snowy_rainy;
                }
                if (summaryIconText.equals("wind")){
                    icon = R.drawable.weather_windy_variant;
                }
                if (summaryIconText.equals("fog")){
                    icon = R.drawable.weather_fog;
                }
                if (summaryIconText.equals("cloudy")){
                    icon = R.drawable.weather_cloudy;
                }
                if (summaryIconText.equals("partly-cloudy-night")){
                    icon = R.drawable.weather_night_partly_cloudy;
                }
                if (summaryIconText.equals("partly-cloudy-day")){
                    icon = R.drawable.weather_partly_cloudy;
                }

                int images[] = {R.drawable.weather_windy, R.drawable.gauge, R.drawable.weather_pouring, R.drawable.thermometer, icon, R.drawable.water_percent, R.drawable.eye_outline, R.drawable.weather_fog_purple, R.drawable.earth};
                gridView.setAdapter(new TodaysWeatherAdapter(getContext(), weather_variants, images, summaryText));

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {
                        Toast.makeText(
                                getContext(),
                                ((TextView) v.findViewById(R.id.grid_item_value))
                                        .getText(), Toast.LENGTH_SHORT).show();

                    }
                });
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
        return view;
    }
}