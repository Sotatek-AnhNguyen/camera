package com.example.nguye.cameravo.Fragment;

import android.graphics.Color;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.nguye.cameravo.Activity.MainActivity;
import com.example.nguye.cameravo.Camera.CameraManager;
import com.example.nguye.cameravo.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FragmentRecorder extends Fragment implements View.OnClickListener {
    private ImageView mBTRecorder;
    private boolean isRecorder;
    private boolean isPauseRecorder;
    private String path;
    private ImageView mImvPauseRecorder;
    private ImageView mImvSwitchCamera;
    private MediaRecorder mMediaRecorder;
    private ImageView mImvFlashInActive;
    private VideoView mVvShowVideoRecorder;
    private ImageView mImvSaveVideo;
    private ImageView mImvCancelVideo;
    private RelativeLayout mRlRecorder;
    private SeekBar mSbVideoRecorder;
    private Handler handler = new Handler();
    private TextView mTvTimeRecorder;
    private boolean playMedia;
    private ArrayList<String> arrDelete;
    private int k;
    private int timePause;
    private int timeplus;
    private boolean isClickVideo;
    private ImageView mImvPauseVideoRecorder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentrecorder, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arrDelete = new ArrayList<>();
        mBTRecorder = view.findViewById(R.id.btRecorder);
        mImvPauseRecorder = view.findViewById(R.id.imvPauseRecorder);
        mImvSwitchCamera = view.findViewById(R.id.imvSwitchCameraR);
        mImvFlashInActive = view.findViewById(R.id.imvFlashInActiveR);
        mVvShowVideoRecorder = view.findViewById(R.id.vvShowVideoRecorder);
        mImvSaveVideo = view.findViewById(R.id.imvSaveVideoR);
        mImvCancelVideo = view.findViewById(R.id.imvCancelVideoR);
        mRlRecorder = view.findViewById(R.id.rlRecorder);
        mSbVideoRecorder = view.findViewById(R.id.sbVideoRecoder);
        mTvTimeRecorder = view.findViewById(R.id.mTvTimeRecorder);
        mImvPauseVideoRecorder = view.findViewById(R.id.imvPauseVideoRecorder);

        k = 0;
        timeplus = 0;
        path = null;
        mBTRecorder.setOnClickListener(this);
        mImvPauseRecorder.setOnClickListener(this);
        mImvSwitchCamera.setOnClickListener(this);
        mImvFlashInActive.setOnClickListener(this);
        mImvSaveVideo.setOnClickListener(this);
        mImvCancelVideo.setOnClickListener(this);
        mImvPauseVideoRecorder.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        timeplus = timePause;

        if (playMedia) {
            mMediaRecorder.resume();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onPause() {
        super.onPause();
        timePause = timeplus;
        if (playMedia) {
            mMediaRecorder.pause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (int i = 0; i < arrDelete.size(); i++){
            File fileDele = new File(arrDelete.get(i));
            fileDele.delete();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (playMedia) {
            mMediaRecorder.reset();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean prepareVideoRecorder() {
        mMediaRecorder = new MediaRecorder();
        playMedia = true;
        ((MainActivity)getActivity()).cameraUnlock();
        ((MainActivity)getActivity()).setCam(mMediaRecorder);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P));

        path = CameraManager.getOutputMediaFile(CameraManager.MEDIA_TYPE_VIDEO).toString();

        mMediaRecorder.setOrientationHint(90);
        mMediaRecorder.setOutputFile(path);
        ((MainActivity)getActivity()).setPreViewDisplay(mMediaRecorder);
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.e("shhowh", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.e("shhowh", "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btRecorder:
                if (isRecorder){
                    mMediaRecorder.stop();
                    isRecorder = false;
                    playMedia = false;
                    mImvPauseRecorder.setVisibility(View.GONE);
                    mBTRecorder.setImageResource(R.drawable.bgcapturecamera);
                    mImvSaveVideo.setVisibility(View.VISIBLE);
                    mImvCancelVideo.setVisibility(View.VISIBLE);
                    mVvShowVideoRecorder.setVisibility(View.VISIBLE);
                    mImvPauseVideoRecorder.setVisibility(View.VISIBLE);
                    mImvPauseVideoRecorder.setImageResource(0);
                    mTvTimeRecorder.setVisibility(View.GONE);
                    mRlRecorder.setBackgroundColor(Color.BLACK);
                    mSbVideoRecorder.setVisibility(View.VISIBLE);
                    mVvShowVideoRecorder.setZOrderMediaOverlay(true);
                    mVvShowVideoRecorder.setVideoURI(Uri.parse(path));
                    mVvShowVideoRecorder.requestFocus();
                    mVvShowVideoRecorder.start();
                    handler.removeCallbacks(timeRecoder);
                    updateProgress();
                }else {
                    if (prepareVideoRecorder()){
                        mMediaRecorder.start();
                        mBTRecorder.setImageResource(R.drawable.bgcapturevideo);
                        mImvPauseRecorder.setVisibility(View.VISIBLE);
                        mTvTimeRecorder.setVisibility(View.VISIBLE);
                        mImvSwitchCamera.setVisibility(View.GONE);
                        mImvFlashInActive.setVisibility(View.GONE);
                        timeplus = 0;
                        updateTime();
                        isRecorder = true;
                    }else {
                        ((MainActivity)getActivity()).releaseMediaRecorder();
                    }
                }

                break;
            case R.id.imvPauseRecorder:
                if (isRecorder){
                    if (isPauseRecorder){
                        mMediaRecorder.resume();
                        mImvPauseRecorder.setImageResource(R.drawable.icsliderpointer);
                        isPauseRecorder = false;
                        timeplus = timePause;
                        updateTime();
                    }else {
                        mMediaRecorder.pause();
                        mImvPauseRecorder.setImageResource(R.drawable.icplay);
                        isPauseRecorder = true;
                        timePause = timeplus;
                        handler.removeCallbacks(timeRecoder);
                    }
                }
                break;
            case R.id.imvSwitchCameraR:
                ((MainActivity)getActivity()).switchCamera();
                break;
            case R.id.imvFlashInActiveR:
                switch (k){
                    case 0:
                        ((MainActivity)getActivity()).setFlashAuto();
                        mImvFlashInActive.setImageResource(R.drawable.icflashauto);
                        k =1;
                        break;
                    case 1:
                        ((MainActivity)getActivity()).setFlashActive();
                        mImvFlashInActive.setImageResource(R.drawable.icflashactive);
                        k = 2;
                        break;
                    case 2:
                        ((MainActivity)getActivity()).setFlashInActive();
                        mImvFlashInActive.setImageResource(R.drawable.icflashinactive);
                        k = 0;
                        break;
                }
                break;
            case R.id.imvCancelVideoR:
                mImvSaveVideo.setVisibility(View.GONE);
                mImvCancelVideo.setVisibility(View.GONE);
                mVvShowVideoRecorder.setVisibility(View.GONE);
                mImvPauseVideoRecorder.setVisibility(View.GONE);
                mRlRecorder.setBackgroundColor(Color.TRANSPARENT);
                mSbVideoRecorder.setVisibility(View.GONE);
                mImvSwitchCamera.setVisibility(View.VISIBLE);
                mImvFlashInActive.setVisibility(View.VISIBLE);
                arrDelete.add(path);
                break;
            case R.id.imvSaveVideoR:
                mImvSaveVideo.setVisibility(View.GONE);
                mImvCancelVideo.setVisibility(View.GONE);
                mImvPauseVideoRecorder.setVisibility(View.GONE);
                mVvShowVideoRecorder.setVisibility(View.GONE);
                mSbVideoRecorder.setVisibility(View.GONE);
                mImvSwitchCamera.setVisibility(View.VISIBLE);
                mImvFlashInActive.setVisibility(View.VISIBLE);
                mRlRecorder.setBackgroundColor(Color.TRANSPARENT);
                break;
            case R.id.imvPauseVideoRecorder:
                if (isClickVideo) {
                    mVvShowVideoRecorder.start();
                    mImvPauseVideoRecorder.setImageResource(0);
                    isClickVideo = false;
                }else {
                    mVvShowVideoRecorder.pause();
                    mImvPauseVideoRecorder.setImageResource(R.drawable.icplay);
                    isClickVideo = true;
                }
                break;
        }
    }

    public void releaseMediaRecorder() {
        if (mMediaRecorder != null){
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    public void updateProgress(){
        handler.postDelayed(updateTime, 100);
    }

    public void updateTime(){
        handler.postDelayed(timeRecoder, 100);
    }

    private Runnable updateTime = new Runnable() {
        @Override
        public void run() {
            mSbVideoRecorder.setProgress(mVvShowVideoRecorder.getCurrentPosition());
            mSbVideoRecorder.setMax(mVvShowVideoRecorder.getDuration());
            handler.postDelayed(this, 10);
        }
    };

    private Runnable timeRecoder = new Runnable() {
        @Override
        public void run() {
            timeplus++;
            int seconds = timeplus/60;
            int minutes = seconds/60;
            seconds = seconds%60;
            mTvTimeRecorder.setText(String.format("%d:%02d", minutes, seconds));
            handler.postDelayed(this, 10);
        }
    };

}
