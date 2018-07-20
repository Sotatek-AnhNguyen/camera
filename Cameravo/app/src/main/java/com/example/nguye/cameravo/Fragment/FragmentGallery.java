package com.example.nguye.cameravo.Fragment;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.nguye.cameravo.Adapter.AdapterShowGallery;
import com.example.nguye.cameravo.Model.LinkImage;
import com.example.nguye.cameravo.R;

import java.util.ArrayList;

public class FragmentGallery extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private AdapterShowGallery mAdapterShowGallery;
    private GridView mGrvListImage;
    private ImageView imageView;
    private VideoView mVvVideoGallery;
    private SeekBar mSbVideoGallery;
    private Handler handler;
    private ArrayList<LinkImage> arrData;
    private boolean isClickVideo;
    private ImageView mImvPlayVideoGallery;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentgallery, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler = new Handler();
        mGrvListImage = view.findViewById(R.id.gvListImage);
        mVvVideoGallery = view.findViewById(R.id.vvVideoGallery);
        imageView = view.findViewById(R.id.imvShowImage);
        mSbVideoGallery = view.findViewById(R.id.sbVideoGallery);
        mImvPlayVideoGallery = view.findViewById(R.id.imvPlayVideoGallery);

        arrData = getImagePath(getActivity());
        arrData.get(0).setClick(true);
        Glide.with(getContext()).load(arrData.get(0).getPath()).into(imageView);
        mAdapterShowGallery = new AdapterShowGallery(arrData, getContext());
        mGrvListImage.setAdapter(mAdapterShowGallery);
        mGrvListImage.setOnItemClickListener(this);
        mImvPlayVideoGallery.setOnClickListener(this);
    }

    public ArrayList<LinkImage> getImagePath(Activity activity){
        Uri uriImage;
        Cursor cursor;
        int mColumIndexData;
        String mPathImage = null;
        String mPathVideo = null;
        ArrayList<LinkImage> arrImagePath = new ArrayList<>();
        uriImage = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri uriVideo = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String [] projection = {MediaStore.MediaColumns.DATA};

        cursor = activity.getContentResolver().query(uriImage, projection, null, null, null);
        Cursor cursoVideo = activity.getContentResolver().query(uriVideo, projection, null, null, null);

        mColumIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        int mColumVideo = cursoVideo.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()){
            mPathImage = cursor.getString(mColumIndexData);
            LinkImage linkImage = new LinkImage(mPathImage, false, true);
            arrImagePath.add(linkImage);
        }

        while (cursoVideo.moveToNext()){
            mPathVideo = cursoVideo.getString(mColumVideo);
            LinkImage image = new LinkImage(mPathVideo, false, false);
            arrImagePath.add(image);
        }

        return arrImagePath;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mVvVideoGallery.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        mSbVideoGallery.setVisibility(View.GONE);
        mImvPlayVideoGallery.setImageResource(0);
        mImvPlayVideoGallery.setVisibility(View.GONE);
        if (arrData.get(i).isImage()) {
            imageView.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(arrData.get(i).getPath()).into(imageView);
        }else {
            mSbVideoGallery.setVisibility(View.VISIBLE);
            mImvPlayVideoGallery.setVisibility(View.VISIBLE);
            mVvVideoGallery.setVisibility(View.VISIBLE);
            mVvVideoGallery.setZOrderMediaOverlay(true);
            mVvVideoGallery.setVideoURI(Uri.parse(arrData.get(i).getPath()));
            mVvVideoGallery.start();
            handler.postDelayed(runTime, 100);
        }
        for (int k =0; k < arrData.size(); k++){
            arrData.get(k).setClick(false);
        }
        arrData.get(i).setClick(true);
        mAdapterShowGallery.notifyDataSetChanged();
    }

    private Runnable runTime = new Runnable() {
        @Override
        public void run() {
            mSbVideoGallery.setProgress(mVvVideoGallery.getCurrentPosition());
            mSbVideoGallery.setMax(mVvVideoGallery.getDuration());
            handler.postDelayed(this, 100);
        }
    };

    @Override
    public void onClick(View view) {
        if (isClickVideo) {
            mVvVideoGallery.start();
            mImvPlayVideoGallery.setImageResource(0);
            isClickVideo = false;
        }else {
            mVvVideoGallery.pause();
            mImvPlayVideoGallery.setImageResource(R.drawable.icplay);
            isClickVideo = true;
        }
    }
}
