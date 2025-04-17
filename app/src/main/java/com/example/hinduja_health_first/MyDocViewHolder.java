package com.example.hinduja_health_first;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyDocViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameView;
    TextView specialtyView;
    TextView experienceView;

    public MyDocViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.docimageview);
        nameView = itemView.findViewById(R.id.docname);
        specialtyView = itemView.findViewById(R.id.docSpecs);
        experienceView = itemView.findViewById(R.id.docExp);
    }

}
