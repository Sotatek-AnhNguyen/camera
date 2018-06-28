package com.example.nguye.cameravo.Fragment;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.nguye.cameravo.Camera.CameraManager;
import com.example.nguye.cameravo.Camera.CameraPreview;
import com.example.nguye.cameravo.Activity.MainActivity;
import com.example.nguye.cameravo.R;

import java.io.IOException;

public class FragmentRecorder extends Fragment implements View.OnClickListener {
    private Camera mCameraR;
    private MediaRecorder mMediaRecorder;
    private CameraPreview mCameraPreviewR;
    private ImageView mBTRecorder;
    private boolean isRecorder;
    private FrameLayout frameLayout;
    private VideoView mVVVideoRecorder;
    private String path;
    private ImageView mImvPauseRecorder;
    private ImageView mImvSaveVideo;
    private ImageView mImvCancelVideo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentrecorder, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isRecorder = false;
        mBTRecorder = view.findViewById(R.id.btRecorder);
        mCameraR = CameraManager.getCameraInstance();
        mCameraPreviewR = new CameraPreview(getActivity().getApplicationContext(), mCameraR);
        frameLayout = view.findViewById(R.id.camRecorderPreview);
        frameLayout.addView(mCameraPreviewR);
        mVVVideoRecorder = view.findViewById(R.id.vvVideoRecorder);
        mImvPauseRecorder = view.findViewById(R.id.imvPauseRecorder);
        mImvSaveVideo = view.findViewById(R.id.imvSaveVideo);
        mImvCancelVideo = view.findViewById(R.id.imvCancelVideo);
        mBTRecorder.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean prepareVideoRecorder() {

        mMediaRecorder = new MediaRecorder();
        mCameraR.unlock();
        mMediaRecorder.setCamera(mCameraR);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_1080P));

        path = CameraManager.getOutputMediaFile(CameraManager.MEDIA_TYPE_VIDEO).toString();
        mMediaRecorder.setOutputFile(path);

        mMediaRecorder.setPreviewDisplay(mCameraPreviewR.getHolder().getSurface());

        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
       // releaseMediaRecorder();
        mCameraR.release();
        mCameraR = null;

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btRecorder:
                if (isRecorder){
                    frameLayout.setVisibility(View.GONE);
                    mBTRecorder.setVisibility(View.GONE);
                    mMediaRecorder.stop();
                    releaseMediaRecorder();
                    mCameraR.lock();
                    isRecorder = false;
                    mVVVideoRecorder.setVisibility(View.VISIBLE);
                    mVVVideoRecorder.setMediaController(new MediaController(getContext()));
                    mVVVideoRecorder.setVideoURI(Uri.parse(path));
                    mVVVideoRecorder.start();
                }else {
                    if (prepareVideoRecorder()){
                        mMediaRecorder.start();
                        isRecorder = true;

                    }else {
                        releaseMediaRecorder();
                    }
                }
                break;
            case R.id.imvPauseRecorder:

                break;
            case R.id.imvSaveVideo:
                break;
            case R.id.imvCancelVideo:
                break;
        }
    }

    public void releaseMediaRecorder() {
        if (mMediaRecorder != null){
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCameraR.lock();
        }
    }

    private void releaseCamera(){
        if (mCameraR != null){
            mCameraR.release();
            mCameraR = null;
        }
    }
}
