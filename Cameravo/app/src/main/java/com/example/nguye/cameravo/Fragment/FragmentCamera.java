package com.example.nguye.cameravo.Fragment;

import android.graphics.Bitmap;
import android.hardware.Camera;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nguye.cameravo.Camera.CameraManager;
import com.example.nguye.cameravo.Camera.CameraPreview;
import com.example.nguye.cameravo.Activity.MainActivity;
import com.example.nguye.cameravo.Camera.RotateImage;
import com.example.nguye.cameravo.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FragmentCamera extends Fragment implements View.OnClickListener {
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout frameLayout;
    private ImageView mIMBCaputer;
    private ImageView imvShowPicture;
    private ImageView ibtSaveImage;
    private ImageView ibtDeleteImage;
    private String path;
    private ImageView mImvSwitchCamera;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentcamera, container, false);
        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        frameLayout = view.findViewById(R.id.cameraPreview);
        mIMBCaputer = view.findViewById(R.id.btCapture);
        imvShowPicture = view.findViewById(R.id.imvShowPicture);
        ibtDeleteImage = view.findViewById(R.id.ibtDeleteImage);
        ibtSaveImage = view.findViewById(R.id.ibtSaveImage);
        //mImvSwitchCamera = view.findViewById(R.id.imvSwitchCamera);

        mIMBCaputer.setOnClickListener(this);
        ibtSaveImage.setOnClickListener(this);
        ibtDeleteImage.setOnClickListener(this);
        //mImvSwitchCamera.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            File pictureFile = CameraManager.getOutputMediaFile(CameraManager.MEDIA_TYPE_IMAGE);

            if (pictureFile == null){
                Log.d("s", "Error creating media file, check storage permissions: " );
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(bytes);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("a", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("zz", "Error accessing file: " + e.getMessage());
            }

            path = pictureFile.getAbsolutePath();

            if (path != null){
                Glide.with(getActivity()).load(path).transform(new RotateImage(getContext(), 90))
                        .into(imvShowPicture);
            }

            if (path != null){
                imvShowPicture.setVisibility(View.VISIBLE);
                mIMBCaputer.setVisibility(View.GONE);
                ibtSaveImage.setVisibility(View.VISIBLE);
                ibtDeleteImage.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btCapture:
                mCamera.takePicture(null, null, mPicture);
                break;
            case R.id.ibtSaveImage:
                imvShowPicture.setVisibility(View.GONE);
                ibtSaveImage.setVisibility(View.GONE);
                ibtDeleteImage.setVisibility(View.GONE);
                mIMBCaputer.setVisibility(View.VISIBLE);
                break;
            case R.id.ibtDeleteImage:
                File file = new File(path);
                file.delete();
                imvShowPicture.setVisibility(View.GONE);
                ibtSaveImage.setVisibility(View.GONE);
                ibtDeleteImage.setVisibility(View.GONE);
                mIMBCaputer.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (mCamera == null){
                mCamera = CameraManager.getCameraInstance();
                mPreview = new CameraPreview(getActivity().getApplicationContext(), mCamera);
                frameLayout.addView(mPreview);
            }

        }else {
            if (mCamera != null) {
                mCamera.release();
                mCamera = null;
                Toast.makeText(getContext(), "release camera", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
