package com.example.food_app.sampledata;

import android.util.Log;

import com.example.food_app.Interfaces.FirestoreCallback;
import com.example.food_app.model.CategoryModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SampleData {
    public static void getData(FirestoreCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<CategoryModel> modelList = new ArrayList<>();

        db.collection("categories-items")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        modelList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            CategoryModel model = document.toObject(CategoryModel.class);
                            modelList.add(model);
                        }
                        Log.d("Firestore", "Data retrieved successfully: " + modelList.size() + " items.");
                        callback.onDataFetched(modelList); // Notify the callback
                    } else {
                        Log.e("Firestore", "Error retrieving data", task.getException());
                    }
                });
    }
}
