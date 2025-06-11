package com.example.food_app.screens.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.UploadCallback;
import com.example.food_app.MainActivity;
import com.example.food_app.R;
import com.example.food_app.model.Usermodel;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private String imageUrl;
    LottieAnimationView loader;
    LinearLayout mainlayout;

    EditText edtUserName, edtPhoneNo, edtEmail, edtPassword;
    Button btnRegister;
    TextView txtLogin;
    CircleImageView profile_image;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        txtLogin = findViewById(R.id.txtLogin);
        btnRegister = findViewById(R.id.btnRegister);
        edtUserName = findViewById(R.id.edtUserName);
        edtPhoneNo = findViewById(R.id.edtPhoneNo);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        profile_image = findViewById(R.id.profile_image);
        loader = findViewById(R.id.loader);
        mainlayout = findViewById(R.id.mainlayout);

        profile_image.setOnClickListener(v -> openFileChooser());

        txtLogin.setOnClickListener(v -> startActivity(new Intent(SignUp.this, Sign_In.class)));

        btnRegister.setOnClickListener(v -> {
            if (imageUri == null) {
                Toast.makeText(this, "Please select a profile image", Toast.LENGTH_SHORT).show();
            } else {
                if (validateInputs()) {
                    loader.setVisibility(View.VISIBLE);
                    mainlayout.setVisibility(View.GONE);
                    uploadImageToCloudinary(imageUri);
                }
            }
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
            profile_image.setImageURI(imageUri); // Just preview
        }
    }

    private void uploadImageToCloudinary(Uri fileUri) {
        Toast.makeText(SignUp.this, "Uploading Image...", Toast.LENGTH_SHORT).show();

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
                        registerUser(); // Now register
                    }

                    @Override
                    public void onError(String requestId, com.cloudinary.android.callback.ErrorInfo error) {

                        Toast.makeText(SignUp.this, "Image Upload Failed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, com.cloudinary.android.callback.ErrorInfo error) {}
                }).dispatch();
    }

    private void registerUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String username = edtUserName.getText().toString().trim();
        String phoneno = edtPhoneNo.getText().toString().trim();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        saveUserToFirebase(username, email, imageUrl, phoneno);
                        Toast.makeText(SignUp.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUp.this, MainActivity.class));
                        finish();
                    } else {
                        loader.setVisibility(View.GONE);
                        mainlayout.setVisibility(View.VISIBLE);
                        Toast.makeText(SignUp.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void saveUserToFirebase(String username, String email, String imageUrl, String phoneno) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        String userId = auth.getCurrentUser().getUid();
        Usermodel user = new Usermodel(username, email, imageUrl, phoneno);

        if (userId != null) {
            databaseReference.child(userId).setValue(user);
        }
    }

    private boolean validateInputs() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String username = edtUserName.getText().toString().trim();
        String phoneno = edtPhoneNo.getText().toString().trim();

        if (username.isEmpty() || phoneno.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid Email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidIndianNumber(phoneno)) {
            Toast.makeText(this, "Enter valid 10-digit Indian phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidPassword(password)) {
            Toast.makeText(this, "Password must be at least 6 characters and contain a letter and number", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean isValidIndianNumber(String number) {
        String regex = "^[6-9]\\d{9}$";
        return number.matches(regex);
    }

    private boolean isValidPassword(String password) {
        // At least one letter, one digit, and 6 characters minimum
        String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$";
        return password.matches(regex);
    }
}
