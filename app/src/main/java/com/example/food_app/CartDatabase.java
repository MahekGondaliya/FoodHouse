package com.example.food_app;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.food_app.model.CategoryModel;

@Database(entities = {CategoryModel.class}, version = 1, exportSchema = false)
public abstract class CartDatabase extends RoomDatabase {
    private static volatile CartDatabase INSTANCE;

    public abstract CartDao cartDao();

    public static CartDatabase getInstance(Context context) {

//        context.deleteDatabase("cart_database");
        if (INSTANCE == null) {
            synchronized (CartDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CartDatabase.class, "cart_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
