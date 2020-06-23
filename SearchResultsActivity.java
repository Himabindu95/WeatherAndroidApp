package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class SearchResultsActivity extends AppCompatActivity {

    TabLayout tabs;
    ViewPager viewPager;
    String latitude;
    String longitude;
    String cityJSON;
    String responseString;
    String toastValue;
    ProgressBar spinner;
    TextView progress_bar_text;
    RelativeLayout rLayout;
    ArrayList<String> favcitylist;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_results);

        //Toolbar functionality
        final Intent intent = getIntent();
        final String url = intent.getStringExtra("searchresults");
        Toolbar toolbar = findViewById(R.id.toolbar);
        cityJSON = intent.getStringExtra("toolbartitle");
        toolbar.setTitle(intent.getStringExtra("toolbartitle"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchResultsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



        //Toolbar functionality

        spinner = (ProgressBar)findViewById(R.id.progressBar22);
        spinner.setVisibility(View.VISIBLE);
        progress_bar_text = findViewById(R.id.textview_progressbar1);
        progress_bar_text.setVisibility(View.VISIBLE);
        rLayout=(RelativeLayout) findViewById(R.id.rel1);
        rLayout.setVisibility(View.VISIBLE);


        TextView search_result_text = findViewById(R.id.search_results_text);
        search_result_text.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            Gson gson = new Gson();
                            String geoCodingAPI = gson.fromJson(response, String.class);
                            final JSONObject latLongJson = new JSONObject(geoCodingAPI);


                            latitude = latLongJson.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
                            longitude = latLongJson.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");

//                            final TextView searchResultText = findViewById(R.id.search_result);
//                            searchResultText.setText("Search Result");

                            getForecastDetails(latitude,longitude,intent.getStringExtra("toolbartitle"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // textViewForCard1.setText("That didn't work!");
                //textViewForCard2.setText("That didn't work!");
                //textViewForCard3.setText("That didn't work!");
            }
        });
        requestQueue.add(stringRequest);

        final FloatingActionButton fab = findViewById(R.id.fab_from_search_results);
        fab.setImageDrawable(getDrawable(R.drawable.map_marker_plus));
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = prefs.edit();

        Map<String, ?> allSharedEntries = prefs.getAll();
        if(allSharedEntries.containsKey(cityJSON)){
           fab.setImageDrawable(getDrawable(R.drawable.map_marker_minus));
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Context context = getApplicationContext();
                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                final SharedPreferences.Editor editor = prefs.edit();

                Map<String, ?> allSharedEntries = prefs.getAll();
                if(allSharedEntries.size()==0){
                    fab.setImageDrawable(getDrawable(R.drawable.map_marker_minus));
                    editor.putString(cityJSON, responseString);
                    editor.commit();
                    Toast.makeText(SearchResultsActivity.this, toastValue+" was added to favorites",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    if(!allSharedEntries.containsKey(cityJSON)){
                        fab.setImageDrawable(getDrawable(R.drawable.map_marker_minus));
                        editor.putString(cityJSON, responseString);
                        editor.commit();
                        Toast.makeText(SearchResultsActivity.this, toastValue+" was added to favorites",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        fab.setImageDrawable(getDrawable(R.drawable.map_marker_plus));
                        editor.remove(cityJSON);
                        editor.commit();
                        Toast.makeText(SearchResultsActivity.this, toastValue+" was removed from favorites",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    public void getForecastDetails(String currentLocLatitude, String currentLocLongitude, final String cityState) {
        final TextView temperature = findViewById(R.id.temperature_text);
        final TextView summary = findViewById(R.id.summary_text);
        final TextView city = findViewById(R.id.city_text);
        final ImageView icon = findViewById(R.id.summary_icon);

        final ImageView itemImage2 = findViewById(R.id.item_image2);
        final ImageView itemImage3 = findViewById(R.id.item_image3);
        final ImageView itemImage4 = findViewById(R.id.item_image4);
        final ImageView itemImage5 = findViewById(R.id.item_image5);

        final TextView humidity = findViewById(R.id.humidity);
        final TextView pressure = findViewById(R.id.pressure);
        final TextView windspeed = findViewById(R.id.windspeed);
        final TextView visibility = findViewById(R.id.visibility);


        final ArrayList<String> weeklyDate = new ArrayList<String>();
        final int[] weeklyIcon = new int[8];
        final ArrayList<String> temperatureHigh = new ArrayList<String>();
        final ArrayList<String> temperatureLow = new ArrayList<String>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String currentWeatherURL = "http://hw9-vkanduku1995.us-east-2.elasticbeanstalk.com/api/currentWeather?lat="+currentLocLatitude+"&lng="+currentLocLongitude+"";
        System.out.println("Cur"+currentWeatherURL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, currentWeatherURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            responseString = response;
                            toastValue = cityState;

                            Gson gson = new Gson();
                            String forecastAPI = gson.fromJson(response, String.class);
                            final JSONObject forecastJSON = new JSONObject(forecastAPI);
                            final String tempForTwitter = String.valueOf(Math.round(forecastJSON.getJSONObject("currently").getDouble("temperature")))+"\u00B0F";
                            spinner.setVisibility(View.GONE);
                            progress_bar_text.setVisibility(View.GONE);
                            rLayout.setVisibility(View.GONE);
                            CardView cardView = findViewById(R.id.card_view1);

                            cardView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(SearchResultsActivity.this, DetailedWeatherTabs.class);
                                    intent.putExtra("detailjson", forecastJSON.toString());
                                    //intent.putExtra("detailjson", forecastJSON.toString());
                                    intent.putExtra("detailedtabtoolbartitle",cityState);
                                    intent.putExtra("temp", tempForTwitter);
                                    startActivity(intent);
                                    overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
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
                            city.setText(cityState);

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

                            ListView simpleList = (ListView) findViewById(R.id.simpleListView);
                            CustomListViewAdapter customAdapter = new CustomListViewAdapter(SearchResultsActivity.this, weeklyDateStringArray,  weeklyIcon, weeklyTemperatureHighStringArray, weeklyTemperatureLowStringArray);
                            simpleList.setAdapter(customAdapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // textViewForCard1.setText("That didn't work!");
                //textViewForCard2.setText("That didn't work!");
                //textViewForCard3.setText("That didn't work!");
            }
        });
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
