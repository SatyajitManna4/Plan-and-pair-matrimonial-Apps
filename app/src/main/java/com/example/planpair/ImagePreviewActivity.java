package com.example.planpair;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ImagePreviewActivity extends AppCompatActivity {

    ImageView fullImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        fullImageView = findViewById(R.id.fullImageView);

        int imageResId = getIntent().getIntExtra("imageResId", 0);
        if (imageResId != 0) {
            fullImageView.setImageResource(imageResId);
        }
    }
}
