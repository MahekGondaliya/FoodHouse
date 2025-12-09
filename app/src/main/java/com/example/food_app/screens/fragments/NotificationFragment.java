package com.example.food_app.screens.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.food_app.R;
import com.example.food_app.adapter.NotificationAdapter;
import com.example.food_app.model.NotificationModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;

    private LottieAnimationView loadingAnimation;

    private TextView No_Notification, notification;
    private List<NotificationModel> notificationList;

    private DatabaseReference databaseReference;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.notificationRecyclerView);
        No_Notification = view.findViewById(R.id.No_Notification);
        loadingAnimation = view.findViewById(R.id.loadingAnimation);
        notification = view.findViewById(R.id.notification);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        notificationList = new ArrayList<>();
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("notifications");

        loadNotifications();

        return view;
    }

    private void loadNotifications() {
        loadingAnimation.setVisibility(View.VISIBLE);

        // Load ONCE, not realtime, to avoid conflicts with adapter delete
        databaseReference.orderByChild("timestamp")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        notificationList.clear();

                        for (DataSnapshot snap : snapshot.getChildren()) {
                            NotificationModel model = snap.getValue(NotificationModel.class);
                            if (model != null) {
                                // Very important: set ID from key
                                model.setNotificationId(snap.getKey());
                                notificationList.add(model);
                            }
                        }

                        if (notificationList.isEmpty()) {
                            notification.setVisibility(View.GONE);
                            No_Notification.setVisibility(View.VISIBLE);
                        } else {
                            // latest first
                            Collections.reverse(notificationList);
                            notification.setVisibility(View.VISIBLE);
                            No_Notification.setVisibility(View.GONE);
                        }

                        adapter.notifyDataSetChanged();
                        loadingAnimation.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadingAnimation.setVisibility(View.GONE);
                        Toast.makeText(getContext(),
                                "Failed to load notifications.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
