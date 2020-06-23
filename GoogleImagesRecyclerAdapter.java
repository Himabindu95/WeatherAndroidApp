package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class GoogleImagesRecyclerAdapter extends RecyclerView.Adapter<GoogleImagesRecyclerAdapter.ViewHolder> {

    private Context context;
    private final String[] googleimages;

    public GoogleImagesRecyclerAdapter (Context context, String[] googleimages) {
        this.context = context;
        this.googleimages = googleimages;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView itemImage;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (ImageView)itemView.findViewById(R.id.item_image);
        }
    }

    @NonNull
    @Override
    public GoogleImagesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.googlephotos_card_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GoogleImagesRecyclerAdapter.ViewHolder holder, int position) {
        if(googleimages[position]!=null && !googleimages[position].isEmpty()){
          Picasso.with(context).load(googleimages[position]).resize(400,300).into(holder.itemImage);
      }
    }

    @Override
    public int getItemCount() {
        return 8;
    }
}