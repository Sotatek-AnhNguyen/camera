package com.example.nguye.cameravo.Activity;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.hardware.Camera;
import android.icu.text.SimpleDateFormat;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
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
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.nguye.cameravo.Camera.CameraManager;
import com.example.nguye.cameravo.Camera.CameraPreview;
import com.example.nguye.cameravo.Fragment.FragmentCamera;
import com.example.nguye.cameravo.Fragment.FragmentGallery;
import com.example.nguye.cameravo.Fragment.FragmentRecorder;
import com.example.nguye.cameravo.Fragment.ManagerFragment;
import com.example.nguye.cameravo.Model.LinkImageTab;
import com.example.nguye.cameravo.R;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity  {
    private ViewPager mVPMain;
    private TabLayout mTLOMain;
    private ArrayList<LinkImageTab> arrImageTabs;
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout frameLayout;
    private MediaRecorder mMediaRecorder;
    private Camera.CameraInfo info;
    private int currentCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrImageTabs = new ArrayList<>();
        info = new Camera.CameraInfo();
        currentCameraId = info.facing;
        initImageTab();
        initView();
    }

    private void initImageTab() {
        arrImageTabs.add(new LinkImageTab(R.drawable.icgalleryactive, R.drawable.icgalleryinactive));
        arrImageTabs.add(new LinkImageTab(R.drawable.icimageactive, R.drawable.icimageinactive));
        arrImageTabs.add(new LinkImageTab(R.drawable.icvideoactive, R.drawable.icvideoinactive));
    }

    public void initView(){
        frameLayout = findViewById(R.id.camRecorderPreview);
        mVPMain = findViewById(R.id.mVPMain);
        mTLOMain = findViewById(R.id.mTloMain);
        setupViewpager(mVPMain);
        mTLOMain.setupWithViewPager(mVPMain);
        mTLOMain.setSelectedTabIndicatorColor(Color.TRANSPARENT);

        mCamera = CameraManager.getCameraInstance(Camera.CameraInfo.CAMERA_FACING_BACK);
        mPreview = new CameraPreview(this, mCamera);
        frameLayout.addView(mPreview);

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

    public void cameraImage(Camera.PictureCallback pictureCallback){
        mCamera.takePicture(null, null, pictureCallback);
    }

    public void releaseMediaRecorder() {
        if (mMediaRecorder != null){
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }
    public void cameraUnlock(){
        mCamera.unlock();
    }

    public void setCam(MediaRecorder mediaRecorder){
        mediaRecorder.setCamera(mCamera);
    }

    public void setPreViewDisplay(MediaRecorder mMediaRecorder){
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
    }

    public void switchCamera(){
        mCamera.release();
        frameLayout.removeView(mPreview);

        if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK){
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }else {
            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }

        mCamera = CameraManager.getCameraInstance(currentCameraId);
        mPreview = new CameraPreview(getApplication(), mCamera);
        frameLayout.addView(mPreview);
    }

    public void setFlashAuto(){
        Camera.Parameters parameter = mCamera.getParameters();
        parameter.setFlashMode(Camera.Parameters.ANTIBANDING_AUTO);
        Toast.makeText(this, "Flash tự động",Toast.LENGTH_SHORT).show();
        mCamera.setParameters(parameter);
    }

    public void setFlashActive(){
        Camera.Parameters parameter = mCamera.getParameters();
        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        Toast.makeText(this, "Flash Luôn bật",Toast.LENGTH_SHORT).show();
        mCamera.setParameters(parameter);
    }

    public void setFlashInActive(){
        Camera.Parameters parameter = mCamera.getParameters();
        parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        Toast.makeText(this, "Tắt flash",Toast.LENGTH_SHORT).show();
        mCamera.setParameters(parameter);
    }

}
