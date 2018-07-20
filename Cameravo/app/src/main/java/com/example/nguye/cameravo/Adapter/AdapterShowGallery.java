package com.example.nguye.cameravo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.nguye.cameravo.Model.LinkImage;
import com.example.nguye.cameravo.R;

import java.util.ArrayList;

public class AdapterShowGallery extends BaseAdapter {
    private ArrayList<LinkImage> arrDataImage;
    private LayoutInflater inflater;
    private Context context;

    public AdapterShowGallery(ArrayList<LinkImage> arrDataImage, Context context) {
        this.arrDataImage = arrDataImage;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrDataImage.size();
    }

    @Override
    public Object getItem(int i) {
        return arrDataImage.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        viewHolder viewHolder;
        if (view == null){
            viewHolder = new viewHolder();
            view = inflater.inflate(R.layout.activityitemgallery, viewGroup, false);
            viewHolder.mImvItemImage = view.findViewById(R.id.imvItemImage);
            viewHolder.mImvItemClickImage = view.findViewById(R.id.imvItemClickImage);
            viewHolder.mImvItemIsImage = view.findViewById(R.id.imvItemIsImage);
            viewHolder.mRlBackground = view.findViewById(R.id.imvBackgroundImage);
            view.setTag(viewHolder);
        }else {
            viewHolder = (AdapterShowGallery.viewHolder) view.getTag();
        }

        Glide.with(context).load(arrDataImage.get(i).getPath()).into(viewHolder.mImvItemImage);
        if (arrDataImage.get(i).isClick()){
            viewHolder.mImvItemClickImage.setVisibility(View.VISIBLE);
            viewHolder.mRlBackground.setVisibility(View.VISIBLE);
        }else {
            viewHolder.mImvItemClickImage.setVisibility(View.GONE);
            viewHolder.mRlBackground.setVisibility(View.GONE);
        }
        if (arrDataImage.get(i).isImage()){
            viewHolder.mImvItemIsImage.setVisibility(View.GONE);
        }else {
            viewHolder.mImvItemIsImage.setVisibility(View.VISIBLE);
        }

        return view;
    }

    class viewHolder{
        ImageView mImvItemImage;
        ImageView mImvItemClickImage;
        ImageView mImvItemIsImage;
        ImageView mRlBackground;
    }

}
