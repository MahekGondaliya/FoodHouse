package com.example.food_app.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
@Entity(tableName = "cart")
public class CategoryModel implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "totalPrice")
    private Double totalPrice;

    @ColumnInfo(name = "imageResource")
    private String imageResource;

    @ColumnInfo(name = "count")
    private int count;

    @ColumnInfo(name = "category")
    private String category;

    public CategoryModel() {
    }

    public CategoryModel(String imageResource, String name, double price, String category) {
        this.imageResource = imageResource;
        this.name = name;
        this.price = price;
        this.count = 1;
        this.totalPrice = price * count;
        this.category = category;
    }

    protected CategoryModel(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readDouble();
        totalPrice = in.readDouble();
        imageResource = in.readString();
        count = in.readInt();
        category = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static final Creator<CategoryModel> CREATOR = new Creator<CategoryModel>() {
        @Override
        public CategoryModel createFromParcel(Parcel in) {
            return new CategoryModel(in);
        }

        @Override
        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeDouble(totalPrice);
        dest.writeString(imageResource);
        dest.writeInt(count);
        dest.writeString(category);
    }
}
