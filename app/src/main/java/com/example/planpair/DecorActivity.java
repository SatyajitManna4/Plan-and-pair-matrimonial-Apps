package com.example.planpair;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planpair.adapters.DecorationAdapter;
import com.example.planpair.adapters.PhotographerAdapter;
import com.example.planpair.adapters.VenueAdapter;
import com.example.planpair.models.Decoration;
import com.example.planpair.models.Photographer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DecorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DecorationAdapter adapter;
    private List<Decoration> decorationList;
    private List<Decoration> filteredList;
    private EditText searchDecoraters;
    private TextView noResultText;
    private ImageButton arrowBack,filterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        recyclerView = findViewById(R.id.recyclerView);
        noResultText = findViewById(R.id.noResultText);
        searchDecoraters = findViewById(R.id.searchAutoTextView);
        filterButton = findViewById(R.id.filterButton);
        arrowBack = findViewById(R.id.backButton);
        filterButton.setOnClickListener(v -> showFilterDialog());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Integer> violaPhotos = Arrays.asList(
                R.drawable.viola1, R.drawable.viola2, R.drawable.viola3,R.drawable.viola4,
                R.drawable.viola5,R.drawable.viola6,R.drawable.viola7
        );
        List<Integer> eventechPhotos = Arrays.asList(
                R.drawable.eventech1, R.drawable.eventech2, R.drawable.eventech3,R.drawable.eventech4,
                R.drawable.eventech5
        );
        List<Integer> divyamaanPhotos = Arrays.asList(
                R.drawable.divyamaan1, R.drawable.divyamaan2, R.drawable.divyamaan3
        );
        List<Integer> chameleonPhotos = Arrays.asList(
                R.drawable.chameleoneventskolkata1, R.drawable.chameleoneventskolkata2, R.drawable.chameleoneventskolkata3,R.drawable.chameleoneventskolkata4,
                R.drawable.chameleoneventskolkata5,R.drawable.chameleoneventskolkata6
        );
        List<Integer> swamangalamPhotos = Arrays.asList(
                R.drawable.swamangalam1, R.drawable.swamangalam2, R.drawable.swamangalam3,R.drawable.swamangalam4,
                R.drawable.swamangalam5,R.drawable.swamangalam6,R.drawable.swamangalam7
        );
        List<Integer> curlyCharmPhotos = Arrays.asList(
                R.drawable.curly_charm1, R.drawable.curly_charm2, R.drawable.curly_charm3,R.drawable.curly_charm4,
                R.drawable.curly_charm5
        );
        List<Integer> eventExpertPhotos = Arrays.asList(
                R.drawable.event_expert1, R.drawable.event_expert2, R.drawable.event_expert3,R.drawable.event_expert4,
                R.drawable.event_expert5,R.drawable.event_expert6
        );



        decorationList = new ArrayList<>();

        decorationList.add(new Decoration("viola",
                "Viola Events", "Barabazar Market, Kolkata", 4.9, 15,
                R.drawable.viola2, 50000, "Floral elegance for every celebration",
                "Floral decor, theme-based setups, mandap design, stage decor",
                "Viola Events is dedicated to delivering personalized and seamless event experiences, ensuring that every celebration is both memorable and uniquely tailored to their clients' desires.",
                violaPhotos));

        decorationList.add(new Decoration("eventech",
                "Eventech Designers", "Kasba, Kolkata", 4.7, 128,
                R.drawable.eventech3, 20000, "We design memories",
                "Wedding stages, entrance arches, lighting setup, custom props",
                "Eventech is a leading wedding planning and decoration company in Kolkata with 17+ years of experience. They specialize in creative themes, floral decor, and complete event management.",
                eventechPhotos));

        decorationList.add(new Decoration("divyamaan",
                "Divyamaan Eventz", "Kolkata", 4.5, 20,
                R.drawable.divyamaan1, 40000, "Divyamaan Eventz has successfully orchestrated over 100 weddings",
                "Full Wedding Planning, Customized decoration services for various events.",
                "Divyamaan Eventz, founded by Divya in 2016, is a Kolkata-based wedding planning and decoration company. They offer comprehensive services, including venue booking, decor, catering, photography, and entertainment.",
                divyamaanPhotos));

        decorationList.add(new Decoration("chameleon",
                "Chameleon Events", "Kolkata", 4.8, 170,
                R.drawable.chameleoneventskolkata2, 50000, "Chameleon Events Kolkata is a renowned event and wedding management company known for its adaptability and creativity.",
                "Wedding planning, Event Conceptualization and Design and decoration",
                "Chameleon Events, founded in 2007, is a top wedding and event management company based in Kolkata with branches in India, the USA, and the UK. They specialize in creative, customized weddings, corporate events, and brand promotions, earning praise for their innovative concepts and seamless execution across diverse locations.",
                chameleonPhotos));

        decorationList.add(new Decoration("swamangalam",
                "Swamangalam Events", "Khardaha, Kolkata", 4.3, 43,
                R.drawable.swamangalam1, 50000, "ElegaCustomized themes ranging from classic elegance to modern fusionnt",
                "Floral and Decoration",
                "Swamangalam Weddings and Events is a Kolkata-based event management company specializing in weddings, birthdays, and corporate events. With over 9 years of experience and 80+ events completed, they offer services like venue selection, decor, catering, photography, and entertainment.",
                swamangalamPhotos));

        decorationList.add(new Decoration("curlycharm",
                "Curly Charm Events", "Behala, Kolkata", 4.5, 20,
                R.drawable.curly_charm1, 60000, "Curly Charm tailors every wedding to reflect the couple's style and preferences.",
                "Wedding event decoration, Hospitality & Guest Support",
                "Curly Charm Events is a Kolkata-based wedding planning company founded in 2024 by Rima Palit. Specializing in personalized celebrations, they offer services including venue selection, decor, catering, photography, and entertainment.",
                curlyCharmPhotos));

        decorationList.add(new Decoration("eventexpertz",
                "Event Expertz", "Taltala, Kolkata", 4.8, 13,
                R.drawable.event_expert1, 60000, "With over 50 weddings successfully executed, they specialize in crafting personalized and seamless wedding experiences that reflect each couple's unique preferences and desires.",
                "Decoration and DJ & Entertainment",
                "Event Expertz is a premier wedding planning company based in Kolkata, specializing in creating personalized and seamless weddings. With 6 years of industry experience, they offer services including venue selection, décor, catering, photography, entertainment, and logistics management.",
                eventExpertPhotos));


        arrowBack.setOnClickListener(v -> finish());
        filteredList = new ArrayList<>(decorationList);
        adapter = new DecorationAdapter(this, filteredList);
        recyclerView.setAdapter(adapter);


        // Search
        searchDecoraters.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterDecoraters(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });


    }

    private void filterDecoraters(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(decorationList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Decoration decoration : decorationList) {
                if (decoration.getName().toLowerCase().contains(lowerCaseQuery) ||
                        decoration.getLocation().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(decoration);
                }
            }
        }

        adapter.notifyDataSetChanged();
        noResultText.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter by Price");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_price_filter_radio, null);
        builder.setView(dialogView);

        RadioGroup priceRadioGroup = dialogView.findViewById(R.id.priceRadioGroup);

        builder.setPositiveButton("Apply", (dialog, which) -> {
            int selectedId = priceRadioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) return; // No option selected

            List<Decoration> priceFilteredList = new ArrayList<>();

            for (Decoration d : decorationList) {
                int price = d.getStartingPrice();
                if (selectedId == R.id.radioUnder40 && price < 40000) {
                    priceFilteredList.add(d);
                } else if (selectedId == R.id.radio40to50 && price >= 40000 && price <= 50000) {
                    priceFilteredList.add(d);
                } else if (selectedId == R.id.radioAbove50 && price > 50000) {
                    priceFilteredList.add(d);
                }
            }

            filteredList = new ArrayList<>(priceFilteredList);
            adapter.updateList(filteredList);
            noResultText.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }


}
