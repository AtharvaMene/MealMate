package com.example.mealmate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mealmate.model.MealHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcomeMessage, tvMealBalance, tvSelectedMeal;


    private Spinner spinnerMealOptions;
    private Button btnGenerateQR;
    private ImageView ivQRCode;


    private String selectedMeal = "";
    private String userName = "";
    private String userId = "";

    private String userCollegeId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        fetchUserData();
        setupSpinner();
        setupListeners();
        setupBottomNavigation();
    }
    private void initViews() {
        tvWelcomeMessage = findViewById(R.id.tv_welcome_message);
        tvMealBalance = findViewById(R.id.tv_meal_balance);
        tvSelectedMeal = findViewById(R.id.tv_selected_meal);
        spinnerMealOptions = findViewById(R.id.spinner_meal_options);

        btnGenerateQR = findViewById(R.id.btn_generate_qr);
        ivQRCode = findViewById(R.id.iv_qr_code);


    }
    private void fetchUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        userId = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userName = snapshot.child("name").getValue(String.class);
                    userCollegeId = snapshot.child("collegeId").getValue(String.class);

                    Long mealsRemaining = snapshot.child("mealsRemaining").getValue(Long.class);

                    tvWelcomeMessage.setText("👤 Welcome, " + userName);
                    tvMealBalance.setText("🍽️ Meals Remaining: " + (mealsRemaining != null ? mealsRemaining : 0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner() {
        String[] meals = {"Select Meal", "Breakfast", "Lunch", "Snacks", "Dinner"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, meals);
        spinnerMealOptions.setAdapter(adapter);

        spinnerMealOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMeal = parent.getItemAtPosition(position).toString();

                if (!selectedMeal.equals("Select Meal")) {
                    tvSelectedMeal.setText("Selected: " + selectedMeal);
                    btnGenerateQR.setEnabled(true);
                } else {
                    tvSelectedMeal.setText("Selected: None");
                    btnGenerateQR.setEnabled(false);
                    ivQRCode.setImageDrawable(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupListeners() {
        btnGenerateQR.setOnClickListener(view -> {
            if (userName.isEmpty() || userCollegeId.isEmpty()) {
                Toast.makeText(this, "User data not loaded yet", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedMeal.isEmpty() || selectedMeal.equals("Select Meal")) {
                Toast.makeText(this, "Please select a valid meal", Toast.LENGTH_SHORT).show();
                return;
            }

            String qrData = "Name: " + userName + "\nCollege ID: " + userCollegeId + "\nMeal: " + selectedMeal;

            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(qrData, BarcodeFormat.QR_CODE, 400, 400);
                ivQRCode.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
                Toast.makeText(this, "QR Generation Error", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔽 SAVE MEAL HISTORY IN FIREBASE 🔽
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                String userId = user.getUid();
                String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(new java.util.Date());

                MealHistory mealHistory = new MealHistory(timestamp, selectedMeal, "Generated");

                DatabaseReference historyRef = FirebaseDatabase.getInstance()
                        .getReference("MealHistory")
                        .child(userId)
                        .push();

                historyRef.setValue(mealHistory)
                        .addOnSuccessListener(unused -> Toast.makeText(this, "Meal history saved", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to save meal history", Toast.LENGTH_SHORT).show());
            }
        });
    }


    private void setupBottomNavigation() {
        findViewById(R.id.nav_home).setOnClickListener(view ->
                Toast.makeText(MainActivity.this, "Home clicked", Toast.LENGTH_SHORT).show());

        findViewById(R.id.nav_profile).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, EditProfileActivity.class)));

        findViewById(R.id.nav_history).setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, HistoryActivity.class));
        });
    }
}
