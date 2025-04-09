package com.example.food_app.screens.fragments;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.example.food_app.R;
import com.example.food_app.screens.activities.Information;
import com.example.food_app.screens.activities.changePassword;
import com.example.food_app.screens.activities.AddCategoryActivity;
import com.example.food_app.screens.activities.Edit_Profile;
import com.example.food_app.screens.activities.Sign_In;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    TextView txtUserName;
    CardView edtProfile, btnLogout, Admin_Add_categort,changePass,Information;
    CircleImageView profile_image;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    String uid = auth.getCurrentUser().getUid();
    Group mainContent;
    LottieAnimationView loadingAnimation;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize UI elements
        txtUserName = view.findViewById(R.id.txtUserName);
        btnLogout = view.findViewById(R.id.btnLogout);
        edtProfile = view.findViewById(R.id.edtProfile);
        profile_image = view.findViewById(R.id.profile_image);
        Admin_Add_categort = view.findViewById(R.id.Admin_Add_categort);
        loadingAnimation = view.findViewById(R.id.loadingAnimation);
        mainContent = view.findViewById(R.id.mainContent);
        changePass = view.findViewById(R.id.changePassword);
        Information = view.findViewById(R.id.Information);


        // Open Edit Profile Activity
        edtProfile.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), Edit_Profile.class));
        });

        // Logout Button
        btnLogout.setOnClickListener(v -> {
            showLogoutDialog();
        });

        //Open add categort activity
        Admin_Add_categort.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), AddCategoryActivity.class));
        });

        changePass.setOnClickListener(v->{
            startActivity(new Intent(getContext(), changePassword.class));
        });

        Information.setOnClickListener(v->{
            startActivity(new Intent(getContext(), Information.class));
        });

        // Display user data
        displayData();

        return view;
    }


    public void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(getContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show();

        // Redirect to Login Activity (Optional)
        Intent intent = new Intent(getContext(), Sign_In.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void displayData() {
        // Show Loader & Hide UI
        loadingAnimation.setVisibility(View.VISIBLE);
        mainContent.setVisibility(View.GONE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loadingAnimation.setVisibility(View.GONE);
                    mainContent.setVisibility(View.VISIBLE);
                    String username = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    String phoneNo = snapshot.child("phoneNo").getValue(String.class);
                    String role = snapshot.child("role").getValue(String.class);

                    if (role.equals("admin") && role != null) {
                        Admin_Add_categort.setVisibility(View.VISIBLE);
                    } else {
                        Admin_Add_categort.setVisibility(View.GONE);
                    }


                    if (username != null && !username.isEmpty()) {
                        txtUserName.setText(username);
                    }

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

    private void showLogoutDialog() {
        // Create the dialog
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_logout_confirmation);
        dialog.setCancelable(false); // Prevent closing by tapping outside

        // Get the views
        TextView txtMessage = dialog.findViewById(R.id.txtLogoutMessage);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);


        // Apply the fade-in animation to the dialog
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(dialog.getWindow().getDecorView(), "alpha", 0f, 1f);
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeIn.setDuration(500);
        fadeIn.start();

        // Button actions
        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            logoutUser();
            dialog.dismiss();
        });


        dialog.show();
    }
}