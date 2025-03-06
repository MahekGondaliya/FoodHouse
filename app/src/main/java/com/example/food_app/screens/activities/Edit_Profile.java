package com.example.food_app.screens.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.UploadCallback;
import com.example.food_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Edit_Profile extends AppCompatActivity {

    CircleImageView profile_image;
    EditText edtUserName, edtPhoneNo, edtEmail;
    AppCompatButton btnEdit;
    TextView txtBack;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String imageUrl = "";
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Views
        profile_image = findViewById(R.id.profile_image);
        edtUserName = findViewById(R.id.edtUserName);
        edtPhoneNo = findViewById(R.id.edtPhoneNo);
        edtEmail = findViewById(R.id.edtEmail);
        btnEdit = findViewById(R.id.btnEdit);
        txtBack = findViewById(R.id.txtBack);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        } else {
            Log.e("FirebaseUpdate", "User not logged in!");
            return;
        }

        displayData();

        // Handle back button click - Navigate to ProfileFragment
        txtBack.setOnClickListener(v -> {
            finish();
        });

        // Handle profile image click
        profile_image.setOnClickListener(v -> openFileChooser());

        // Handle edit button click
        btnEdit.setOnClickListener(v -> {
            String username = edtUserName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String phoneno = edtPhoneNo.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || phoneno.isEmpty()) {
                Toast.makeText(Edit_Profile.this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            updateUserData(uid, username, imageUrl, phoneno);
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            uploadImageToCloudinary(imageUri);
        }
    }

    private void uploadImageToCloudinary(Uri fileUri) {
        Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show();

        MediaManager.get().upload(fileUri)
                .option("folder", "profileImg")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {}

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map result) {
                        imageUrl = (String) result.get("secure_url");
                        Picasso.get().load(imageUrl).into(profile_image);
                        Toast.makeText(Edit_Profile.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(String requestId, com.cloudinary.android.callback.ErrorInfo error) {
                        Toast.makeText(Edit_Profile.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, com.cloudinary.android.callback.ErrorInfo error) {}
                }).dispatch();
    }

    public void displayData() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    String phoneNo = snapshot.child("phoneNo").getValue(String.class);

                    edtUserName.setText(username != null ? username : "");
                    edtEmail.setText(email != null ? email : "");
                    edtPhoneNo.setText(phoneNo != null ? phoneNo : "");

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Picasso.get().load(imageUrl).into(profile_image);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseData", "Error: " + error.getMessage());
            }
        });
    }

    public void updateUserData(String userId, String newUsername, String newImageUrl, String newPhoneNo) {
        if (userId == null || userId.isEmpty()) {
            Log.e("FirebaseUpdate", "User ID is null or empty! Update failed.");
            return;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("username", newUsername);
//        updates.put("email", newEmail);
        updates.put("imageUrl", newImageUrl);
        updates.put("phoneNo", newPhoneNo);

        databaseReference.updateChildren(updates)
                .addOnSuccessListener(aVoid -> {

                    Toast.makeText(Edit_Profile.this, "Update Successfully...", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseUpdate", "Update Failed: " + e.getMessage());
                    Toast.makeText(Edit_Profile.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
