package com.example.curd;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText editText1;
    EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Bind views properly
        editText1 = findViewById(R.id.editTextText);
        editText2 = findViewById(R.id.editTextAuthor);
        button = findViewById(R.id.button);

        // Button click listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = editText1.getText().toString().trim();
                String a = editText2.getText().toString().trim();

                if (q.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Quote Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (a.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Author Field is Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                addQuatToDb(q, a);
            }
        });

        // Inset Handling (optional for newer Android versions)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Add data to Firebase Realtime Database
    private void addQuatToDb(String q, String a) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("quote", q);
        hm.put("author", a);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("author");
        String key = myRef.push().getKey();
        hm.put("key", key);

        myRef.child(key).setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "Quote Added Successfully!", Toast.LENGTH_SHORT).show();
                editText1.getText().clear(); 
                editText2.getText().clear();
            }
        });
    }
}
