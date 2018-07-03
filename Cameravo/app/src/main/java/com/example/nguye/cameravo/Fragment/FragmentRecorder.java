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
import android.widget.VideoView;

import com.example.nguye.cameravo.Camera.CameraManager;
import com.example.nguye.cameravo.Camera.CameraPreview;
import com.example.nguye.cameravo.R;

import java.io.File;
import java.io.IOException;

public class FragmentRecorder extends Fragment implements View.OnClickListener {
    private Camera mCameraR;
    private MediaRecorder mMediaRecorder;
    private CameraPreview mCameraPreviewR;
    private ImageView mBTRecorder;
    private boolean isRecorder;
    private boolean isPauseRecorder;
    private FrameLayout frameLayout;
    private VideoView mVVVideoRecorder;
    private String path;
    private ImageView mImvPauseRecorder;
    private ImageView mImvSaveVideo;
    private ImageView mImvCancelVideo;
    private ImageView mImvSwitchCamera;

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
        isPauseRecorder = false;
        mBTRecorder = view.findViewById(R.id.btRecorder);
        frameLayout = view.findViewById(R.id.camRecorderPreview);

        mVVVideoRecorder = view.findViewById(R.id.vvVideoRecorder);
        mImvPauseRecorder = view.findViewById(R.id.imvPauseRecorder);
        mImvSaveVideo = view.findViewById(R.id.imvSaveVideo);
        mImvCancelVideo = view.findViewById(R.id.imvCancelVideo);
        mImvSwitchCamera = view.findViewById(R.id.imvSwitchCamera);
        mBTRecorder.setOnClickListener(this);
        mImvPauseRecorder.setOnClickListener(this);
        mImvSaveVideo.setOnClickListener(this);
        mImvCancelVideo.setOnClickListener(this);
        mImvSwitchCamera.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean prepareVideoRecorder() {

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (mCameraR == null){
                mCameraR = CameraManager.getCameraInstance();
                mMediaRecorder = new MediaRecorder();
                mCameraPreviewR = new CameraPreview(getActivity().getApplicationContext(), mCameraR);
                frameLayout.addView(mCameraPreviewR);
            }
        }else {
            if (mMediaRecorder != null){
                mMediaRecorder.reset();
                mMediaRecorder.release();
                mMediaRecorder = null;
                mCameraR.lock();
            }

            if (mCameraR != null) {
                mCameraR.release();
                mCameraR = null;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
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
                    mImvSaveVideo.setVisibility(View.VISIBLE);
                    mImvCancelVideo.setVisibility(View.VISIBLE);
                    mImvPauseRecorder.setVisibility(View.GONE);
                    mVVVideoRecorder.setVisibility(View.VISIBLE);
                    mVVVideoRecorder.setMediaController(new MediaController(getContext()));
                    mVVVideoRecorder.setVideoURI(Uri.parse(path));
                    mVVVideoRecorder.start();
                }else {
                    if (prepareVideoRecorder()){
                        mMediaRecorder.start();
                        mBTRecorder.setImageResource(R.drawable.bgcapturecancel);
                        mImvPauseRecorder.setVisibility(View.VISIBLE);
                        isRecorder = true;

                    }else {
                        releaseMediaRecorder();
                    }
                }
                break;
            case R.id.imvPauseRecorder:
                if (isRecorder){
                    if (isPauseRecorder){
                        mMediaRecorder.resume();
                        mImvPauseRecorder.setImageResource(R.drawable.icsliderpointer);
                        isPauseRecorder = false;
                    }else {
                        mMediaRecorder.pause();
                        mImvPauseRecorder.setImageResource(R.drawable.icplay);
                        isPauseRecorder = true;
                    }
                }
                break;
            case R.id.imvSaveVideo:
                mVVVideoRecorder.setVisibility(View.GONE);
                mImvSaveVideo.setVisibility(View.GONE);
                mImvCancelVideo.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                mBTRecorder.setVisibility(View.VISIBLE);
                mImvPauseRecorder.setVisibility(View.GONE);
                mBTRecorder.setImageResource(R.drawable.bgcapturevideo);
                break;
            case R.id.imvCancelVideo:
                File file = new File(path);
                file.delete();
                mVVVideoRecorder.setVisibility(View.GONE);
                mImvSaveVideo.setVisibility(View.GONE);
                mImvCancelVideo.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                mBTRecorder.setVisibility(View.VISIBLE);
                mImvPauseRecorder.setVisibility(View.GONE);
                mBTRecorder.setImageResource(R.drawable.bgcapturevideo);
                break;
            case R.id.imvSwitchCamera:
                if (mCameraR != null){
                    mCameraR.stopPreview();
                    mCameraR.release();
                }

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
}
