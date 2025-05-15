package com.example.hinduja_health_first.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hinduja_health_first.R;
import com.example.hinduja_health_first.models.HealthBlog;

import java.util.List;

public class HealthBlogAdapter extends RecyclerView.Adapter<HealthBlogAdapter.BlogViewHolder> {
    private List<HealthBlog> blogList;
    private OnBlogClickListener listener;

    public interface OnBlogClickListener {
        void onBlogClick(HealthBlog blog);
    }

    public HealthBlogAdapter(List<HealthBlog> blogList, OnBlogClickListener listener) {
        this.blogList = blogList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_health_blog, parent, false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlogViewHolder holder, int position) {
        HealthBlog blog = blogList.get(position);
        holder.titleTextView.setText(blog.getTitle());
        holder.itemView.setOnClickListener(v -> listener.onBlogClick(blog));
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    static class BlogViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        BlogViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.blogTitle);
        }
    }
} 