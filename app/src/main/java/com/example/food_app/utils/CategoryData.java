package com.example.food_app.utils;

import com.example.food_app.Interfaces.CategoryCallback;
import com.example.food_app.sampledata.SampleData;
import com.example.food_app.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryData {

    public static void getFood(String name, CategoryCallback callback) {
        SampleData.getData(categoryList -> {
            List<CategoryModel> filteredList = new ArrayList<>();

            for (CategoryModel item : categoryList) {
                if (item.getCategory().equalsIgnoreCase(name)) {
                    filteredList.add(item);
                }
            }

            callback.onCategoryFetched(filteredList); // Return filtered list via callback
        });
    }


}


