package com.example.weatherapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.weatherapp.ui.main.DetailedSectionPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class DetailedWeatherTabs extends AppCompatActivity {


    private int[] tabIcons = {
            R.drawable.calendar_today,
            R.drawable.trending_up,
            R.drawable.google_photos
    };
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        setContentView(R.layout.activity_detailed_weather_tabs);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(intent.getStringExtra("detailedtabtoolbartitle"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        DetailedSectionPagerAdapter sectionsPagerAdapter = new DetailedSectionPagerAdapter(this, getSupportFragmentManager(), intent.getStringExtra("detailjson"), intent.getStringExtra("detailedtabtoolbartitle"));
        final ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(tabIcons[0]);
        tabs.getTabAt(1).setIcon(tabIcons[1]);
        tabs.getTabAt(2).setIcon(tabIcons[2]);
        tabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detailspage_toolbarmenuitem, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.twitter_action) {
            intent = getIntent();
            Uri uri = Uri.parse("https://twitter.com/intent/tweet?hashtags=CSCI571WeatherSearch&text=Check Out "+ intent.getStringExtra("detailedtabtoolbartitle")+"’s Weather! It is "+ intent.getStringExtra("temp")+"!"); // missing 'http://' will cause crashed
            Intent twitter = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(twitter);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}