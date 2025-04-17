package com.example.hinduja_health_first;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameView;


    public MyViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.Imageview);
        nameView = itemView.findViewById(R.id.name);


    }
}
