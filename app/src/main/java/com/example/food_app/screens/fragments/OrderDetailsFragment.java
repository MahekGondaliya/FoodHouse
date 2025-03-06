package com.example.food_app.screens.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_app.R;
import com.example.food_app.adapter.OrderDetailsAdapter;
import com.example.food_app.model.CategoryModel;
import com.example.food_app.model.OrderModel;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsFragment extends Fragment {

    TextView orderDetailId, subTotal, total;
    RecyclerView recyclerViewOrderDetails;
    OrderDetailsAdapter orderDetailsAdapter;

    private static final String ARG_ORDER = "order_model";
    private OrderModel orderModel;
    private List<CategoryModel> list = new ArrayList<>();

    public static OrderDetailsFragment newInstance(OrderModel order) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ORDER, order);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        orderDetailId = view.findViewById(R.id.orderDetailId);
        subTotal = view.findViewById(R.id.SubTotalDetails);
        total = view.findViewById(R.id.totalDetails);
        recyclerViewOrderDetails = view.findViewById(R.id.recyclerViewOrderDetails);

        recyclerViewOrderDetails.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            orderModel = getArguments().getParcelable(ARG_ORDER);

            if (orderModel != null) {
                orderDetailId.setText(orderModel.getId());

                // Flatten the nested list properly
                List<List<CategoryModel>> lists = orderModel.getNestedList();
                if (lists != null) {
                    list = flattenNestedList(lists);
                }

                // Calculate subtotal after list is initialized
                double subtotalValue = calculateSubtotal();
                subTotal.setText(String.format("$%.2f", subtotalValue));
                total.setText(String.format("$%.2f", subtotalValue + (subtotalValue * 0.05)));

                // Set up RecyclerView adapter
                orderDetailsAdapter = new OrderDetailsAdapter(getContext(), lists);
                recyclerViewOrderDetails.setAdapter(orderDetailsAdapter);
            }
        }

        return view;
    }

    private List<CategoryModel> flattenNestedList(List<List<CategoryModel>> nestedList) {
        List<CategoryModel> flatList = new ArrayList<>();
        if (nestedList != null) {
            for (List<CategoryModel> innerList : nestedList) {
                flatList.addAll(innerList);
            }
        }
        return flatList;
    }

    private double calculateSubtotal() {
        double subtotal = 0;
        for (CategoryModel categoryModel : list) {
            subtotal += categoryModel.getTotalPrice();
        }
        return subtotal;
    }
}
