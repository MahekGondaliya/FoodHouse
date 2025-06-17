package com.example.food_app.screens.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_app.R;
import com.example.food_app.adapter.ItemAdapter;
import com.example.food_app.adapter.RecycleViewBigAdapter;
import com.example.food_app.adapter.RecycleViewRecommendAdapter;
import com.example.food_app.adapter.SearchAdapter;
import com.example.food_app.sampledata.SampleDataForRecommend;
import com.example.food_app.utils.SearchData;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private SearchView searchView;
    private CardView searchCardView;
    private RecyclerView recyclerView, recyclerViewBig, recyclerViewRecommend, recyclerViewSearchView;
    private SearchAdapter searchAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);





        // Initialize Views
        initializeViews(view);

        // Set RecyclerView Layouts
        setupRecyclerView(recyclerView, LinearLayoutManager.HORIZONTAL);
        setupRecyclerView(recyclerViewBig, LinearLayoutManager.HORIZONTAL);
        setupRecyclerView(recyclerViewRecommend, LinearLayoutManager.HORIZONTAL);
        setupRecyclerView(recyclerViewSearchView, LinearLayoutManager.VERTICAL);

        // Load Sample Data
        setupAdapters();

        // Handle SearchView Actions
        setupSearchView();

        return view;
    }

    private void initializeViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerViewBig = view.findViewById(R.id.recyclerViewBig);
        recyclerViewRecommend = view.findViewById(R.id.recyclerViewRecommend);
        recyclerViewSearchView = view.findViewById(R.id.recyclerViewSearchView);
        searchView = view.findViewById(R.id.SearchView);
        searchCardView = view.findViewById(R.id.SearchCardView);
    }

    private void setupRecyclerView(RecyclerView recyclerView, int orientation) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), orientation, false));
    }

    private void setupAdapters() {
        recyclerView.setAdapter(new ItemAdapter(SampleDataForRecommend.getFoodItem()));
        recyclerViewBig.setAdapter(new RecycleViewBigAdapter(SampleDataForRecommend.getFoodItemsBig()));
        recyclerViewRecommend.setAdapter(new RecycleViewRecommendAdapter(SampleDataForRecommend.getFoodItemsRecommend()));

        searchAdapter = new SearchAdapter(getContext(), new ArrayList<>());
        recyclerViewSearchView.setAdapter(searchAdapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                updateSearchResults(newText);
                return false;
            }
        });
    }

    private void updateSearchResults(String query) {
        SearchData.getFood(query, filteredList -> {
            boolean hasResults = !filteredList.isEmpty();

            searchCardView.setVisibility(hasResults ? View.VISIBLE : View.GONE);
            recyclerViewSearchView.setVisibility(hasResults ? View.VISIBLE : View.GONE);

            if (hasResults) {
                searchAdapter.updateList(filteredList);
            }
        });
    }

}
