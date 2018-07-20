package com.example.nguye.cameravo.Camera;

import android.app.Activity;
import android.hardware.Camera;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Surface;

import java.io.File;
import java.util.Date;

public class CameraManager {
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    public static Camera getCameraInstance(int currentCameraId){
        Camera camera = null;
        try {
            camera = Camera.open(currentCameraId);
        }
        catch (Exception e){
        }
        return camera;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static File getOutputMediaFile(int type){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("Camera", "failed to create directory");
                return null;
            }
        }


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static void setCameraDisplayOrientation(Activity activity, int mCameaId, Camera camera){
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameaId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degress = 0;
        switch (rotation){
            case Surface.ROTATION_0:
                degress = 0;
                break;
            case Surface.ROTATION_90:
                degress =90;
                break;
            case Surface.ROTATION_180:
                degress = 180;
                break;
            case Surface.ROTATION_270:
                degress = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT){
            result = (info.orientation + degress) % 360;
            result = (360 - result)%360;
        }else {
            result = (info.orientation - degress + 360) % 360;
        }

        camera.setDisplayOrientation(result);
    }
}
