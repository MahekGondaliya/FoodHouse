package com.example.food_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_app.utils.AddToCart;
import com.example.food_app.R;
import com.example.food_app.model.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private final List<CategoryModel> foodList;
    private final Context context;


    // Pass context explicitly to avoid issues
    public CategoryRecyclerViewAdapter(List<CategoryModel> foodList, Context context) {
        this.foodList = foodList;
        this.context = context;


    }

    @NonNull
    @Override
    public CategoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerViewAdapter.ViewHolder holder, int position) {
        CategoryModel food = foodList.get(position);
        holder.foodName.setText(food.getName());
        holder.foodPrice.setText("$" + food.getPrice());
        Picasso.get().load(food.getImageResource()).into(holder.foodImage);

        holder.addButton.setOnClickListener(v -> {
            AddToCart.addToCart(food,context);
        });

    }

    @Override
    public int getItemCount() {
        return foodList.size(); // Fixed issue
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImage;
        TextView foodName, foodPrice;
        Button addButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImage = itemView.findViewById(R.id.food_image);
            foodName = itemView.findViewById(R.id.food_name);
            foodPrice = itemView.findViewById(R.id.food_price);
            addButton = itemView.findViewById(R.id.add_button);
        }
    }





}
