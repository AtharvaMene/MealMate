package com.example.mealmate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileName, profileEmail, contactInfo, feedbackInfo;
    private ImageView profileImage;
    private Button editProfileBtn, logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileImage = findViewById(R.id.profileImage);
        editProfileBtn = findViewById(R.id.editProfileBtn);
        logoutBtn = findViewById(R.id.logoutBtn);
        contactInfo = findViewById(R.id.contactInfo);
        feedbackInfo = findViewById(R.id.feedbackInfo);

        // Fetch user data from Firebase
        fetchUserProfile();

        // Edit profile button action
        editProfileBtn.setOnClickListener(v -> {
            // Start EditProfileActivity
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        });

        // Logout button action
        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });

        // Contact info click action
        contactInfo.setOnClickListener(v -> {
            // Trigger an email intent
            sendContactEmail();
        });

        // Feedback info click action
        feedbackInfo.setOnClickListener(v -> {
            // Show a toast or trigger a feedback activity
            openFeedbackForm();
        });
    }

    private void fetchUserProfile() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            profileName.setText(currentUser.getDisplayName());
            profileEmail.setText(currentUser.getEmail());
            // Optionally, you can load the profile image here (use libraries like Glide or Picasso)
        }
    }

    private void sendContactEmail() {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:support@mealmate.com")); // Use your support email
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    private void openFeedbackForm() {

        Toast.makeText(ProfileActivity.this, "Opening Feedback Form", Toast.LENGTH_SHORT).show();

    }
}
