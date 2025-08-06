package com.example.planpair;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.planpair.adapters.VenueAdapter;
import com.example.planpair.models.Venue;

public class VenueActivity extends AppCompatActivity {

    private TextView noResultText;
    private EditText searchVenueEditText;
    private RecyclerView recyclerView;
    private VenueAdapter venueAdapter;
    private List<Venue> venueList;
    private List<Venue> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        searchVenueEditText = findViewById(R.id.smsAutoTextView);
        recyclerView = findViewById(R.id.recyclerView);
        ImageView filterIcon = findViewById(R.id.filterButton);// Add a filter icon in your layout
        noResultText = findViewById(R.id.noResultText);
        filterIcon.setOnClickListener(v -> showFilterDialog());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Integer> galleriaPhotos = Arrays.asList(
                R.drawable.galleria1, R.drawable.galleria2, R.drawable.galleria3
        );
        List<Integer> kathakaliPhotos = Arrays.asList(
                R.drawable.kathakali_banquet1, R.drawable.kathakali_banquet2
        );
        List<Integer> shubhamBanquetPhotos = Arrays.asList(
                R.drawable.subham_banquet1, R.drawable.subham_banquet2
        );
        List<Integer> royalPalacePhotos = Arrays.asList(
                R.drawable.royal_banquet_hall1, R.drawable.royal_banquet_hall2,R.drawable.royal_banquet_hall3,
                R.drawable.royal_banquet_hall4,R.drawable.royal_banquet_hall5
        );
        List<Integer> bikaPhotos = Arrays.asList(
                R.drawable.bika_banquet1, R.drawable.bika_banquet2
        );
        List<Integer> shreeBanquetPhotos = Arrays.asList(
                R.drawable.shree_banquet1, R.drawable.shree_banquet2, R.drawable.shree_banquet3,
                R.drawable.shree_banquet4
        );
        List<Integer> orchidBanquetPhotos = Arrays.asList(
                R.drawable.orchid_garden1, R.drawable.orchid_garden2, R.drawable.orchid_garden3,
                R.drawable.orchid_garden4, R.drawable.orchid_garden5
        );
        List<Integer> pcChandraGardenPhotos = Arrays.asList(
                R.drawable.pc_chanda_garden1, R.drawable.pc_chanda_garden2, R.drawable.pc_chanda_garden3,
                R.drawable.pc_chanda_garden4, R.drawable.pc_chanda_garden5
        );
        List<Integer> mandevillePhotos = Arrays.asList(
                R.drawable.mandeville1, R.drawable.mandeville2, R.drawable.mandeville3,
                R.drawable.mandeville4, R.drawable.mandeville5
        );
        List<Integer> verdeVistaPhotos = Arrays.asList(
                R.drawable.verde_vista1, R.drawable.verde_vista2, R.drawable.verde_vista3,
                R.drawable.verde_vista4
        );

        // Step 1: Initialize original list
        venueList = new ArrayList<>();
        venueList.add(new Venue("Galleria",R.drawable.galleria2, "Galleria 1910 Banquets", "7, Ho Chi Minh Sarani, Camac St, Sreet, West Bengal 700071", 25000 , 500, 15,4.7 ,"Indoor",50000,25000,galleriaPhotos));
        venueList.add(new Venue("Kathaakali",R.drawable.kathakali_banquet1, "Kathaakali Banquet", "B/2, Dr Bholanath Chakraborty Sarani, Ramrajatala, Howrah, West Bengal 711104", 80000, 300, 10, 4.6 ,"Indoor",80000,65000,kathakaliPhotos));
        venueList.add(new Venue("Subham",R.drawable.subham_banquet1, "Shubham Banquet", "Salkia School Road 140/1, Howrah, West Bengal 711101", 70000, 400, 25,4.1,"Indoor",90000,70000,shubhamBanquetPhotos));
        venueList.add(new Venue("Royal place",R.drawable.royal_banquet_hall1, "ROYAL PALACE BANQUET", "Raja Peary Mohan Rd, Kotrung, Uttarpara, West Bengal 712258", 50000, 500, 25,4.7,"Indoor",60000,35000,royalPalacePhotos));
        venueList.add(new Venue("Bika",R.drawable.bika_banquet1, "Bika Banquet", "76, Golaghata Rd, Dakshindari, Kolkata, South Dumdum, West Bengal 700048", 50000, 300, 25,4.2,"Indoor",90000,50000,bikaPhotos));
        venueList.add(new Venue("Shree",R.drawable.shree_banquet1, "Shree Banquet", "10/6A/1, Gobinda Khatick Rd, Tangra, Kolkata, West Bengal 700046", 60000, 250, 15, 4.3 ,"Indoor",60000,35000,shreeBanquetPhotos));
        venueList.add(new Venue("Orchid",R.drawable.orchid_garden1, "Orchid Banquets\n & Gardens", "E Topsia Rd, Mirania Gardens, East Topsia, Topsia, Kolkata, West Bengal 700046", 70000, 400, 20,4.5,"Garden & Hall",100000,70000,orchidBanquetPhotos));
        venueList.add(new Venue("PC chandra",R.drawable.pc_chanda_garden2, "PC Chandra Garden", "144, Arupota, Eastern Metropolitan Bypass, opp. Science, Kolkata, West Bengal 700105", 150000, 3000, 70,4.5,"Outdoor Garden",300000,100000,pcChandraGardenPhotos));
        venueList.add(new Venue("Mandeville",R.drawable.mandeville3, "The Villa at \n Mandeville", "8, Mandeville Gardens, Ekdalia, Ballygunge, Kolkata, West Bengal 700019", 150000, 1000, 61,4.9,"Indoor + Garden",250000,150000,mandevillePhotos));
        venueList.add(new Venue("Verde Vista",R.drawable.verde_vista1, "Club Verde Vista", "The Condoville, Budherhat Rd, 2052, Chak Garia, Panchasayar, Upohar, Kolkata, West Bengal 700094", 120000, 500, 20,4.3,"Garden + Indoor",250000,120000,verdeVistaPhotos));
        // Add more venues as needed...

        // Step 2: Copy venueList into filteredList
        filteredList = new ArrayList<>(venueList);

        // Step 3: Setup adapter with filteredList
        venueAdapter = new VenueAdapter(filteredList);
        recyclerView.setAdapter(venueAdapter);

        // Step 4: Add search logic
        searchVenueEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterVenues(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // not needed
            }
        });
    }

    private void filterVenues(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(venueList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Venue venue : venueList) {
                if (venue.getName().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(venue);
                }
            }
        }
        venueAdapter.notifyDataSetChanged();
    }
    private void showFilterDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_filter, null);

        EditText editCapacity = dialogView.findViewById(R.id.editCapacity);
        EditText editPrice = dialogView.findViewById(R.id.editPrice);
        RadioGroup radioGroupType = dialogView.findViewById(R.id.radioGroupType);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        dialogView.findViewById(R.id.btnCancel).setOnClickListener(v -> dialog.dismiss());

        dialogView.findViewById(R.id.btnApply).setOnClickListener(v -> {
            String capacityStr = editCapacity.getText().toString().trim();
            String priceStr = editPrice.getText().toString().trim();

            int minCapacity = capacityStr.isEmpty() ? 0 : Integer.parseInt(capacityStr);
            int maxPrice = priceStr.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(priceStr);

            String selectedType = "All";
            int selectedId = radioGroupType.getCheckedRadioButtonId();
            if (selectedId == R.id.radioIndoor) selectedType = "Indoor";
            else if (selectedId == R.id.radioOutdoor) selectedType = "Garden";

            applyFilters(minCapacity, maxPrice, selectedType);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void applyFilters(int minCapacity, int maxPrice, String type) {
        filteredList.clear();

        for (Venue venue : venueList) {
            boolean matchesCapacity = venue.getGuestCount() >= minCapacity;
            boolean matchesPrice = venue.getMaxPrice() <= maxPrice;
            boolean matchesType = type.equals("All") || venue.getType().equalsIgnoreCase(type);

            if (matchesCapacity && matchesPrice && matchesType) {
                filteredList.add(venue);
            }
        }

        if (filteredList.isEmpty()) {
            noResultText.setVisibility(View.VISIBLE);
        } else {
            noResultText.setVisibility(View.GONE);
        }

        venueAdapter.notifyDataSetChanged();
    }


}
