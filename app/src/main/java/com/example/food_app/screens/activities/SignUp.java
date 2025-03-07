package com.example.food_app.screens.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.UploadCallback;
import com.example.food_app.MainActivity;
import com.example.food_app.R;
import com.example.food_app.model.Usermodel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    TextView txtLogin;
    Button btnRegister;

    EditText edtPassword, edtEmail, edtPhoneNo, edtUserName;

    CircleImageView profile_image;

    FirebaseAuth auth;
    String imageUrl;


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


        profile_image.setOnClickListener(v -> openFileChooser());

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp.this, Sign_In.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(v -> {
            registerUser();
        });
    }


    public static boolean isValidIndianNumber(String number) {
        String regex = "^[6789]\\d{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    private void registerUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String username = edtUserName.getText().toString().trim();
        String phoneno = edtPhoneNo.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isValidIndianNumber(phoneno)){
            Toast.makeText(this, "Enter Valid Phone Number..", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    saveUserToFirebase(username,email,imageUrl,phoneno);
                    Toast.makeText(SignUp.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUp.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(SignUp.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
            uploadImageToCloudinary(imageUri);
        }
    }

    private void uploadImageToCloudinary(Uri fileUri) {

        Toast.makeText(SignUp.this, "Uploading...", Toast.LENGTH_SHORT).show();

        MediaManager.get().upload(fileUri)
                .option("folder", "profileImg")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        // Upload started
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {
                        // Progress update
                    }

                    @Override
                    public void onSuccess(String requestId, Map result) {
                        imageUrl = (String) result.get("secure_url");
                        Picasso.get().load(imageUrl).into(profile_image);
                        Toast.makeText(SignUp.this, "Upload Successful!", Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onError(String requestId, com.cloudinary.android.callback.ErrorInfo error) {
                        Toast.makeText(SignUp.this, "Upload Failed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, com.cloudinary.android.callback.ErrorInfo error) {
                        // Handle reschedule
                    }
                }).dispatch();
    }

    public void saveUserToFirebase(String username, String email, String imageUrl,String phoneno) {
        // Get Firebase Database Reference
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        // Generate a unique ID for each user
        String userId = auth.getCurrentUser().getUid();

        // Create a new User object
        Usermodel user = new Usermodel(username, email, imageUrl,phoneno);

        // Store data in Firebase
        if (userId != null) {
            databaseReference.child(userId).setValue(user);

        }
    }

}
