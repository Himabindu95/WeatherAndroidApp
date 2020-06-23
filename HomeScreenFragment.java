package com.example.weatherapp.ui.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.weatherapp.CustomListViewAdapter;
import com.example.weatherapp.DetailedWeatherTabs;
import com.example.weatherapp.MainActivity;
import com.example.weatherapp.R;
import com.example.weatherapp.SearchResultsActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.core.content.ContextCompat.getDrawable;

public class HomeScreenFragment extends Fragment {

    private String cityString;
    private ArrayList<String> cityList;

    public static HomeScreenFragment newInstance(int index, String json, String city) {
        HomeScreenFragment fragment = new HomeScreenFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", index);
        args.putString("currloc", json);
        args.putString("city",city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_main, container, false);

        final FloatingActionButton fab = root.findViewById(R.id.fab_from_search_results);
        cityList = new ArrayList<>();
        cityList.add(getArguments().getString("city"));
        if(cityList.contains("Los Angeles, CA, US")) {
            root.findViewById(R.id.fab_from_search_results).setVisibility(GONE);

        }
        else {
            root.findViewById(R.id.fab_from_search_results).setVisibility(VISIBLE);
        }

        final TextView temperature = root.findViewById(R.id.temperature_text);
        final TextView summary = root.findViewById(R.id.summary_text);
        final TextView city = root.findViewById(R.id.city_text);
        final ImageView icon = root.findViewById(R.id.summary_icon);

        final ImageView itemImage2 = root.findViewById(R.id.item_image2);
        final ImageView itemImage3 = root.findViewById(R.id.item_image3);
        final ImageView itemImage4 = root.findViewById(R.id.item_image4);
        final ImageView itemImage5 = root.findViewById(R.id.item_image5);

        final TextView humidity = root.findViewById(R.id.humidity);
        final TextView pressure = root.findViewById(R.id.pressure);
        final TextView windspeed = root.findViewById(R.id.windspeed);
        final TextView visibility = root.findViewById(R.id.visibility);

        Toolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setVisibility(GONE);

        TextView search_result_text = root.findViewById(R.id.search_results_text);
        search_result_text.setVisibility(GONE);


        final ArrayList<String> weeklyDate = new ArrayList<String>();
        final int[] weeklyIcon = new int[8];
        final ArrayList<String> temperatureHigh = new ArrayList<String>();
        final ArrayList<String> temperatureLow = new ArrayList<String>();
        if(getArguments().getString("currloc")!=""){

            cityString = getArguments().getString("city");

            try {
                Gson gson = new Gson();
                String forecastAPI = gson.fromJson(getArguments().getString("currloc"), String.class);
                final JSONObject forecastJSON = new JSONObject(forecastAPI);
                final String tempForTwitter = String.valueOf(Math.round(forecastJSON.getJSONObject("currently").getDouble("temperature")))+"\u00B0F";

                CardView cardView = root.findViewById(R.id.card_view1);

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), DetailedWeatherTabs.class);
                        intent.putExtra("detailjson", forecastJSON.toString());
                        intent.putExtra("detailedtabtoolbartitle",getArguments().getString("city"));
                        intent.putExtra("temp", tempForTwitter);
                        startActivity(intent);
                    }
                });

                String icon_text = forecastJSON.getJSONObject("currently").getString("icon");
                if (icon_text.contentEquals("clear-day")){
                    icon.setImageResource(R.drawable.weather_sunny);
                }
                if(icon_text.contentEquals("clear-night")){
                    icon.setImageResource(R.drawable.weather_night);
                }
                if (icon_text.contentEquals("rain")){
                    icon.setImageResource(R.drawable.weather_rainy);
                }
                if (icon_text.contentEquals("sleet")){
                    icon.setImageResource(R.drawable.weather_snowy_rainy);
                }
                if (icon_text.contentEquals("wind")){
                    icon.setImageResource(R.drawable.weather_windy_variant);
                }
                if (icon_text.contentEquals("fog")){
                    icon.setImageResource(R.drawable.weather_fog);
                }
                if (icon_text.contentEquals("cloudy")){
                    icon.setImageResource(R.drawable.weather_cloudy);
                }
                if (icon_text.contentEquals("partly-cloudy-night")){
                    icon.setImageResource(R.drawable.weather_night_partly_cloudy);
                }
                if (icon_text.contentEquals("partly-cloudy-day")){
                    icon.setImageResource(R.drawable.weather_partly_cloudy);
                }
                temperature.setText((String.valueOf(Math.round(forecastJSON.getJSONObject("currently").getDouble("temperature"))))+"\u00B0F");
                summary.setText(forecastJSON.getJSONObject("currently").getString("summary"));
                city.setText(getArguments().getString("city"));

                itemImage2.setImageResource(R.drawable.water_percent);
                humidity.setText(String.valueOf(Math.round((forecastJSON.getJSONObject("currently").getDouble("humidity")*100)))+"%");
                itemImage3.setImageResource(R.drawable.weather_windy);
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                windspeed.setText(String.valueOf(df.format(forecastJSON.getJSONObject("currently").getDouble("windSpeed")))+" mph");
                itemImage4.setImageResource(R.drawable.eye_outline);
                visibility.setText(String.valueOf(df.format(forecastJSON.getJSONObject("currently").getDouble("visibility")))+" km");
//                            visibility.append(forecastJSON.getJSONObject("currently").getString("visibility")+" km");
                itemImage5.setImageResource(R.drawable.gauge);
                pressure.setText(String.valueOf(df.format(forecastJSON.getJSONObject("currently").getDouble("pressure"))).replaceAll(",","")+" mb");

                for(int i=0;i<8;i++){

                    JSONObject hourlyWeather = (JSONObject) forecastJSON.getJSONObject("daily").getJSONArray("data").getJSONObject(i);

                    long timestamp = Long.parseLong(hourlyWeather.getString("time"));
                    Date date = new Date(timestamp*1000L);
                    SimpleDateFormat jdf = new SimpleDateFormat("MM/dd/YYYY");
                    weeklyDate.add(jdf.format(date));
                    if (hourlyWeather.getString("icon").equals("clear-day")){
                        weeklyIcon[i] = R.drawable.weather_sunny;
                    }
                    if(hourlyWeather.getString("icon").equals("clear-night")){
                        weeklyIcon[i] = R.drawable.weather_night;
                    }
                    if (hourlyWeather.getString("icon").equals("rain")){
                        weeklyIcon[i] = R.drawable.weather_rainy;
                    }
                    if (hourlyWeather.getString("icon").equals("sleet")){
                        weeklyIcon[i] = R.drawable.weather_snowy_rainy;
                    }
                    if (hourlyWeather.getString("icon").equals("wind")){
                        weeklyIcon[i] = R.drawable.weather_windy_variant;
                    }
                    if (hourlyWeather.getString("icon").equals("fog")){
                        weeklyIcon[i] = R.drawable.weather_fog;
                    }
                    if (hourlyWeather.getString("icon").equals("cloudy")){
                        weeklyIcon[i] = R.drawable.weather_cloudy;
                    }
                    if (hourlyWeather.getString("icon").equals("partly-cloudy-night")){
                        weeklyIcon[i] = R.drawable.weather_night_partly_cloudy;
                    }
                    if (hourlyWeather.getString("icon").equals("partly-cloudy-day")){
                        weeklyIcon[i] = R.drawable.weather_partly_cloudy;
                    }

                    temperatureHigh.add((String.valueOf(Math.round(hourlyWeather.getDouble("temperatureLow")))));
                    temperatureLow.add((String.valueOf(Math.round(hourlyWeather.getDouble("temperatureHigh")))));
                }

                String[] tempArrayDate = new String[weeklyDate.size()];
                String[] weeklyDateStringArray = weeklyDate.toArray(tempArrayDate);

                String[] tempArrayTempHigh = new String[temperatureHigh.size()];
                String[] weeklyTemperatureHighStringArray = temperatureHigh.toArray(tempArrayTempHigh);

                String[] tempArrayTempLow = new String[temperatureLow.size()];
                String[] weeklyTemperatureLowStringArray = temperatureLow.toArray(tempArrayTempLow);

                ListView simpleList = root.findViewById(R.id.simpleListView);
                CustomListViewAdapter customAdapter = new CustomListViewAdapter(getContext(), weeklyDateStringArray,  weeklyIcon, weeklyTemperatureHighStringArray, weeklyTemperatureLowStringArray);
                simpleList.setAdapter(customAdapter);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final SharedPreferences.Editor editor = prefs.edit();
        Map<String, ?> allSharedEntries = prefs.getAll();
        if(allSharedEntries.size()>0){
            fab.setImageDrawable(getDrawable(getActivity(),R.drawable.map_marker_minus));
        }
        else{
               fab.setImageDrawable(getDrawable(getActivity(),R.drawable.map_marker_plus));
        }
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                fab.setImageDrawable(getDrawable(getActivity(), R.drawable.map_marker_minus));
                Context context = getContext();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = prefs.edit();
                String toastValue = getArguments().getString("city");
                editor.remove(getArguments().getString("city"));
                editor.commit();
                ViewPager viewPager = ((MainActivity) getActivity()).findViewById(R.id.view_pager);
                viewPager.getAdapter().notifyDataSetChanged();
                Map<String, ?> removedEntries = prefs.getAll();
                ArrayList<String> removedCitiesData = new ArrayList<>();
                ArrayList<String> removedCitiesTitles = new ArrayList<>();
                for(Map.Entry<String, ?> entry : removedEntries.entrySet()) {
                    removedCitiesData.add(entry.getValue().toString());
                    removedCitiesTitles.add(entry.getKey());
                }
                Toast.makeText(getContext(), toastValue+" was removed from favorites",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    public void onResume() {
        super.onResume();
    }

//    @Override
//    public void onBackPressed() {
//        startActivity(new Intent(this, MainActivity.class));
//    }
}