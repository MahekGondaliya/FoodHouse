package com.example.food_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_app.model.CategoryModel;
import com.example.food_app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    private final Context context;
    private final List<CategoryModel> flattenedList;


    public OrderDetailsAdapter(Context context, List<List<CategoryModel>> nestedList) {
        this.context = context;
        this.flattenedList = flattenNestedList(nestedList);

    }



    @NonNull
    @Override
    public OrderDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_details_recycler_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.ViewHolder holder, int position) {
        CategoryModel item = flattenedList.get(position);

        holder.itemName.setText(item.getName());
        holder.itemTotalPrice.setText(String.format("$%.2f", item.getTotalPrice()));
        holder.itemQuantity.setText(String.valueOf(item.getCount()));
        Picasso.get().load(item.getImageResource()).into(holder.itemImage);


    }

    @Override
    public int getItemCount() {
        return flattenedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName, itemTotalPrice, itemQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemName = itemView.findViewById(R.id.itemName);
            itemTotalPrice = itemView.findViewById(R.id.itemTotalPrice);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);

        }
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
}
