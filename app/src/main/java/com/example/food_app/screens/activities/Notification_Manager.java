package com.example.food_app.screens.activities;

import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_app.R;
import com.example.food_app.model.NotificationModel;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Locale;

public class Notification_Manager extends AppCompatActivity {

    EditText edtTital, edtSubTital, edtDescripation;
    MaterialButton btnSend;

    TextView txtBack;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification_manager);

        edtTital = findViewById(R.id.edtTital);
        edtSubTital = findViewById(R.id.edtSubTital);
        edtDescripation = findViewById(R.id.edtDescripation);
        btnSend = findViewById(R.id.btnSend);
        txtBack = findViewById(R.id.txtBack);

        txtBack.setOnClickListener(v -> {
            finish();
        });

        btnSend.setOnClickListener(v -> {
            String title = edtTital.getText().toString().trim();
            String subtitle = edtSubTital.getText().toString().trim();
            String description = edtDescripation.getText().toString().trim();

            if (title.isEmpty() || subtitle.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = currentUser.getUid();
            String username = currentUser.getDisplayName();
            long timestamp = System.currentTimeMillis();


            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault());
            String readableDate = sdf.format(new Date(timestamp));

            NotificationModel notification = new NotificationModel(
                    title,
                    subtitle,
                    description,
                    timestamp,
                    readableDate,
                    userId,
                    username != null ? username : "Anonymous"
            );


            databaseReference = FirebaseDatabase.getInstance().getReference("notifications");

            String key = databaseReference.push().getKey();
            assert key != null;
            databaseReference.child(key).setValue(notification)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Sent to All User", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });


    }
}