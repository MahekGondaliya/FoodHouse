package com.example.food_app.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.food_app.CartDao;
import com.example.food_app.CartDatabase;
import com.example.food_app.model.CategoryModel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddToCart {

    public static void addToCart(CategoryModel model, Context context) {


        CartDatabase db = CartDatabase.getInstance(context);
        CartDao cartDao = db.cartDao();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper()); // UI updates

        executor.execute(() -> {
            boolean exists = cartDao.isItemExists(model.getName());  // Check if item exists

            handler.post(() -> {  // Update UI safely
                if (exists) {
                    Toast.makeText(context, model.getName() + " is already in the cart", Toast.LENGTH_SHORT).show();
                } else {
                    executor.execute(() -> cartDao.insertItem(model)); // Insert item in background
                    Toast.makeText(context, model.getName() + " added to cart", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }
}
