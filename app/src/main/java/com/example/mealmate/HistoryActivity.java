package com.example.mealmate;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mealmate.adapters.MealAdapter;
import com.example.mealmate.model.MealHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MealAdapter adapter;
    private List<MealHistory> historyList;
    private List<MealHistory> allHistoryList = new ArrayList<>();

    private FirebaseUser user;
    private DatabaseReference historyRef;

    private Spinner spinnerMealFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        historyList = new ArrayList<>();
        adapter = new MealAdapter(historyList);
        recyclerView.setAdapter(adapter);

        spinnerMealFilter = findViewById(R.id.spinnerMealFilter);
        ArrayAdapter<String> mealFilterAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"All", "Breakfast", "Lunch", "Dinner"});
        mealFilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealFilter.setAdapter(mealFilterAdapter);

        spinnerMealFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = parent.getItemAtPosition(position).toString();
                filterMealHistory(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            historyRef = FirebaseDatabase.getInstance().getReference("MealHistory").child(user.getUid());
            loadHistory();
        } else {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadHistory() {
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                allHistoryList.clear();
                historyList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot entry : snapshot.getChildren()) {
                        MealHistory history = entry.getValue(MealHistory.class);
                        if (history != null) {
                            allHistoryList.add(history);
                        }
                    }
                    // Initially show all
                    filterMealHistory("All");
                } else {
                    Toast.makeText(HistoryActivity.this, "No meal history found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(HistoryActivity.this, "Failed to load history.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterMealHistory(String mealType) {
        historyList.clear();
        if (mealType.equals("All")) {
            historyList.addAll(allHistoryList);
        } else {
            for (MealHistory m : allHistoryList) {
                if (m.getMealType().equalsIgnoreCase(mealType)) {
                    historyList.add(m);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
