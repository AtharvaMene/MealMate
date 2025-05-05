package com.example.mealmate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class EditProfileActivity extends AppCompatActivity {

    private EditText updateName, updateEmail;
    private Button saveUpdatesBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);  // Correct layout for EditProfileActivity

        // Initialize views
        updateName = findViewById(R.id.updateName);
        updateEmail = findViewById(R.id.updateEmail);
        saveUpdatesBtn = findViewById(R.id.saveUpdatesBtn);
        cancelBtn = findViewById(R.id.cancelBtn);

        // Fetch user data from Firebase
        fetchUserProfile();

        // Save updates button action
        saveUpdatesBtn.setOnClickListener(v -> {
            String newName = updateName.getText().toString();
            String newEmail = updateEmail.getText().toString();

            if (!newName.isEmpty() && !newEmail.isEmpty()) {
                // Update profile name and email in Firebase
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // Update name
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(newName)
                            .build();
                    currentUser.updateProfile(profileUpdates);

                    // Update email
                    currentUser.updateEmail(newEmail).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Successfully updated, navigate back to profile
                            Toast.makeText(EditProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            finish();  // Close activity
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(EditProfileActivity.this, "Please fill out both fields", Toast.LENGTH_SHORT).show();
            }
        });

        // Cancel button action (Go back without saving)
        cancelBtn.setOnClickListener(v -> finish());
    }

    private void fetchUserProfile() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            updateName.setText(currentUser.getDisplayName());
            updateEmail.setText(currentUser.getEmail());
        }
    }
}
