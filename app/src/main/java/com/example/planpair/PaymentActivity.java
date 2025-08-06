/*
package com.example.planpair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {
    CardView cardView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        cardView = findViewById(R.id.card);
        // Status Bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        // Initialize Razorpay
        Checkout.preload(getApplicationContext());

        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener(view -> startPayment());
    }

    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_uGj2098nJYQX0A"); // your Razorpay Test Key ID

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Plan & Pair Premium");
            options.put("description", "Unlock Premium Features");
            options.put("currency", "INR");
            options.put("amount", "100"); // ₹1.00 (amount in paise)

            // Enable UPI explicitly
            JSONObject methodOptions = new JSONObject();
            methodOptions.put("netbanking", true);
            methodOptions.put("card", true);
            methodOptions.put("wallet", true);
            methodOptions.put("upi", true);
            options.put("method", methodOptions);
            checkout.open(PaymentActivity.this, options);
        } catch (Exception e) {
            Toast.makeText(this, "Error in Payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isPremium", true);
        editor.apply();

        Toast.makeText(this, "Payment Successful! Premium Unlocked", Toast.LENGTH_LONG).show();
        finish(); // Return to HomeActivity
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment Failed! Please try again.", Toast.LENGTH_LONG).show();
    }
}*/

// premium update into the firestore firebase

package com.example.planpair;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    CardView cardView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cardView = findViewById(R.id.card);

        // Make status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(android.graphics.Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }

        // Initialize Razorpay
        Checkout.preload(getApplicationContext());

        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener(view -> startPayment());
    }

    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_uGj2098nJYQX0A"); // Replace with your Razorpay test/live key

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Plan & Pair Premium");
            options.put("description", "Unlock Premium Features");
            options.put("currency", "INR");
            options.put("amount", "49900"); // ₹499.00 in paise

            // Enable payment methods
            JSONObject methodOptions = new JSONObject();
            methodOptions.put("netbanking", true);
            methodOptions.put("card", true);
            methodOptions.put("wallet", true);
            methodOptions.put("upi", true);
            options.put("method", methodOptions);

            checkout.open(PaymentActivity.this, options);
        } catch (Exception e) {
            Toast.makeText(this, "Error in Payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("UsersData")
                .document(currentUid)
                .update("isPremium", true)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Payment Successful! Premium Unlocked", Toast.LENGTH_LONG).show();

                    // Set result OK to notify HomeActivity
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Payment succeeded but failed to update premium status.", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(this, "Payment Failed! Please try again.", Toast.LENGTH_LONG).show();
    }
}
