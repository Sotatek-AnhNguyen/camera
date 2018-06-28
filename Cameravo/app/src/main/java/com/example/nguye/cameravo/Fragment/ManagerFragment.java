package com.example.nguye.cameravo.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class ManagerFragment extends FragmentPagerAdapter {
    private ArrayList<Fragment> arrFragment = new ArrayList<>();
    private ArrayList<String> arrTitle = new ArrayList<>();

    public ManagerFragment(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return arrFragment.get(position);
    }

    @Override
    public int getCount() {
        return arrFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arrTitle.get(position);
    }

    public void addFragment(Fragment fragment, String title){
        arrFragment.add(fragment);
        arrTitle.add(title);
    }
}
