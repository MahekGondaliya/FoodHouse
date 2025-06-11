package com.example.food_app;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.food_app.model.CategoryModel;

import java.util.List;

@Dao
public interface CartDao {

    @Insert
    void insertItem(CategoryModel model);


    @Query("DELETE FROM cart WHERE id = :itemId")
    void deleteItem(int itemId);

    @Query("SELECT * FROM cart")
    List<CategoryModel> getAllRecords();


    @Query("SELECT SUM(price * count) FROM cart")
    double getTotalPrice();


    @Query("SELECT EXISTS(SELECT 1 FROM cart WHERE name = :name)")
    boolean isItemExists(String name);

    @Query("UPDATE cart SET count = count + 1 WHERE id = :id")
    void increaseCount(int id);

    @Query("UPDATE cart SET count = count - 1 WHERE id = :id AND count > 1")
    void decreaseCount(int id);



    @Query("DELETE FROM cart")
    void deleteAllCategories();
}
