package com.example.mealmate;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class ReportActivity extends AppCompatActivity {

    private TextView totalMealsText, breakfastCountText, lunchCountText, dinnerCountText;
    private Button btnGenerateReport;

    private DatabaseReference historyRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        totalMealsText = findViewById(R.id.totalMealsText);
        breakfastCountText = findViewById(R.id.breakfastCountText);
        lunchCountText = findViewById(R.id.lunchCountText);
        dinnerCountText = findViewById(R.id.dinnerCountText);
        btnGenerateReport = findViewById(R.id.btnGenerateReport);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        historyRef = FirebaseDatabase.getInstance().getReference("MealHistory").child(currentUserId);

        btnGenerateReport.setOnClickListener(v -> generateMealReport());
    }

    private void generateMealReport() {
        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int total = 0;
                int breakfast = 0;
                int lunch = 0;
                int dinner = 0;

                for (DataSnapshot mealSnap : snapshot.getChildren()) {
                    String mealType = mealSnap.child("mealType").getValue(String.class);

                    if (mealType != null) {
                        total++;
                        switch (mealType.toLowerCase()) {
                            case "breakfast":
                                breakfast++;
                                break;
                            case "lunch":
                                lunch++;
                                break;
                            case "dinner":
                                dinner++;
                                break;
                        }
                    }
                }

                totalMealsText.setText("Total Meals: " + total);
                breakfastCountText.setText("Breakfast: " + breakfast);
                lunchCountText.setText("Lunch: " + lunch);
                dinnerCountText.setText("Dinner: " + dinner);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                totalMealsText.setText("Failed to load report.");
            }
        });
    }
}
