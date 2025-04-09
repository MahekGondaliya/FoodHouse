package com.example.food_app.screens.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_app.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.EmailAuthProvider;


public class changePassword extends AppCompatActivity {

    EditText edtNewPassword,edtcurrentpassword,edtConfirmPassword;
    MaterialButton btnSave;
    TextView txtBack;

    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);
        edtcurrentpassword = findViewById(R.id.edtcurrentpassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnSave = findViewById(R.id.btnSave);
        txtBack = findViewById(R.id.txtBack);

        user = FirebaseAuth.getInstance().getCurrentUser();

        txtBack.setOnClickListener(v->{
            finish();
        });

        btnSave.setOnClickListener(v->{
            checkpassword();
        });

    }

    private void checkpassword() {
       String newPassword = edtNewPassword.getText().toString().trim();
       String confirmPassword = edtConfirmPassword.getText().toString().trim();
       String currentPassword = edtcurrentpassword.getText().toString().trim();

        if (validatePasswords(newPassword,confirmPassword)){
            reauthenticateAndChangePassword(currentPassword, newPassword);
        }
        return;


    }

    private void reauthenticateAndChangePassword(String currentPassword, String newPassword) {

        if (user == null || user.getEmail() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Now update password
                        user.updatePassword(newPassword)
                                .addOnCompleteListener(updateTask -> {
                                    if (updateTask.isSuccessful()) {
                                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                        Log.e("PasswordUpdate", updateTask.getException().getMessage());
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Reauthentication failed. Incorrect current password.", Toast.LENGTH_SHORT).show();
                        Log.e("Reauth", task.getException().getMessage());
                    }
                });

    }

    public boolean isPasswordValid(String password) {

        return password != null && password.length() >= 6 && password.matches(".*\\d.*");
    }

    public boolean validatePasswords(String newPassword, String confirmPassword) {
        if (!isPasswordValid(newPassword)) {
            Toast.makeText(this, "Password must be at least 6 characters and contain a digit.", Toast.LENGTH_SHORT).show();
            Log.e("PasswordCheck", "Password must be at least 6 characters and contain a digit.");
            return false;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "New password and confirm password do not match.", Toast.LENGTH_SHORT).show();
            Log.e("PasswordCheck", "New password and confirm password do not match.");
            return false;
        }

        return true;
    }
}