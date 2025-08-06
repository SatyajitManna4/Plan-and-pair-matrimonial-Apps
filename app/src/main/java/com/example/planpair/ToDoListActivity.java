package com.example.planpair;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.planpair.adapters.ChecklistAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToDoListActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    List<String> listGroupTitles;
    HashMap<String, List<String>> listChildItems;
    ChecklistAdapter checklistAdapter;
    SharedPreferences sharedPreferences;
    public static final String PREF_NAME = "WeddingChecklistPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        expandableListView = findViewById(R.id.expandableListView);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        loadChecklistData();

        checklistAdapter = new ChecklistAdapter(this, listGroupTitles, listChildItems, sharedPreferences);
        expandableListView.setAdapter(checklistAdapter);
    }

    private void loadChecklistData() {
        listGroupTitles = new ArrayList<>();
        listChildItems = new HashMap<>();

        listGroupTitles.add("12 Months Out");
        listChildItems.put("12 Months Out", List.of(
                "Set a budget",
                "Create a guest list",
                "Book a venue",
                "Hire key vendors",
                "Choose a wedding date",
                "Select a theme or style",
                "Shop for attire"
        ));

        listGroupTitles.add("6 Months Out");
        listChildItems.put("6 Months Out", List.of(
                "Send save-the-dates",
                "Hire additional vendors",
                "Plan the ceremony",
                "Start a wedding registry",
                "Order invitations",
                "Plan the honeymoon",
                "Attend pre-marital counseling"
        ));

        listGroupTitles.add("3 Months Out");
        listChildItems.put("3 Months Out", List.of(
                "Send invitations",
                "Plan the rehearsal dinner",
                "Finalize the menu",
                "Buy wedding rings",
                "Confirm details with vendors",
                "Create a day-of timeline",
                "Arrange accommodation for guests"
        ));

        listGroupTitles.add("Final Week");
        listChildItems.put("Final Week", List.of(
                "Confirm the final guest count",
                "Pick up attire",
                "Prepare payments",
                "Pack for honeymoon",
                "Delegate day-of tasks",
                "Practice vows",
                "Relax and pamper"
        ));

        listGroupTitles.add("Night Before");
        listChildItems.put("Night Before", List.of(
                "Get plenty of rest",
                "Prepare a wedding emergency kit",
                "Have a light dinner"
        ));
    }
}
