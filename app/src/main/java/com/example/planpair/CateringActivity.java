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

import com.example.planpair.adapters.CateringAdapter;
import com.example.planpair.adapters.DecorationAdapter;
import com.example.planpair.adapters.PhotographerAdapter;
import com.example.planpair.adapters.VenueAdapter;
import com.example.planpair.models.Catering;
import com.example.planpair.models.Decoration;
import com.example.planpair.models.Photographer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CateringActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CateringAdapter adapters;
    private List<Catering> cateringList;
    private List<Catering> filteredList;
    private EditText searchCaterers;
    private TextView noResultText;
    private ImageButton arrowBack,filterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catering);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        recyclerView = findViewById(R.id.recyclerView);
        noResultText = findViewById(R.id.noResultText);
        searchCaterers = findViewById(R.id.searchAutoTextView);
        filterButton = findViewById(R.id.filterButton);
        arrowBack = findViewById(R.id.backButton);
        filterButton.setOnClickListener(v -> showFilterDialog());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Integer> sweetnsourPhotos = Arrays.asList(
                R.drawable.sweetnsour1, R.drawable.sweetnsour2, R.drawable.sweetnsour3,R.drawable.sweetnsour4,
                R.drawable.sweetnsour5,R.drawable.sweetnsour6,R.drawable.sweetnsour7,R.drawable.sweetnsour8
        );
        List<Integer> gharoaPhotos = Arrays.asList(
                R.drawable.gharoa_catering1, R.drawable.gharoa_catering2, R.drawable.gharoa_catering3,R.drawable.gharoa_catering4,
                R.drawable.gharoa_catering6
        );
        List<Integer> bhojcaterPhotos = Arrays.asList(
                R.drawable.bhoj_caterer1, R.drawable.bhoj_caterer2, R.drawable.bhoj_caterer3,R.drawable.bhoj_caterer4,R.drawable.bhoj_caterer5,R.drawable.bhoj_caterer6,R.drawable.bhoj_caterer7
        );
        List<Integer> orioncaterPhotos = Arrays.asList(
                R.drawable.orion_caterer1, R.drawable.orion_caterer2, R.drawable.orion_caterer3,R.drawable.orion_caterer4,
                R.drawable.orion_caterer5,R.drawable.orion_caterer6,R.drawable.orion_caterer7
        );
        List<Integer> royalcaterPhotos = Arrays.asList(
                R.drawable.royal_caterer1, R.drawable.royal_caterer2, R.drawable.royal_caterer3,R.drawable.royal_caterer4,
                R.drawable.royal_caterer5,R.drawable.royal_caterer6,R.drawable.royal_caterer7
        );
        List<Integer> asparaguscaterPhotos = Arrays.asList(
                R.drawable.asparagus_catering1, R.drawable.asparagus_catering2, R.drawable.asparagus_catering3,R.drawable.asparagus_catering4,
                R.drawable.asparagus_catering5,R.drawable.asparagus_catering6,R.drawable.asparagus_catering7,R.drawable.asparagus_catering8
        );
        List<Integer> guptacaterPhotos = Arrays.asList(
                R.drawable.gupta_caterer1, R.drawable.gupta_caterer2, R.drawable.gupta_caterer3,R.drawable.gupta_caterer4,
                R.drawable.gupta_caterer5,R.drawable.gupta_caterer6,R.drawable.gupta_caterer7
        );
        List<Integer> ahelicaterPhotos = Arrays.asList(
                R.drawable.aheli_caterer, R.drawable.aheli_caterer2, R.drawable.aheli_caterer3,R.drawable.aheli_caterer4,
                R.drawable.aheli_caterer5,R.drawable.aheli_caterer6,R.drawable.aheli_caterer7
        );
        List<Integer> babulcaterPhotos = Arrays.asList(
                R.drawable.babul_caterer1, R.drawable.babul_caterer2, R.drawable.babul_caterer3,R.drawable.babul_caterer4,
                R.drawable.babul_caterer5,R.drawable.babul_caterer6,R.drawable.babul_caterer7,R.drawable.babul_caterer8,R.drawable.babul_caterer9
        );



        cateringList = new ArrayList<>();
        cateringList.add(new Catering("SweetnSour","Sweet-N-Sour Catering", "Shop No, JJ-7, H.A-7, Baguihati Main Rd, Ashwini Nagar, Baguihati, Kolkata, West Bengal 700159", 4.9, 537, R.drawable.sweetnsour6, 700,"Catering Services: Provides both vegetarian and non-vegetarian menus, featuring cuisines such as Bengali, North Indian, South Indian, Mughlai, Chinese, Italian, Thai, Japanese, Gujarati, Rajasthani, Goan, and a variety of desserts.\n Live Counters: Offers live stations for Italian, Chinese, Mexican dishes, as well as chaat and phuchka counters. ", "Sweet-N-Sour Catering Services has built a reputation for delivering high-quality food and impeccable service. Their team of experienced professionals is dedicated to making each event memorable by focusing on hygiene, presentation, and taste.",sweetnsourPhotos));
        cateringList.add(new Catering("Gharoa","Gharoa Catering", "13/2 Golf Green, Near Golf Green Central Park, Kolkata, West Bengal 700095", 3.8, 87, R.drawable.gharoa_catering1, 600, "Specialized menus tailored for weddings, including traditional Bengali dishes and international cuisines.\n Authentic Bengali cuisine for events like weddings, receptions, and cultural gatherings.", "Gharoa Catering is recognized as one of Kolkata's premier catering services, known for its commitment to quality, hygiene, and customer satisfaction.",gharoaPhotos));
        cateringList.add(new Catering("Bhoj","Bhoj Caterers", "11-B, Ashton Road, Madan Mohan Malaviya Sarani, Jadubabur Bazar, Bhowanipore, Kolkata, West Bengal 700020", 3.8, 103, R.drawable.bhoj_caterer2, 800, "Specializes in crafting sophisticated and flavorful menus for weddings, ensuring a personalized experience that aligns with the grandeur of the celebration. \n Indian Cuisine: Includes Mughlai (e.g., Biryani, Kebabs), North Indian (e.g., Butter Chicken, Paneer Tikka), South Indian (e.g., Dosas, Idlis), and popular street foods like Chaat and Pani Puri.\n" +
                "\n" +
                "International Cuisine: Offers Continental, Chinese, Italian, and Mediterranean dishes.\n" +
                "\n" +
                "Vegetarian & Vegan Options: Provides a wide variety of vegetarian and vegan dishes, ensuring inclusivity for all guests.\n" +
                "\n" +
                "Desserts & Beverages: Features traditional Indian sweets, cakes, pastries, puddings, and a selection of beverages including fresh juices, mocktails, lassi, and chai. ", "Bhoj Caterers has built a reputation for delivering high-quality food and impeccable service. Their team of experienced professionals is dedicated to making each event memorable by focusing on hygiene, presentation, and taste.",bhojcaterPhotos));
        cateringList.add(new Catering("orion","Orion Caterer", "44B, Nandaram Sen St, Sovabazar, Hatkhola, Kolkata, West Bengal 700005", 4.7, 89, R.drawable.orion_caterer3, 800, "Orion Caterer specializes in providing comprehensive catering solutions for weddings, ensuring that each event is tailored to the client's preferences. Their services include:\n" +
                "\n" +
                "Customized Menus: Offering a diverse range of cuisines, including Bengali, North Indian, South Indian, Chinese, Italian, Lebanese, Thai, Gujarati, and Rajasthani dishes. \n" +
                 "Live Counters: Featuring interactive food stations such as chaat, phuchka, and mocktail bars to enhance guest engagement.", "Orion Caterer is a premier wedding catering service based in Kolkata, established in 1994 by Mr. Arun Kumar Basak, a seasoned hospitality professional with experience at esteemed establishments like the Oberoi Palace in Kashmir. Over the years, Orion Caterer has garnered a reputation for delivering exceptional culinary experiences, blending traditional flavors with global cuisines to create memorable events.",orioncaterPhotos));
        cateringList.add(new Catering("royalCook","Royal Cook Caterer", "18/7, Nafar Chandra Das Rd, Auddy Bagan Basti, Behala, Kolkata, West Bengal 700034", 4.7, 229, R.drawable.royal_caterer7, 300, "Royal Cook Caterer specializes in comprehensive wedding catering solutions, offering:\n" +
                "\n" +
                "Customized Menus: Tailored to client preferences, featuring a diverse range of cuisines including Bengali, North Indian, South Indian, Chinese, Italian, Lebanese, Thai, Gujarati, and Rajasthani dishes.\n" +
                "\n" +
                "Live Counters: Interactive food stations such as chaat, phuchka, mocktail bars, and dessert counters to enhance guest engagement.", "Royal Cook Caterer is a premier catering service based in Kolkata, renowned for delivering exceptional culinary experiences for weddings and various other events. With over 35 years of experience, they have established a reputation for quality, hospitality, and versatility.",royalcaterPhotos));
        cateringList.add(new Catering("Asparagus","Asparagus Catering ", "Asparagus Hospitality, CA 229, CA Block, Sector 1, Bidhannagar, Kolkata, West Bengal 700064", 4.8, 628, R.drawable.asparagus_catering1, 700, "Asparagus Catering Unit offers a wide array of services tailored for weddings:\n" +
                "\n" +
                "Customized Menus: They provide a diverse range of cuisines, including Indian (Bengali, North Indian, South Indian), Continental, Chinese, Italian, Lebanese, Thai, Gujarati, and Rajasthani dishes. \n" +
                "wedmegood.com\n" +
                "\n" +
                "Live Counters: Interactive food stations such as chaat, phuchka, mocktail bars, and dessert counters to enhance guest engagement.", "Founded in 2008 by Pritam Datta, an alumnus of IHM Taratala-Kolkata, Asparagus Catering Unit has established itself as a leading catering service in Kolkata. ",asparaguscaterPhotos));
        cateringList.add(new Catering("guptacater","Gupta Caterers", "101/C, Raja Dinendra St, Manicktala, Sahitya Parishad, Ward Number 15, Kolkata, West Bengal 700006", 4.8, 85, R.drawable.gupta_caterer2, 1000, "Gupta Caterers specializes in providing comprehensive catering solutions for weddings, offering:\n" +
                "\n" +
                "Customized Menus: A diverse range of cuisines including North Indian, South Indian, Bengali, Chinese, Italian, Thai, Gujarati, Rajasthani, Goan, Maharashtrian, and more. \n" +
                "\n" +
                "Live Counters: Interactive food stations such as chaat, phuchka, mocktail bars, and dessert counters to enhance guest engagement.", "Gupta Caterers is a distinguished wedding catering service based in Kolkata, renowned for its exceptional culinary offerings and professional service. With over two decades of experience, they have catered to a diverse clientele, ensuring memorable dining experiences for various events.",guptacaterPhotos));
        cateringList.add(new Catering("aheli","Aheli Caterer","Old culcatta road, Natunpally, 1st Ln, near Bandhu Mahal club, boropoul, Barrackpore, Kolkata, West Bengal 700123",5.0,521,R.drawable.aheli_caterer,500,"Aheli Caterer specializes in providing comprehensive catering solutions for weddings, offering:\n" +
                "\n" +
                "Customized Menus: A diverse range of cuisines including Bengali, North Indian, South Indian, Chinese, Italian, Lebanese, Thai, Gujarati, Rajasthani, and more.\n" +
                "\n" +
                "Live Counters: Interactive food stations such as chaat, phuchka, mocktail bars, and dessert counters to enhance guest engagement. ","Aheli Caterer is a renowned catering service based in Barrackpore, Kolkata, known for its exceptional culinary offerings and professional service. With over 19 years of experience, they have catered to a diverse clientele, ensuring memorable dining experiences for various events.",ahelicaterPhotos));
        cateringList.add(new Catering("babul","Babul Caterer","No 2 Airport gate, Old Gurudwarai, Dum Dum, Kolkata, West Bengal 700081",4.8,278,R.drawable.babul_caterer1,600,"Babul Caterer specializes in providing comprehensive catering solutions for weddings, offering:\n" +
                "\n" +
                "Customized Menus: A diverse range of cuisines including Bengali, North Indian, South Indian, Chinese, Italian, and Continental dishes. \n" +
                "\n" +
                "Live Counters: Interactive food stations such as chaat, phuchka, mocktail bars, and dessert counters to enhance guest engagement.","Babul Caterer is a premier catering service based in Kolkata, renowned for its exceptional culinary offerings and professional service. With over 30 years of experience, they have catered to a diverse clientele, ensuring memorable dining experiences for various events. ",babulcaterPhotos));

        arrowBack.setOnClickListener(v -> finish());
        filteredList = new ArrayList<>(cateringList);
        adapters = new CateringAdapter(this, filteredList);
        recyclerView.setAdapter(adapters);


        // Search
        searchCaterers.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterCaterers(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });


    }

    private void filterCaterers(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(cateringList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Catering catering : cateringList) {
                if (catering.getName().toLowerCase().contains(lowerCaseQuery) ||
                        catering.getLocation().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(catering);
                }
            }
        }

        adapters.notifyDataSetChanged();
        noResultText.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter by Price");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_catering_filter, null);
        builder.setView(dialogView);

        RadioGroup priceRadioGroup = dialogView.findViewById(R.id.priceRadioGroup);

        builder.setPositiveButton("Apply", (dialog, which) -> {
            int selectedId = priceRadioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) return; // No option selected

            List<Catering> priceFilteredList = new ArrayList<>();

            for (Catering c : cateringList) {
                int price = c.getStartingPrice();
                if (selectedId == R.id.radioUnder500 && price < 500) {
                    priceFilteredList.add(c);
                } else if (selectedId == R.id.radio500to800 && price >= 500 && price <= 800) {
                    priceFilteredList.add(c);
                } else if (selectedId == R.id.radioAbove800 && price > 800) {
                    priceFilteredList.add(c);
                }
            }

            filteredList = new ArrayList<>(priceFilteredList);
            adapters.updateList(filteredList);
            noResultText.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }


}
