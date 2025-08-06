package com.example.planpair;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpair.adapters.PhotographerAdapter;
import com.example.planpair.models.Photographer;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhotographerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PhotographerAdapter adapter;
    private List<Photographer> allPhotographers;
    private List<Photographer> filteredList;
    private EditText searchPhotographer;
    private ChipGroup mainFilterChipGroup, subFilterChipGroup;
    private Chip chipAll, chipBudget, chipRating, chipType;
    private TextView noResultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        mainFilterChipGroup = findViewById(R.id.mainFilterChipGroup);
        subFilterChipGroup = findViewById(R.id.subFilterChipGroup);
        chipAll = findViewById(R.id.chipAll);
        chipBudget = findViewById(R.id.chipBudget);
        chipRating = findViewById(R.id.chipRating);
        chipType = findViewById(R.id.chipType);
        noResultText = findViewById(R.id.noResultText);
        searchPhotographer = findViewById(R.id.searchAutoTextView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Integer> sourav_gallery = Arrays.asList(
                R.drawable.sourav_gallary1, R.drawable.sourav_gallary2, R.drawable.sourav_gallary3,R.drawable.sourav_gallary4,
                R.drawable.sourav_gallary5,R.drawable.sourav_gallary6,R.drawable.sourav_gallary7
        );
        List<Integer> rig_gallery = Arrays.asList(
                R.drawable.rig_photography1, R.drawable.rig_photography2, R.drawable.rig_photography3,R.drawable.rig_photography4,
                R.drawable.rig_photography5,R.drawable.rig_photography6
        );
        List<Integer> sparkling_gallery = Arrays.asList(
                R.drawable.sparkling1, R.drawable.sparkling2, R.drawable.sparkling3,R.drawable.sparkling4
        );
        List<Integer> dariya_gallery = Arrays.asList(
                R.drawable.daria_events1, R.drawable.daria_events2, R.drawable.daria_events3,R.drawable.daria_events4,
                R.drawable.daria_events5,R.drawable.daria_events6
        );
        List<Integer> jeet_gallery = Arrays.asList(
                R.drawable.jeet_photography1, R.drawable.jeet_photography2, R.drawable.jeet_photography3,R.drawable.jeet_photography4,
                R.drawable.jeet_photography5,R.drawable.jeet_photography6
        );
        List<Integer> frame_shadow_gallery = Arrays.asList(
                R.drawable.frame_shadow1, R.drawable.frame_shadow2, R.drawable.frame_shadow3,R.drawable.frame_shadow4,
                R.drawable.frame_shadow5,R.drawable.frame_shadow6
        );
        List<Integer> wpc_gallery = Arrays.asList(
                R.drawable.photo_creators1, R.drawable.photo_creators2, R.drawable.photo_creators3,R.drawable.photo_creators4,
                R.drawable.photo_creators5,R.drawable.photo_creators6,R.drawable.photo_creators7
        );
        List<Integer> memories_designer_gallery = Arrays.asList(
                R.drawable.memories_designer1, R.drawable.memories_designer2, R.drawable.memories_designer3,R.drawable.memories_designer4,
                R.drawable.memories_designer5,R.drawable.memories_designer6,R.drawable.memories_designer7
        );
        List<Integer> charcoal_gallery = Arrays.asList(
                R.drawable.charcol_photography1, R.drawable.charcol_photography2, R.drawable.charcol_photography3,R.drawable.charcol_photography4,
                R.drawable.charcol_photography5,R.drawable.charcol_photography6
        );

        // Sample data
        allPhotographers = new ArrayList<>();
        allPhotographers.add(new Photographer("souravgal","Sourav Sen Galleries",
                "Birati, Kolkata", R.drawable.sourav_gallary1,
                "Candid, Traditional, Cinematography",
                4.9,
                280,
                35000,
                50000,
                "Proofs in 3 weeks; Final album in 6 weeks",
                "25%",
                "Outstation travel and stay charges borne by client",
                "8+ years",
                "Clients commend the team for their professionalism, punctuality, and ability to make subjects feel comfortable.",
                sourav_gallery));
        allPhotographers.add(new Photographer( "rigphoto","Rig Photography",
                "Barasat, Kolkata",
                R.drawable.rig_photography3,
                "Bengali Wedding Photography & Videography",
                4.8,
                428,
                35000,
                60000,
                "Up to 40 days",
                "50%",
                "Outstation travel and stay charges borne by client",
                "7 years",
                "Known for budget-friendly packages without compromising quality",rig_gallery));
        allPhotographers.add(new Photographer( "sparkling","The Sparkling Wedding",
                "Shyam Bazar, Kolkata",
                R.drawable.sparkling3,
                "Wedding Photography and Cinematography",
                4.8,
                220,
                30000,
                40000,
                "6 weeks",
                "50%",
                "Outstation travel and stay charges borne by client",
                "4 years",
                "Appreciated for their friendly approach and creative storytelling.",
                sparkling_gallery));
        allPhotographers.add(new Photographer( "dariya","Dariya Events",
                "Rajarhat New Town, Kolkata",
                R.drawable.daria_events1,
                "Wedding Photography and Cinematography",
                4.0,
                28,
                20000,
                30000,
                "6 weeks",
                "50%",
                "Outstation travel and stay charges borne by client",
                "6 years",
                "Recognized for capturing vibrant and lively wedding moments. ",dariya_gallery));
        allPhotographers.add(new Photographer( "jeetphoto","Jeet Biswas Photography",
                "Tollygunge, Kolkata",
                R.drawable.jeet_photography1,
                "Wedding Photography and Videography",
                4.9,
                335,
                40000,
                50000,
                "6 weeks",
                "25%",
                "Outstation stay borne by client; travel cost included in package",
                "5+ years",
                "Known for capturing the essence of weddings with creativity.",jeet_gallery));
        allPhotographers.add(new Photographer( "frameshadow","FRAME SHADOW PHOTOGRAPHY",
                "Tollygunge, Kolkata",
                R.drawable.frame_shadow2,
                "Wedding Photography and Videography",
                4.9,
                335,
                40000,
                50000,
                "6 weeks",
                "50%",
                "Outstation travel and stay charges borne by client",
                "4+ years",
                "Offers exclusive discounts for WeddingWire.in couples.",frame_shadow_gallery));
        allPhotographers.add(new Photographer( "photocreaters","Wedding Photo Creators",
                "23/15 D.P.P Lane, Kolkata",
                R.drawable.photo_creators3,
                "Wedding Photography and Cinematography",
                4.5,
                216,
                15000,
                35000,
                "6 weeks",
                "50%",
                " Outstation travel and stay charges borne by client",
                "8+ years",
                "Known for their cooperative nature and timely delivery.",wpc_gallery));
        allPhotographers.add(new Photographer("memoriesDizn" ,"Memories Designer",
                "Naora, Shibpur, Howrah",
                R.drawable.memories_designer5,
                "Wedding Photography and Videography",
                4.8,
                103,
                50000,
                60000,
                "6 weeks",
                "50%",
                "Outstation travel and stay charges borne by client",
                "8+ years",
                "Celebrated for artistic storytelling and attention to detail.",memories_designer_gallery));
        allPhotographers.add(new Photographer("charcoal", "Charcoal & Vermillion",
                "Kalikapur, Haltu, Kolkata",
                R.drawable.charcol_photography2,
                "Wedding Photography and Videography",
                4.9,
                110,
                60000,
                80000,
                "6 weeks",
                "50%",
                "Outstation travel and stay charges are borne by the client.",
                "5+ years",
                "Renowned for capturing candid and genuine moments during weddings.\n" +
                        "\n" +
                        "Recognized with Silver & Bronze awards at the International Photography Awards (IPA 2020) and Tokyo International Foto Awards 2020.",charcoal_gallery));

        filteredList = new ArrayList<>(allPhotographers);
        adapter = new PhotographerAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);

        // Search
        searchPhotographer.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPhotography(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        setupMainChipListeners();
    }

    private void filterPhotography(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(allPhotographers);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Photographer photographer : allPhotographers) {
                if (photographer.getName().toLowerCase().contains(lowerCaseQuery) ||
                        photographer.getCity().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(photographer);
                }
            }
        }

        adapter.notifyDataSetChanged();
        noResultText.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void setupMainChipListeners() {
        mainFilterChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            subFilterChipGroup.removeAllViews();
            subFilterChipGroup.setVisibility(View.GONE);

            if (checkedId == R.id.chipAll) {
                filteredList.clear();
                filteredList.addAll(allPhotographers);
                adapter.notifyDataSetChanged();
                noResultText.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
            } else if (checkedId == R.id.chipBudget) {
                showSubFilters(new String[]{"Below ₹40,000", "₹40,000 - ₹60,000", "Above ₹60,000"}, "budget");
            } else if (checkedId == R.id.chipRating) {
                showSubFilters(new String[]{"4.0 - 4.5", "4.5+", "5.0"}, "rating");
            } else if (checkedId == R.id.chipType) {
                showSubFilters(new String[]{"Photography", "Videography", "Cinematography"}, "type");
            }
        });
    }

    private void showSubFilters(String[] filters, String filterType) {
        subFilterChipGroup.setVisibility(View.VISIBLE);
        subFilterChipGroup.removeAllViews();  // clear previous chips

        for (String label : filters) {
            Chip chip = new Chip(this);
            chip.setText(label);
            chip.setCheckable(true);
            chip.setClickable(true);
            chip.setChipBackgroundColorResource(R.color.white);
            chip.setTextColor(getResources().getColor(R.color.black));

            chip.setOnClickListener(v -> {
                // Uncheck all other chips
                for (int i = 0; i < subFilterChipGroup.getChildCount(); i++) {
                    Chip otherChip = (Chip) subFilterChipGroup.getChildAt(i);
                    otherChip.setChecked(false);
                }
                chip.setChecked(true);
                applySubFilter(label, filterType);
            });

            subFilterChipGroup.addView(chip);
        }
    }

    private void applySubFilter(String value, String filterType) {
        filteredList.clear();

        for (Photographer p : allPhotographers) {
            switch (filterType) {
                case "budget":
                    if (value.equals("Below ₹40,000") && p.getPvPrice()< 40000) {
                        filteredList.add(p);
                    } else if (value.equals("₹40,000 - ₹60,000") && p.getPvPrice() >= 40000 && p.getPvPrice() <= 60000) {
                        filteredList.add(p);
                    } else if (value.equals("Above ₹60,000") && p.getPvPrice() > 60000) {
                        filteredList.add(p);
                    }
                    break;

                case "rating":
                    double rating = p.getRating();
                    if (value.equals("4.0 - 4.5") && rating >= 4.0 && rating <= 4.5) {
                        filteredList.add(p);
                    } else if (value.equals("4.5+") && rating > 4.5) {
                        filteredList.add(p);
                    } else if (value.equals("5.0") && rating == 5.0) {
                        filteredList.add(p);
                    }
                    break;

                case "type":
                    if (p.getService().toLowerCase().contains(value.toLowerCase())) {
                        filteredList.add(p);
                    }
                    break;
            }
        }

        adapter.notifyDataSetChanged();
        noResultText.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
