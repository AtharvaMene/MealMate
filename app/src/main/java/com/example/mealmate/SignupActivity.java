package com.example.mealmate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText fullName,email,password,confirmPassword;
    private Button signUpButton;

    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        fullName = findViewById(R.id.fullName);
        email=findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUpButton = findViewById(R.id.signUpButton);
        loginLink = findViewById(R.id.loginLink);
        mAuth = FirebaseAuth.getInstance();

        signUpButton.setOnClickListener(view -> {
            String userName = fullName.toString().trim();
            String userEmail = email.toString().trim();
            String userPassword = password.toString().trim();
            String confirmPass = confirmPassword.toString().trim();


            if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)
                    || TextUtils.isEmpty(confirmPass)) {

                Toast.makeText(this,"All Fields are Mandatory!", Toast.LENGTH_SHORT).show();

            }
        });

        mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(this, "Registration Succesfull!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else {
                Toast.makeText(this, "Registration Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        loginLink.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.class));
            finish();
        });
    }
}