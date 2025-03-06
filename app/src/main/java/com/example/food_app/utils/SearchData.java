package com.example.food_app.utils;

import com.example.food_app.Interfaces.SearchCallback;
import com.example.food_app.sampledata.SampleData;
import com.example.food_app.model.CategoryModel;
import java.util.ArrayList;
import java.util.List;

public class SearchData {

    // Callback interface for async search results

    public static void getFood(String searchQuery, SearchCallback callback) {
        if (searchQuery.isEmpty()) {
            callback.onSearchResult(new ArrayList<>());
            return;
        }

        SampleData.getData(categoryList -> {
            List<CategoryModel> filteredList = new ArrayList<>();

            for (CategoryModel item : categoryList) {
                if (item.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                    filteredList.add(item);
                }
            }

            // Return filtered results via callback
            callback.onSearchResult(filteredList);
        });
    }
}
