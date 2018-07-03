package com.example.nguye.cameravo.Activity;

import android.hardware.Camera;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.nguye.cameravo.Fragment.FragmentCamera;
import com.example.nguye.cameravo.Fragment.FragmentGallery;
import com.example.nguye.cameravo.Fragment.FragmentRecorder;
import com.example.nguye.cameravo.Fragment.ManagerFragment;
import com.example.nguye.cameravo.Model.LinkImageTab;
import com.example.nguye.cameravo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {
    private ViewPager mVPMain;
    private TabLayout mTLOMain;
    private ArrayList<LinkImageTab> arrImageTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrImageTabs = new ArrayList<>();
        initImageTab();
        initView();
    }

    private void initImageTab() {
        arrImageTabs.add(new LinkImageTab(R.drawable.icgalleryactive, R.drawable.icgalleryinactive));
        arrImageTabs.add(new LinkImageTab(R.drawable.icimageactive, R.drawable.icimageinactive));
        arrImageTabs.add(new LinkImageTab(R.drawable.icvideoactive, R.drawable.icvideoinactive));
    }

    public void initView(){
        mVPMain = findViewById(R.id.mVPMain);
        mTLOMain = findViewById(R.id.mTloMain);
        setupViewpager(mVPMain);
        mTLOMain.setupWithViewPager(mVPMain);
        mTLOMain.getTabAt(0).setIcon(arrImageTabs.get(0).getmSelection());
        mTLOMain.getTabAt(1).setIcon(R.drawable.icimageinactive);
        mTLOMain.getTabAt(2).setIcon(R.drawable.icvideoinactive);
        mTLOMain.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setIcon(arrImageTabs.get(tab.getPosition()).getmSelection());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(arrImageTabs.get(tab.getPosition()).getmUnSelection());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void setupViewpager(ViewPager viewPager){
        ManagerFragment managerFragment = new ManagerFragment(getSupportFragmentManager());
        managerFragment.addFragment(new FragmentGallery(), "");
        managerFragment.addFragment(new FragmentCamera(), "");
        managerFragment.addFragment(new FragmentRecorder(), "");
        viewPager.setAdapter(managerFragment);
    }
}
