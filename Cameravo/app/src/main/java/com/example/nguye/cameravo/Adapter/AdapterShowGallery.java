package com.example.nguye.cameravo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.nguye.cameravo.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class AdapterShowGallery extends RecyclerView.Adapter<AdapterShowGallery.viewHolder> {
    private ArrayList<String> arrDataImage;
    private LayoutInflater inflater;
    private Context context;

    public AdapterShowGallery(ArrayList<String> arrDataImage, Context context) {
        this.arrDataImage = arrDataImage;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.activityitemgallery, parent, false);
        viewHolder viewHolder = new viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        Glide.with(context).load(arrDataImage.get(position)).into(holder.mImvItemImage);
    }

    @Override
    public int getItemCount() {
        return arrDataImage.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        ImageView mImvItemImage;
        ImageView mImvItemImage1;
        ImageView mImvItemImage2;

        public viewHolder(View itemView) {
            super(itemView);
            mImvItemImage = itemView.findViewById(R.id.imvItemImage);
            mImvItemImage1 = itemView.findViewById(R.id.imvItemImage1);
            mImvItemImage2 = itemView.findViewById(R.id.imvItemImage2);
        }
    }
}
