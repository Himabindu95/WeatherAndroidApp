package com.example.weatherapp.ui.main;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.weatherapp.R;

public class DetailedSectionPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;
    private String mJson;
    private String mcities;

    public DetailedSectionPagerAdapter(Context context, FragmentManager fm, String json, String cities) {
        super(fm);
        mContext = context;
        mJson = json;
        mcities = cities;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TodaysWeatherFragment fragment = new TodaysWeatherFragment();
                Bundle bundle = new Bundle();
                bundle.putString("json", mJson);
                fragment.setArguments(bundle);
                return fragment;
            case 1:
                WeeklyWeatherFragment tab2 = new WeeklyWeatherFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("json", mJson);
                tab2.setArguments(bundle1);
                return tab2;
            case 2:
                GooglePhotosFragment tab3 = new GooglePhotosFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("cities", mcities);
                tab3.setArguments(bundle2);
                return tab3;
            default:
                return null;
        }

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }
}