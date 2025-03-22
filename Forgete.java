package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Forgete extends AppCompatActivity {

    private TextInputEditText etEmailf;
    private MaterialButton Fbutton;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgete);

        etEmailf = findViewById(R.id.etEmailf);
        Fbutton = findViewById(R.id.Fbutton);
        firebaseAuth = FirebaseAuth.getInstance();

        Fbutton.setOnClickListener(v -> {
            String email = etEmailf.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Forgete.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Forgete.this, "Reset link sent to your email", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(Forgete.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
