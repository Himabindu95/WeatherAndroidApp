package com.example.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.weatherapp.ui.main.HomeScreenSectionsPagerAdapter;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabs;
    private ViewPager viewPager;
    private String currentWeatherURL;
    private final String[] predictions = new String[5];
    private String latitude = "";
    private String longitude = "";
    private String latLongURL;
    private ProgressBar spinner;
    private TextView progressBarText;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private static final int TRIGGER_AUTO_COMPLETE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        spinner = findViewById(R.id.progressBar1);
        spinner.setVisibility(View.VISIBLE);

        progressBarText = findViewById(R.id.textview_progressbar1);
        progressBarText.setVisibility(View.VISIBLE);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "http://ip-api.com/json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String currentLocLatitude = response.getString("lat");
                            String currentLocLongitude = response.getString("lon");
                            String currentLocCity = response.getString("city");
                            String currentLocState = response.getString("region");
                            String currentLocCountry = response.getString("countryCode");
                            getForecastDetails(currentLocLatitude,currentLocLongitude,currentLocCity,currentLocState,currentLocCountry);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                    }
                });

        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.homepage_toolbarmenuitem, menu);
        final androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) menu.findItem(R.id.city_search).getActionView();

        final androidx.appcompat.widget.SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(R.id.search_src_text);
        searchAutoComplete.setThreshold(2);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.background_light);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);

                Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);


                intent.putExtra("toolbartitle", queryString);

                String[] values = queryString.split(",");
                String q1 = values[0];
                String q2 = values[1];
                String q3 = "";
                if(values.length > 2){
                    q3 = values[2];
                }
                getLatLong(q1,q2,q3);
                intent.putExtra("searchresults", latLongURL);
                tabs = findViewById(R.id.tabs);
                String abc = Integer.toString(tabs.getTabCount());
                intent.putExtra("json", abc);
                //spinner.setVisibility(View.GONE);

               startActivity(intent);
               overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );


            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {



                return false;
            }

            Handler handler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    if (msg.what == TRIGGER_AUTO_COMPLETE) {
                        if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
                            getAutocompleteValues(searchView.getQuery(),searchAutoComplete);
                        }
                    }
                    return false;
                }
            });

            @Override
            public boolean onQueryTextChange(String newText) {
                //if(searchView.getQuery() != "" || searchView.getQuery() != null){
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
                //getAutocompleteValues(searchView.getQuery(),searchAutoComplete);

//                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
//                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
//                        AUTO_COMPLETE_DELAY);




              //  }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.city_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void getForecastDetails(String currentLocLatitude, String currentLocLongitude, final String currentLocCity, final String currentLocState, final String currentLocCountry) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        currentWeatherURL = "http://hw9-vkanduku1995.us-east-2.elasticbeanstalk.com/api/currentWeather?lat="+currentLocLatitude+"&lng="+currentLocLongitude+"";
        System.out.println("Cur"+currentWeatherURL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, currentWeatherURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            ArrayList<String> arr = new ArrayList<>();
                            ArrayList<String> arr1 = new ArrayList<>();
                            Intent intent1 = new Intent(MainActivity.this, HomeScreenSectionsPagerAdapter.class);
                            intent1.putExtra("currentlocationjson", response);
                            arr.add(intent1.getStringExtra("currentlocationjson"));

                            String city = ""+currentLocCity+", "+currentLocState+", "+currentLocCountry+"";
                            arr1.add(city);
                            HomeScreenSectionsPagerAdapter sectionsPagerAdapter = new HomeScreenSectionsPagerAdapter(getSupportFragmentManager(), 1, arr, arr1);
                            viewPager = findViewById(R.id.view_pager);
                            viewPager.setAdapter(sectionsPagerAdapter);
                            tabs = findViewById(R.id.tabs);
                            tabs.setupWithViewPager(viewPager);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            spinner.setVisibility(View.GONE);
                            progressBarText.setVisibility(View.GONE);
                            ArrayList<String> favoritesCityTitle = new ArrayList<>();
                            ArrayList<String> favorites = new ArrayList<>();
                            favorites.add(preferences.getString(city,""));
                            favoritesCityTitle.add(arr1.get(0));
                            Map<String, ?> allSharedEntries = preferences.getAll();
                            for(Map.Entry<String, ?> entry : allSharedEntries.entrySet()) {
                                if(!(favoritesCityTitle.contains(entry.getKey()))){
                                    favorites.add(entry.getValue().toString());
                                    favoritesCityTitle.add(entry.getKey());
                                }
                            }
                            addToFavorites(favorites.size(), tabs, viewPager, favorites, favoritesCityTitle);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            }
                    });
        requestQueue.add(stringRequest);
    }

    public void getAutocompleteValues(CharSequence city, final SearchView.SearchAutoComplete searchAutoComplete) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String autoCompleteURL = "http://hw9-vkanduku1995.us-east-2.elasticbeanstalk.com/api/cityAutoComplete?city="+city+"";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, autoCompleteURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            Gson gson = new Gson();
                            String autoCompleteAPI = gson.fromJson(response, String.class);
                            final JSONObject autoCompleteJSON = new JSONObject(autoCompleteAPI);

                            if(!autoCompleteJSON.getString("status").contentEquals("INVALID_REQUEST")){

                                for(int i=0; i<autoCompleteJSON.getJSONArray("predictions").length(); i++) {
                                    predictions[i] = autoCompleteJSON.getJSONArray("predictions").getJSONObject(i).getString("description");

                                }
                                ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, predictions);
                                searchAutoComplete.setAdapter(newsAdapter);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
    }

    public void getLatLong(final String q1, final String q2, final String q3) {
        System.out.println("http://hw9-vkanduku1995.us-east-2.elasticbeanstalk.com/api/searchResults?q1="+q1+"&q2="+q2+"&q3="+q3);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        latLongURL = "http://hw9-vkanduku1995.us-east-2.elasticbeanstalk.com/api/searchResults?q1="+q1+"&q2="+q2+"&q3="+q3+"";
        System.out.println("latlong"+latLongURL);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, latLongURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        try {
                            Gson gson = new Gson();
                            String geoCodingAPI = gson.fromJson(response, String.class);
                            final JSONObject latLongJson = new JSONObject(geoCodingAPI);
                            latitude = latLongJson.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lat");
                            longitude = latLongJson.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").getString("lng");
                            getForecastDetails(latitude,longitude,q1,q2,q3);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);


    }
    public void addToFavorites(int q, TabLayout tab, ViewPager viewPager, ArrayList<String> test, ArrayList<String> city) {

                HomeScreenSectionsPagerAdapter adapter = new HomeScreenSectionsPagerAdapter(getSupportFragmentManager(), q,test, city);
                viewPager.setAdapter(adapter);
                viewPager.setOffscreenPageLimit(10);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
                tab.setTabMode(TabLayout.MODE_SCROLLABLE);
//                }
//                else{
//                    tab.setTabMode(TabLayout.MODE_FIXED);
//                }


    }
//    public void removeFav(int nTabs, TabLayout tab, ViewPager viewPager, ArrayList<String> removedCitiesJson, ArrayList<String> removedCitiesTitles){
//
//        HomeScreenSectionsPagerAdapter adapter = new HomeScreenSectionsPagerAdapter(getSupportFragmentManager(), nTabs,removedCitiesJson, removedCitiesTitles);
//        viewPager.setAdapter(adapter);
//        viewPager.setOffscreenPageLimit(1);
//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab));
//        tab.setTabMode(TabLayout.MODE_SCROLLABLE);
//
//        startActivity(getIntent());
//
//    }

}