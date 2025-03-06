package com.example.food_app.serverconfig;

import android.util.Log;

import com.example.food_app.model.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelperForOrder {

    private static final DatabaseReference mDatabase;

    // Static initialization block to prevent reinitialization issues
    static {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private FirebaseHelperForOrder() {
        // Private constructor to prevent instantiation
    }

    // Method to save the model to Firebase
    public static void saveModelToFirebase(OrderModel model) {
        // Get the current Firebase user ID
        String userId = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : model.getId();

        if (userId == null) {
            Log.e("FirebaseHelperForOrder", "User ID is null. Cannot save order.");
            return;
        }

        // Generate a unique order ID under the user's orders
        String orderId = mDatabase.child("Orders").child(userId).push().getKey();
        if (orderId == null) {
            Log.e("FirebaseHelperForOrder", "Failed to generate order ID.");
            return;
        }

        mDatabase.child("Orders").child(userId).child(orderId).setValue(model)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseHelperForOrder", "Order saved successfully"))
                .addOnFailureListener(e -> Log.e("FirebaseHelperForOrder", "Failed to save order: " + e.getMessage()));
    }
}
