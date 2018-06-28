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
import com.example.nguye.cameravo.R;

import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {
    private ViewPager mVPMain;
    private TabLayout mTLOMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void initView(){
        mVPMain = findViewById(R.id.mVPMain);
        mTLOMain = findViewById(R.id.mTloMain);
        setupViewpager(mVPMain);
        mTLOMain.setupWithViewPager(mVPMain);
    }

    public void setupViewpager(ViewPager viewPager){
        ManagerFragment managerFragment = new ManagerFragment(getSupportFragmentManager());

        managerFragment.addFragment(new FragmentCamera(), "Camera");
        managerFragment.addFragment(new FragmentGallery(), "Gallery");
        managerFragment.addFragment(new FragmentRecorder(), "Recorder");
        viewPager.setAdapter(managerFragment);
    }
}
