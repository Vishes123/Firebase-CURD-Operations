package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class FatchData extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private EditText etSearch;
    private TextView tvName, tvEmail, tvCity, tvNumber;
    private MaterialButton btnSearch;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fatch_data);

        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvCity = findViewById(R.id.tvCity);
        tvNumber = findViewById(R.id.tvNumber);

        firestore = FirebaseFirestore.getInstance();

        btnSearch.setOnClickListener(v -> {
            String input = etSearch.getText().toString().trim();
            if (input.isEmpty()) {
                Toast.makeText(this, "Enter Email or Phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            searchUser(input);
        });
    }

    private void searchUser(String input) {
        firestore.collection("Users")
                .whereEqualTo("email", input)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            showUser(doc.toObject(User.class));
                        }
                    } else {
                        searchByPhone(input);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void searchByPhone(String phone) {
        firestore.collection("Users")
                .whereEqualTo("number", phone)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            showUser(doc.toObject(User.class));
                        }
                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                        clearTable();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showUser(User user) {
        tvName.setText(user.name);
        tvEmail.setText(user.email);
        tvCity.setText(user.city);
        tvNumber.setText(user.number);
    }

    private void clearTable() {
        tvName.setText("");
        tvEmail.setText("");
        tvCity.setText("");
        tvNumber.setText("");
    }
}
