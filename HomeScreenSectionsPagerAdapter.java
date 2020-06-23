package com.example.weatherapp.ui.main;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenSectionsPagerAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;
    ArrayList<String> currjson;
    ArrayList<String> city;

    public HomeScreenSectionsPagerAdapter(FragmentManager fm, int NumOfTabs, ArrayList<String> currjson, ArrayList<String> city) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.currjson = currjson;
        this.city = city;
    }

    @Override
    public Fragment getItem(int position) {

        List<Fragment> fragments = new ArrayList<>();
       ArrayList<String> test = new ArrayList<>();
       for(int i =0; i<currjson.size(); i++) {
            fragments.add(HomeScreenFragment.newInstance(i,currjson.get(i), city.get(i)));
       }
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return HomeScreenSectionsPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}