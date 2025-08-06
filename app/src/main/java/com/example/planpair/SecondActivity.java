//package com.example.weddingplanner;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.prolificinteractive.materialcalendarview.CalendarDay;
//import com.prolificinteractive.materialcalendarview.CalendarMode;
//import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
//import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.Locale;
//
//public class SecondActivity extends AppCompatActivity {
//
//    MaterialCalendarView calendarView;
//    Button doneButton;
//    String selectedDate;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_second);
//
//        calendarView = findViewById(R.id.calendarView);
//        doneButton = findViewById(R.id.doneButton);
//
//        calendarView.state().edit()
//                .setFirstDayOfWeek(Calendar.MONDAY)
//                .setMinimumDate(CalendarDay.from(2025, 1, 1))
//                .setMaximumDate(CalendarDay.from(2100, 12, 31))
//                .setCalendarDisplayMode(CalendarMode.MONTHS)
//                .commit();
//
//        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(@NonNull @androidx.annotation.NonNull MaterialCalendarView widget, @NonNull @androidx.annotation.NonNull CalendarDay date, boolean selected) {
//                Toast.makeText(SecondActivity.this,"" + date,Toast.LENGTH_SHORT).show();
//            }
//        });
////        // Initialize selected date with the current date
////        Calendar selectedCalendar = Calendar.getInstance();
////        selectedDate = formatDate(selectedCalendar.getTimeInMillis());
////
////        // Set on date changed listener
////        calendarView.setOnDateChangedListener((widget, date, selected) -> {
////            // Here date is of type Calendar
////            selectedDate = formatDate(date.getMonth()); // Correctly extract the time in millis
////        });
//
//        // Handle button click to save the selected date
//        doneButton.setOnClickListener(v -> {
//            saveWeddingDate(selectedDate);
//            Toast.makeText(SecondActivity.this, "Saved: " + selectedDate, Toast.LENGTH_SHORT).show();
//        });
//    }
//
//    private void saveWeddingDate(String date) {
//        SharedPreferences sharedPreferences = getSharedPreferences("weddingPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("weddingDate", date);
//        editor.apply(); // or use commit()
//    }
//
////    private String formatDate(long millis) {
////        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
////        return sdf.format(new Date(millis));
////    }
//}
package com.example.planpair;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SecondActivity extends AppCompatActivity {

    CalendarView calendarView;
    Button doneButton;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        calendarView = findViewById(R.id.calendarView);
        doneButton = findViewById(R.id.doneButton);

        // Set default selected date
        selectedDate = formatDate(calendarView.getDate());

        final String yourName = getIntent().getStringExtra("NAME1");
        final String partnerName = getIntent().getStringExtra("NAME2");

        // Handle date change
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Update selectedDate when the user picks a new date
            selectedDate = String.format(Locale.getDefault(), "%02d %s %d",
                    dayOfMonth,
                    new SimpleDateFormat("MMMM", Locale.getDefault()).format(new Date(year, month, dayOfMonth)),
                    year);
        });


        // Handle button click
        doneButton.setOnClickListener(v -> {
            saveWeddingDate(selectedDate);
            Toast.makeText(SecondActivity.this, "Saved: " + selectedDate, Toast.LENGTH_SHORT).show();
            // Create an Intent to pass data to the display activity
            Intent intent = new Intent(SecondActivity.this, VendorActivity.class);
            intent.putExtra("NAME1", yourName);
            intent.putExtra("NAME2", partnerName);
            intent.putExtra("WEDDING_DATE",selectedDate);  // Pass selected date
            startActivity(intent);
        });
    }

    private void saveWeddingDate(String date) {
        SharedPreferences sharedPreferences = getSharedPreferences("weddingPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("weddingDate", date);
        editor.apply(); // or use commit()
    }

    private String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return sdf.format(new Date(millis));
    }
}