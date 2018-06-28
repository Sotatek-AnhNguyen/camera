package com.example.nguye.cameravo.Camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

public class RotateImage extends BitmapTransformation {
    private int mOriention;

    public RotateImage(Context context, int oriention) {
        super(context);
        this.mOriention = oriention;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Matrix matrix = new Matrix();
        matrix.postRotate(mOriention);
        return Bitmap.createBitmap(toTransform, 0,0 , toTransform.getWidth(), toTransform.getHeight(), matrix, true);
    }

    @Override
    public String getId() {
        return "rotate" + mOriention;
    }
}
