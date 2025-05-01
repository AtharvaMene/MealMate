package com.example.mealmate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editName;
    private ImageView editProfileImage;
    private Button saveChangesBtn, changeImageBtn;
    private FirebaseUser currentUser;
    private StorageReference storageReference;
    private FirebaseFirestore db;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editName = findViewById(R.id.editName);
        editProfileImage = findViewById(R.id.editProfileImage);
        saveChangesBtn = findViewById(R.id.saveChangesBtn);
        changeImageBtn = findViewById(R.id.changeImageBtn);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");
        db = FirebaseFirestore.getInstance();

        // Fetch current user data and set it in the fields
        if (currentUser != null) {
            editName.setText(currentUser.getDisplayName());
            // You can load the user's current profile image using Glide or Picasso
        }

        // Change profile image button
        changeImageBtn.setOnClickListener(v -> openImageChooser());

        // Save changes button
        saveChangesBtn.setOnClickListener(v -> saveProfileChanges());
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void saveProfileChanges() {
        String name = editName.getText().toString().trim();

        if (name.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update Firebase Authentication (Name)
        if (currentUser != null) {
            currentUser.updateProfile(new com.google.firebase.auth.UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Successfully updated name
                            Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });

            // If the image URI is selected, upload it to Firebase Storage
            if (imageUri != null) {
                StorageReference fileReference = storageReference.child(currentUser.getUid() + ".jpg");
                fileReference.putFile(imageUri)
                        .addOnSuccessListener(taskSnapshot -> {
                            fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                                // Save the profile image URL to Firestore
                                db.collection("users").document(currentUser.getUid())
                                        .update("profileImage", uri.toString())
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(EditProfileActivity.this, "Profile image updated", Toast.LENGTH_SHORT).show();
                                        });
                            });
                        })
                        .addOnFailureListener(e -> Toast.makeText(EditProfileActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            editProfileImage.setImageURI(imageUri); // Show the selected image
        }
    }
}
