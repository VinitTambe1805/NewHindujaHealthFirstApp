package com.example.hinduja_health_first;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyDocAdapter extends RecyclerView.Adapter<MyDocViewHolder> {


    Context context;

    List<Doc_item> items;

    public MyDocAdapter(Context context, List<Doc_item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyDocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyDocViewHolder(LayoutInflater.from(context).inflate(R.layout.doc_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyDocViewHolder holder, int position) {
        holder.nameView.setText(items.get(position).getName());
        holder.specialtyView.setText(items.get(position).getSpecialty());
        holder.experienceView.setText(items.get(position).getExperience());
        holder.imageView.setImageResource(items.get(position).getImage());



    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
