package com.example.nguye.cameravo.Fragment;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.example.nguye.cameravo.Adapter.AdapterShowGallery;
import com.example.nguye.cameravo.R;

import java.util.ArrayList;

public class FragmentGallery extends Fragment {
    private RecyclerView mRcvListImage;
    private AdapterShowGallery mAdapterShowGallery;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentgallery, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRcvListImage = view.findViewById(R.id.rcvListImage);
        mAdapterShowGallery = new AdapterShowGallery(getImagePath(getActivity()), getContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRcvListImage.setAdapter(mAdapterShowGallery);
        mRcvListImage.setLayoutManager(mLayoutManager);
    }

    public ArrayList<String> getImagePath(Activity activity){
        Uri uriImage;
        Cursor cursor;
        int mColumIndexData;
        String mPathImage = null;
        ArrayList<String> arrImagePath = new ArrayList<>();
        uriImage = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String [] projection = {MediaStore.MediaColumns.DATA};

        cursor = activity.getContentResolver().query(uriImage, projection, null, null, null);

        mColumIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()){
            mPathImage = cursor.getString(mColumIndexData);
            arrImagePath.add(mPathImage);
        }

        return arrImagePath;
    }
}
