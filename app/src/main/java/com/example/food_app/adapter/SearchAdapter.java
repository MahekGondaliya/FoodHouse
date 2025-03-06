package com.example.food_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_app.utils.AddToCart;
import com.example.food_app.model.CategoryModel;
import com.example.food_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    public Context context;
    public List<CategoryModel> searchList;

    public SearchAdapter(Context context, List<CategoryModel> itemList) {
        this.context = context;
        this.searchList = itemList;
    }


    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_recyclerview_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        CategoryModel cartModel = searchList.get(position);
        holder.itemName.setText(cartModel.getName());
        holder.itemPrice.setText(String.valueOf(cartModel.getPrice()));
        Picasso.get().load(cartModel.getImageResource()).into(holder.itemImage);

        holder.AddCart.setOnClickListener(v->{
            AddToCart.addToCart(cartModel,context);
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public void updateList(List<CategoryModel> newList) {
        searchList.clear();
        searchList = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView itemName, itemPrice;

         AppCompatButton AddCart;

        public ViewHolder(@NonNull View view) {
            super(view);
            itemImage = view.findViewById(R.id.itemImage);
            itemName = view.findViewById(R.id.itemName);
            itemPrice = view.findViewById(R.id.itemPrice);
            AddCart = view.findViewById(R.id.AddCart);

        }
    }
}
