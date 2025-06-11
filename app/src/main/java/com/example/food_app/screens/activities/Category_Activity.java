package com.example.food_app.screens.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.food_app.R;
import com.example.food_app.adapter.CategoryRecyclerViewAdapter;
import com.example.food_app.utils.CategoryData;

public class Category_Activity extends AppCompatActivity {
    RecyclerView recyclerViewCategory;
    String category;
    CategoryRecyclerViewAdapter adapter;
    LottieAnimationView loadingOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);

        recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        loadingOrder = findViewById(R.id.loadingOrder);


        int spanCount;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanCount = 4; // More columns in landscape
        } else {
            spanCount = 2; // Fewer columns in portrait
        }

        recyclerViewCategory.setLayoutManager(new GridLayoutManager(this, spanCount));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category = bundle.getString("category");
        }else {
            category = null;
        }

        fetchCategoryData();
    }

    private void fetchCategoryData() {
        loadingOrder.setVisibility(View.VISIBLE);
        CategoryData.getFood(category, filteredList -> {
            if (!filteredList.isEmpty()) {
                loadingOrder.setVisibility(View.GONE);
                // Update RecyclerView
                runOnUiThread(() -> {
                    adapter = new CategoryRecyclerViewAdapter(filteredList, Category_Activity.this);
                    recyclerViewCategory.setAdapter(adapter);
                });

            } else {
                Log.e("CategoryActivity", "No food items found for category: " + category);
            }
        });
    }
}
