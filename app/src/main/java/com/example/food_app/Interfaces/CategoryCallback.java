package com.example.food_app.Interfaces;

import com.example.food_app.model.CategoryModel;

import java.util.List;

public interface CategoryCallback {
    void onCategoryFetched(List<CategoryModel> filteredList);
}

