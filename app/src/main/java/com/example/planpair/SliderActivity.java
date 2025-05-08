package com.example.planpair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.planpair.adapters.SliderAdapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

public class SliderActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private DotsIndicator dotsIndicator;
    private SliderAdapter sliderAdapter;
    private List<SliderItem> sliderItems;
    private Button btnStartJourney;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Set status bar color to transparent for a better visual effect.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);

        // Initialize Views
        viewPager = findViewById(R.id.viewPager);
        dotsIndicator = findViewById(R.id.dotsIndicator);
        btnStartJourney = findViewById(R.id.btnStartJourney); // ✅ Now this will not be null

        // Initialize slider items
        sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.slider_img1, "Welcome to Plan & Pair\nYour one-stop destination for finding your perfect match and planning your dream wedding — all in one place."));
        sliderItems.add(new SliderItem(R.drawable.slider_img2, "Unlock Premium for Broader Matches\nTell us about yourself, your preferences, and what you're looking for — with premium, access exclusive filters, enhanced visibility, and connect with a wider range of potential matches!"));
        sliderItems.add(new SliderItem(R.drawable.slider_img3, "Premium Wedding Planning\nFrom venues to vendors, Plan & Pair helps you organize every detail of your big day with ease. Upgrade to premium for personalized vendor recommendations and priority access to planning tools!"));
        sliderItems.add(new SliderItem(R.drawable.slider_img4, "Designed for Modern Traditions\nWhether you're into timeless customs or contemporary celebrations, we blend both for a unique experience."));
        sliderItems.add(new SliderItem(R.drawable.slider_img5, "Start Your Journey with Us\nFind love. Plan beautifully. Make it unforgettable — only with Plan & Pair."));

        // Set up adapter and bind it to the ViewPager
        sliderAdapter = new SliderAdapter(sliderItems);
        viewPager.setAdapter(sliderAdapter);

        // Bind DotsIndicator to ViewPager2
        dotsIndicator.setViewPager2(viewPager);

        // Show button only on the last slide
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                btnStartJourney.setVisibility(position == sliderItems.size() - 1 ? View.VISIBLE : View.GONE);
            }
        });

        // Navigate to LoginActivity when button is clicked
        btnStartJourney.setOnClickListener(v -> {
            Intent intent = new Intent(SliderActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }
}