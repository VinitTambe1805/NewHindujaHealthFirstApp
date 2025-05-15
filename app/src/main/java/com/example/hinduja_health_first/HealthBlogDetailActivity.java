package com.example.hinduja_health_first;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.example.hinduja_health_first.models.HealthBlog;

public class HealthBlogDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_blog_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Health Blog");
        toolbar.setNavigationOnClickListener(v -> finish());

        HealthBlog blog = (HealthBlog) getIntent().getSerializableExtra("blog");
        if (blog == null) {
            finish();
            return;
        }

        TextView title = findViewById(R.id.blogDetailTitle);
        TextView description = findViewById(R.id.blogDetailDescription);
        TextView source = findViewById(R.id.blogDetailSource);
        TextView date = findViewById(R.id.blogDetailDate);
        ImageView image = findViewById(R.id.blogDetailImage);

        title.setText(blog.getTitle());
        description.setText(blog.getDescription());
        source.setText("Source: " + blog.getSource());
        date.setText(blog.getPublishedAt());
        Glide.with(this)
                .load(blog.getImageUrl())
                .placeholder(R.color.grey)
                .error(R.color.primary_light)
                .into(image);
    }
} 