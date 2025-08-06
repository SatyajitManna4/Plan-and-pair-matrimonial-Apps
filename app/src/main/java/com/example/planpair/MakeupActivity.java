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
import com.example.planpair.adapters.MakeupAdapter;
import com.example.planpair.adapters.PhotographerAdapter;
import com.example.planpair.adapters.VenueAdapter;
import com.example.planpair.models.Decoration;
import com.example.planpair.models.MakeupArtist;
import com.example.planpair.models.Photographer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MakeupActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MakeupAdapter makeupAdapter;
    private List<MakeupArtist> makeupArtistList;
    private List<MakeupArtist> filteredList;
    private EditText searchMakeupArtist;
    private TextView noResultText;
    private ImageButton arrowBack,filterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_makeup);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        recyclerView = findViewById(R.id.recyclerView);
        noResultText = findViewById(R.id.noResultText);
        searchMakeupArtist = findViewById(R.id.searchAutoTextView);
        filterButton = findViewById(R.id.filterButton);
        arrowBack = findViewById(R.id.backButton);
        filterButton.setOnClickListener(v -> showFilterDialog());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Integer> snehaMakeupPhotos = Arrays.asList(
                R.drawable.sneha_makeup, R.drawable.sneha_makeup2, R.drawable.sneha_makeup3,R.drawable.sneha_makeup4,
                R.drawable.sneha_makeup5,R.drawable.sneha_makeup6,R.drawable.sneha_makeup7
        );
        List<Integer> anubratiMakeupPhotos = Arrays.asList(
                R.drawable.anubrati_makeup1, R.drawable.anubrati_makeup2, R.drawable.anubrati_makeup3,R.drawable.anubrati_makeup4,
                R.drawable.anubrati_makeup5,R.drawable.anubrati_makeup6,R.drawable.anubrati_makeup7
        );
        List<Integer> bhaskarMakeupPhotos = Arrays.asList(
                R.drawable.bhaskar_makeup1, R.drawable.bhaskar_makeup2, R.drawable.bhaskar_makeup3,R.drawable.bhaskar_makeup4,R.drawable.bhaskar_makeup5,R.drawable.bhaskar_makeup6
        );
        List<Integer> makeoverExpresssPhotos = Arrays.asList(
                R.drawable.makeoverxpress1, R.drawable.makeoverxpress2, R.drawable.makeoverxpress3,R.drawable.makeoverxpress,
                R.drawable.makeoverxpress5,R.drawable.makeoverxpress6
        );
        List<Integer> deenasMakeoverPhotos = Arrays.asList(
                R.drawable.deenas_makeover1, R.drawable.deenas_makeover2, R.drawable.deenas_makeover3,R.drawable.deenas_makeover4,
                R.drawable.deenas_makeover5
        );
        List<Integer> siddhartMakeupPhotos = Arrays.asList(
                R.drawable.siddhart_jaiswal_makeup1, R.drawable.siddhart_jaiswal_makeup2, R.drawable.siddhart_jaiswal_makeup3,R.drawable.siddhart_jaiswal_makeup4,
                R.drawable.siddhart_jaiswal_makeup5,R.drawable.siddhart_jaiswal_makeup6
        );
        List<Integer> mayuriMakeupPhotos = Arrays.asList(
                R.drawable.mayuri_makeup1, R.drawable.mayuri_makeup2, R.drawable.mayuri_makeup3,R.drawable.mayuri_makeup4,
                R.drawable.mayuri_makeup5,R.drawable.mayuri_makeup6,R.drawable.mayuri_makeup7
        );
        List<Integer> rajaMakeupPhotos = Arrays.asList(
                R.drawable.raja_makeup1, R.drawable.raja_makeup2, R.drawable.raja_makeup3,R.drawable.raja_makeup4,
                R.drawable.raja_makeup5,R.drawable.raja_makeup6,R.drawable.raja_makeup7
        );



        makeupArtistList = new ArrayList<>();
        makeupArtistList.add(new MakeupArtist("snehamua","Sneha Bhowmick", "Block 1 Flat, SRIJAN MIDLANDS, 1 D, Jessore Rd, Fortune City, Madhyamgram, Kolkata, West Bengal 700132", 4.8, 154, R.drawable.sneha_makeup6, 18000, "HD, Airbrush, Traditional Bridal Makeup","Sneha Bhowmick is a professional International makeup artist, based in Kolkata, with an experience of 5 years. She is experienced and well trained makeover stylist from London College Of Makeup and offer personalized makeover for every occasions. ",snehaMakeupPhotos));
        makeupArtistList.add(new MakeupArtist("anunratimua","Anubrati's Makeup Studio", "174, Rajdanga Sarat Park Rd, Rajdanga Chakraborty Para, Sector A, East Kolkata Twp, Kolkata, West Bengal 700107", 4.9, 186, R.drawable.anubrati_makeup7, 12000, "HD, 3D, 4D, Airbrush, Pre-wedding & Party Makeup","Best Bridal Makeup Artist, Celebrity Makeup Artist, Mentor and Educator ",anubratiMakeupPhotos));
        makeupArtistList.add(new MakeupArtist("bhaskarmua"," Bhaskar Biswas Makeup Studio", "Dum Dum, Kolkata", 4.7, 10, R.drawable.bhaskar_makeup2, 10000,  "Bridal, Party, Engagement, Reception Makeup","Bridal Makeover by Bhaskar Biswas from Kolkata is your one choice for all kinds of makeover needs. One of the most well known makeup services , they offer top class services to their clients by understanding their needs and desires and working accordingly.",bhaskarMakeupPhotos));
        makeupArtistList.add(new MakeupArtist("makeoverexpress","Makeoverxpress - MOXSA", "11th Floor, Sankalpa II, T-9, Street Number 160, BF Block(Newtown), Action Area 1B, Newtown, New Town, West Bengal 700156", 4.8, 88, R.drawable.makeoverxpress6, 11000,  "Bridal, Reception, Mehndi, Sangeet, Party Makeup","\"Makeoverxpress Studio & Academy - MOXSA is where you can find the perfect look for every occasion by the Best Makeup Artist in Kolkata run by Rimanka J. Whether you're looking to enhance your features with a subtle, natural glow or go for a bold, dramatic statement, our talented team of makeup artists has the experience to make your vision a reality.",makeoverExpresssPhotos));
        makeupArtistList.add(new MakeupArtist("deenamua","Deena's Makeover", "Oasis Nature, 266, Madurdaha, Hussainpur, Kolkata, West Bengal 700107", 4.9, 75, R.drawable.deenas_makeover5, 9000, " Bridal, Pre-wedding, Reception Makeup","In the heart of vibrant Kolkata, Deena's Makeover is a sanctuary of artistry that orchestrates symphony of services to unleash the radiance within you. Led by Kolkata's premier bridal makeup artist, this South Kolkata establishment is a canvas where talented makeup magicians, skilled hairstylists, and expert skincare specialists weave their magic, ensuring every detail is flawlessly orchestrated, leaving you poised and ready to shine on your special day.",deenasMakeoverPhotos));
        makeupArtistList.add(new MakeupArtist("siddhartmua","Siddhart Jaiswal Artistry", "73, Sisir bhadhury Sarani, Shanti Kunj Apartment, opposite Duff Church, Kolkata, West Bengal 700006", 4.8, 941, R.drawable.siddhart_jaiswal_makeup4, 13500,  "Bridal, Sangeet, Haldi Makeup","Siddhart Jaiswal is a renowned bridal makeup artist and creative director based in Kolkata, India. Celebrated for his elegant and flawless bridal transformations, he has garnered accolades including the IEA Award in 2019.",siddhartMakeupPhotos));
        makeupArtistList.add(new MakeupArtist("mayurimua","Mayuri's Makeup Studio", "Vignesh Tower, C Road, Anandapuri, Barrackpore Kolkata, India 700122 West Bengal", 4.8, 591, R.drawable.mayuri_makeup2, 15000,  "HD makeup, airbrush makeup, hairstyling, hair spa, straightening, hair coloring, nail styling, makeup for family and friends, pre and post-wedding makeup","Mayuri's The Professional Makeup Artist is a reputable bridal makeup studio and academy based in Kolkata, India, led by certified makeup artist Mayuri Sarkar. With over 7 years of experience, Mayuri specializes in creating sophisticated and elegant bridal looks tailored to individual skin tones and preferences.",mayuriMakeupPhotos));
        makeupArtistList.add(new MakeupArtist("rajamua","Raja Kundu's Makeup Studio", "Kolkata, West Bengal", 4.7, 186, R.drawable.raja_makeup6, 10000, "Bridal, Party, Engagement, Reception Makeup","Raja Kundu is a renowned makeup artist and creative director based in Kolkata, West Bengal. He is celebrated for his expertise in bridal makeup and his contributions to the fashion and beauty industry.",rajaMakeupPhotos));



        arrowBack.setOnClickListener(v -> finish());
        filteredList = new ArrayList<>(makeupArtistList);
        makeupAdapter = new MakeupAdapter(this, filteredList);
        recyclerView.setAdapter(makeupAdapter);


        // Search
        searchMakeupArtist.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMakeupArtist(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });


    }

    private void filterMakeupArtist(String query) {
        filteredList.clear();

        if (query.isEmpty()) {
            filteredList.addAll(makeupArtistList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (MakeupArtist makeupArtist : makeupArtistList) {
                if (makeupArtist.getName().toLowerCase().contains(lowerCaseQuery) ||
                        makeupArtist.getLocation().toLowerCase().contains(lowerCaseQuery)) {
                    filteredList.add(makeupArtist);
                }
            }
        }

        makeupAdapter.notifyDataSetChanged();
        noResultText.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
    }
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter by Price");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_makeup_filter, null);
        builder.setView(dialogView);

        RadioGroup priceRadioGroup = dialogView.findViewById(R.id.priceRadioGroup);

        builder.setPositiveButton("Apply", (dialog, which) -> {
            int selectedId = priceRadioGroup.getCheckedRadioButtonId();
            if (selectedId == -1) return; // No option selected

            List<MakeupArtist> priceFilteredList = new ArrayList<>();

            for (MakeupArtist d : makeupArtistList) {
                int price = d.getStartingPrice();
                if (selectedId == R.id.radioUnder10 && price < 10000) {
                    priceFilteredList.add(d);
                } else if (selectedId == R.id.radio10to15 && price >= 10000 && price < 15000) {
                    priceFilteredList.add(d);
                } else if (selectedId == R.id.radioAbove15 && price >= 15000) {
                    priceFilteredList.add(d);
                }
            }

            filteredList = new ArrayList<>(priceFilteredList);
            makeupAdapter.updateList(filteredList);
            noResultText.setVisibility(filteredList.isEmpty() ? View.VISIBLE : View.GONE);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }


}
