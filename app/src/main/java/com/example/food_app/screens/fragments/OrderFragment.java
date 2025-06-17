package com.example.food_app.screens.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.food_app.R;
import com.example.food_app.adapter.OrderAdapter;
import com.example.food_app.model.OrderModel;
import com.example.food_app.serverconfig.FirebaseHelperToGetOrder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class OrderFragment extends Fragment {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    String uid = auth.getCurrentUser().getUid();
    TextView ordersHide;
    RecyclerView orderRecyclerView;
    OrderAdapter orderAdapter;

    LottieAnimationView loadingOrder;


    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);

        orderRecyclerView = view.findViewById(R.id.orderRecyclerView);
        ordersHide = view.findViewById(R.id.ordersHide);
        loadingOrder = view.findViewById(R.id.loadingOrder);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        orderRecyclerView.setLayoutManager(linearLayoutManager);

        getOrderList();
        return view;
    }

    private boolean getOrderList() {
         loadingOrder.setVisibility(View.VISIBLE);
        FirebaseHelperToGetOrder.getAllOrdersForUser(new FirebaseHelperToGetOrder.FirebaseOrdersCallback() {

            @Override
            public void onSuccess(List<OrderModel> orders) {
                loadingOrder.setVisibility(View.GONE);
                if (orders.isEmpty()) {
                    ordersHide.setVisibility(View.VISIBLE);
                } else {
                    Collections.reverse(orders); // Show latest orders at the top
                    orderAdapter = new OrderAdapter(getContext(), orders);
                    orderRecyclerView.setAdapter(orderAdapter);
                }
            }


            @Override
            public void onFailure(String errorMessage) {
                Log.e("FirebaseData", "Error fetching orders: " + errorMessage);
            }
        });

         return true;

    }
}