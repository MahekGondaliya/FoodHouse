package com.example.food_app;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.food_app.CartDatabase;
import com.example.food_app.model.CategoryModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CartViewModel extends ViewModel {

    private final MutableLiveData<List<CategoryModel>> cartItems = new MutableLiveData<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public LiveData<List<CategoryModel>> getCartItems(Context context) {
        CartDatabase db = CartDatabase.getInstance(context);
        executorService.execute(() -> {
            List<CategoryModel> items = db.cartDao().getAllRecords();
            cartItems.postValue(items);
        });
        return cartItems;
    }

    public void clearCart(Context context) {
        CartDatabase db = CartDatabase.getInstance(context);
        executorService.execute(() -> db.cartDao().deleteAllCategories());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        executorService.shutdown();
    }
}
