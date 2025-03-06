package com.example.food_app.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food_app.CartDao;
import com.example.food_app.CartDatabase;
import com.example.food_app.model.CategoryModel;
import com.example.food_app.R;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<CategoryModel> cartModelList;
    TextView cartHide;

    Context context;


    public CartAdapter(List<CategoryModel> categoryModelList, TextView  llCartHide) {
        this.cartModelList = categoryModelList;
        this.cartHide = llCartHide;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_recyclerview, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        CategoryModel cartModel = cartModelList.get(position);
        CartDatabase db = CartDatabase.getInstance(context);
        CartDao cartDao = db.cartDao();
        Handler handler = new Handler(Looper.getMainLooper());
        ExecutorService executor = Executors.newSingleThreadExecutor();

        holder.itemName.setText(cartModel.getName());
        holder.itemPrice.setText(String.valueOf(cartModel.getPrice()));
        holder.itemQuantity.setText(String.valueOf(cartModel.getCount()));
        Picasso.get().load(cartModel.getImageResource()).into(holder.itemImage);

        holder.btnIncrease.setOnClickListener(v -> {

            executor.execute(() -> {
                cartDao.increaseCount(cartModel.getId());
                cartModel.setCount(cartModel.getCount() + 1);
                handler.post(() -> {
                    notifyItemChanged(position);
                });
            });

        });

        holder.btnDecrease.setOnClickListener(v -> {

            if (cartModel.getCount() > 1) {
                executor.execute(() -> {
                    cartDao.decreaseCount(cartModel.getId());
                    cartModel.setCount(cartModel.getCount() - 1);
                    handler.post(() -> {
                        notifyItemChanged(position);
                    });
                });
            } else {
                executor.execute(() -> {
                    cartDao.deleteItem(cartModel.getId());
                    handler.post(() -> {
                        if (position >= 0 && position < cartModelList.size()) {  // Prevent out-of-bounds error
                            cartModelList.remove(position);
                            notifyItemRemoved(position);
                            updateCartVisibility();
                        } else {
                            Log.e("CartAdapter", "Invalid index: " + position + ", Size: " + cartModelList.size());
                        }

                    });
                });
            }
        });


    }

    @Override
    public int getItemCount() {

        return cartModelList.size();
    }
    private void updateCartVisibility() {
        if (cartModelList.isEmpty()) {
            cartHide.setVisibility(View.VISIBLE);
        } else {
            cartHide.setVisibility(View.GONE);
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView itemName, itemPrice, itemQuantity;
        AppCompatImageButton btnDecrease, btnIncrease;

        public ViewHolder(@NonNull View view) {
            super(view);

            itemImage = view.findViewById(R.id.itemImage);
            itemName = view.findViewById(R.id.itemName);
            itemPrice = view.findViewById(R.id.itemPrice);
            itemQuantity = view.findViewById(R.id.itemQuantity);
            btnIncrease = view.findViewById(R.id.btnIncrease);
            btnDecrease = view.findViewById(R.id.btnDecrease);
        }
    }
}
