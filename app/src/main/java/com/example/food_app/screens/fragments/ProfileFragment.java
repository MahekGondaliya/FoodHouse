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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.food_app.R;
import com.example.food_app.adapter.OptionsAdapter;
import com.example.food_app.model.Option;
import com.example.food_app.screens.activities.AddCategoryActivity;
import com.example.food_app.screens.activities.Edit_Profile;
import com.example.food_app.screens.activities.Information;
import com.example.food_app.screens.activities.Notification_Manager;
import com.example.food_app.screens.activities.Sign_In;
import com.example.food_app.screens.activities.changePassword;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private TextView txtUserName;
    private CircleImageView profileImage;
    private LottieAnimationView loadingAnimation;
    private FirebaseAuth auth;
    private String uid;
    private Group maingroup;
    private RecyclerView recycler;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            redirectToLogin();
            return null;
        }

        uid = user.getUid();

        // Initialize Views
        txtUserName = view.findViewById(R.id.txtUserName);
        profileImage = view.findViewById(R.id.profile_image);
        loadingAnimation = view.findViewById(R.id.loadingAnimation);
        maingroup = view.findViewById(R.id.maingroup);
        recycler = view.findViewById(R.id.optionsRecycler);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load Profile Data
        loadUserData();

        return view;
    }

    private List<Option> getProfileOptions(String role) {
        List<Option> options = new ArrayList<>();

        options.add(new Option(R.drawable.edit, "Edit Profile", () ->
                startActivity(new Intent(getContext(), Edit_Profile.class))));

        if ("admin".equals(role)) {
            options.add(new Option(R.drawable.add, "Add Item", () ->
                    startActivity(new Intent(getContext(), AddCategoryActivity.class))));
        }

        if ("admin".equals(role)) {
            options.add(new Option(R.drawable.add, "Notification", () ->
                    startActivity(new Intent(getContext(), Notification_Manager.class))));
        }

        options.add(new Option(R.drawable.key, "Change Password", () ->
                startActivity(new Intent(getContext(), changePassword.class))));

        options.add(new Option(R.drawable.information, "Information", () ->
                startActivity(new Intent(getContext(), Information.class))));

        options.add(new Option(R.drawable.update, "Update", () -> {
            // Add update action here
        }));

        options.add(new Option(R.drawable.logout, "Logout", this::showLogoutDialog));

        return options;
    }

    private void loadUserData() {
        showLoading(true);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showLoading(false);
                if (!isAdded()) return;

                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    String role = snapshot.child("role").getValue(String.class);

                    txtUserName.setText(username != null ? username : "User");

                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Picasso.get()
                                .load(imageUrl)
                                .placeholder(R.drawable.default_profile)
                                .error(R.drawable.default_profile)
                                .into(profileImage);
                    }

                    // Set options based on role
                    recycler.setAdapter(new OptionsAdapter(getProfileOptions(role)));

                } else {
                    Toast.makeText(getContext(), "User data not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showLoading(false);
                Log.e("ProfileFragment", "Firebase Error: " + error.getMessage());
                Toast.makeText(getContext(), "Error loading data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLogoutDialog() {
        if (!isAdded()) return;

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_logout_confirmation);
        dialog.setCancelable(false);

        TextView txtMessage = dialog.findViewById(R.id.txtLogoutMessage);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);

        if (dialog.getWindow() != null) {
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(dialog.getWindow().getDecorView(), "alpha", 0f, 1f);
            fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
            fadeIn.setDuration(500);
            fadeIn.start();
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnConfirm.setOnClickListener(v -> {
            logoutUser();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void logoutUser() {
        auth.signOut();
        Toast.makeText(getContext(), "Logged out successfully!", Toast.LENGTH_SHORT).show();
        redirectToLogin();
    }

    private void redirectToLogin() {
        if (!isAdded()) return;
        Intent intent = new Intent(getContext(), Sign_In.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        if (getActivity() != null) getActivity().finish();
    }

    private void showLoading(boolean isLoading) {
        loadingAnimation.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        maingroup.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }
}
