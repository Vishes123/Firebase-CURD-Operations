package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Regesstration extends AppCompatActivity {

    private EditText etName, etEmail, etCity, etNumber, etPassword, etConfirmPassword;
    private MaterialButton btnRegister;
    private TextView btnLoginmov;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regesstration);


        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etCity = findViewById(R.id.etCity);
        etNumber = findViewById(R.id.etNumber);
        etPassword = findViewById(R.id.etpassword);
        etConfirmPassword = findViewById(R.id.etConfirmpassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnLoginmov = findViewById(R.id.btnLoginmov);

        // Move to Login Page
        btnLoginmov.setOnClickListener(v -> {
            startActivity(new Intent(Regesstration.this, Login.class));
        });

        // Register Click
        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String number = etNumber.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(city) ||
                TextUtils.isEmpty(number) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            String uid = firebaseUser.getUid();

                            // Custom User Object
                            User user = new User(name, email, city, number);

                            firestore.collection("Users").document(uid).set(user)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this, "User Registered & Data Saved to Firestore!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Regesstration.this, Login.class));
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Firestore save failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Toast.makeText(this, "Auth Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
