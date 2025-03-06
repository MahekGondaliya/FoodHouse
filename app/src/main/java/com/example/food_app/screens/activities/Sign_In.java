package com.example.food_app.screens.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_app.MainActivity;
import com.example.food_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_In extends AppCompatActivity {
 TextView txtRegister;
 Button btnLogin;
 EditText loginPassword,loginEmail;

 FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        auth = FirebaseAuth.getInstance();

        txtRegister = findViewById(R.id.txtRegister);
        btnLogin = findViewById(R.id.btnLogin);
        loginEmail = findViewById(R.id.edtloginEmail);
        loginPassword = findViewById(R.id.edtloginPassword);
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.logo);


        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sign_In.this, SignUp.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(v->{
            loginuser();
        });

    }

    private void loginuser() {

        String emailText = loginEmail.getText().toString().trim();
        String passwordText = loginPassword.getText().toString().trim();

        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(Sign_In.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Sign_In.this, MainActivity.class));
                            finish();
                        }
                    } else {
                        Toast.makeText(Sign_In.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}