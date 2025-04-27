package com.example.mealmate; // replace with your package name

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcomeMessage, tvMealBalance, tvSelectedMeal;
    private Spinner spinnerMealOptions;
    private Button btnViewMenu, btnGenerateQR;
    private ImageView ivQRCode;
    private RelativeLayout layoutQRContainer;

    private String selectedMeal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // This should be your XML layout name

        initViews();
        setupSpinner();
        setupListeners();
    }

    private void initViews() {
        tvWelcomeMessage = findViewById(R.id.tv_welcome_message);
        tvMealBalance = findViewById(R.id.tv_meal_balance);
        tvSelectedMeal = findViewById(R.id.tv_selected_meal);
        spinnerMealOptions = findViewById(R.id.spinner_meal_options);
        btnViewMenu = findViewById(R.id.btn_view_menu);
        btnGenerateQR = findViewById(R.id.btn_generate_qr);
        ivQRCode = findViewById(R.id.iv_qr_code);
        layoutQRContainer = findViewById(R.id.layout_qr_container);
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
                    ivQRCode.setImageDrawable(null); // Clear previous QR if any
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });
    }

    private void setupListeners() {
        btnViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Open today's menu (you can create a new activity later)
            }
        });

        btnGenerateQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateQRCode(selectedMeal);
            }
        });
    }

    private void generateQRCode(String data) {
        if (data.isEmpty() || data.equals("Select Meal")) {
            return;
        }

        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            android.graphics.Bitmap bitmap = barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, 400, 400);
            ivQRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
