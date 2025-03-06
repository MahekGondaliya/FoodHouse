package com.example.food_app.serverconfig;
import android.util.Log;
import androidx.annotation.NonNull;

import com.example.food_app.model.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FirebaseHelperToGetOrder {

    private static final DatabaseReference mDatabase;

    static {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private FirebaseHelperToGetOrder() {
        // Private constructor to prevent instantiation
    }

    // Retrieve all orders for the current user
    public static void getAllOrdersForUser(FirebaseOrdersCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (userId == null) {
            Log.e("FirebaseHelperForOrder", "User ID is null. Cannot retrieve orders.");
            callback.onFailure("User not logged in.");
            return;
        }

        DatabaseReference ordersRef = mDatabase.child("Orders").child(userId);

        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<OrderModel> orderList = new ArrayList<>();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    String orderId = orderSnapshot.getKey(); // Get order ID

                    OrderModel order = orderSnapshot.getValue(OrderModel.class);
                    if (order != null) {
                        order.setId(orderId); // Assign the order ID to the object
                        orderList.add(order);
                    }
                }
                callback.onSuccess(orderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseHelperForOrder", "Failed to retrieve orders: " + error.getMessage());
                callback.onFailure(error.getMessage());
            }
        });
    }

    // Interface for callback handling
    public interface FirebaseOrdersCallback {
        void onSuccess(List<OrderModel> orders);
        void onFailure(String errorMessage);
    }
}
