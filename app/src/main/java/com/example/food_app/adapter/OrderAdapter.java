package com.example.food_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_app.screens.fragments.OrderDetailsFragment;
import com.example.food_app.model.OrderModel;
import com.example.food_app.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context context;
    private final List<OrderModel> orderModelList;



    public OrderAdapter(Context context, List<OrderModel> orderModelList) {
        this.context = context;
        this.orderModelList = orderModelList;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_recyclerview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        OrderModel model = orderModelList.get(position);
        int orderNumber = orderModelList.size() - position;
        holder.orderId.setText("Order No: " + orderNumber);
        holder.orderDate.setText("Date: " + model.getCurrentDateTime());

        holder.orderDetails.setOnClickListener(view -> {
            // Create fragment instance and pass the whole OrderModel
            OrderDetailsFragment orderDetailsFragment = OrderDetailsFragment.newInstance(model);

            // Open fragment
            FragmentManager fragmentManager = ((AppCompatActivity) view.getContext()).getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, orderDetailsFragment) // Ensure this ID exists in activity XML
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate;
        AppCompatButton orderDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderDetails = itemView.findViewById(R.id.orderDetails);
        }
    }
}
