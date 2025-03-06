package com.example.food_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@IgnoreExtraProperties
public class OrderModel implements Parcelable {

    private String id;
    private String currentDateTime;
    private List<List<CategoryModel>> nestedList;

    public OrderModel() {
        this.nestedList = new ArrayList<>();
    }

    public OrderModel(String id) {
        this.id = id;
        this.currentDateTime = generateCurrentDateTime();
        this.nestedList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public List<List<CategoryModel>> getNestedList() {
        return nestedList;
    }

    public void setNestedList(List<List<CategoryModel>> nestedList) {
        this.nestedList = nestedList;
    }

    public void addNestedList(List<CategoryModel> list) {
        if (this.nestedList == null) {
            this.nestedList = new ArrayList<>();
        }
        this.nestedList.add(list);
    }

    private String generateCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Parcelable Implementation
    protected OrderModel(Parcel in) {
        id = in.readString();
        currentDateTime = in.readString();

        int outerSize = in.readInt();
        nestedList = new ArrayList<>();
        for (int i = 0; i < outerSize; i++) {
            List<CategoryModel> innerList = new ArrayList<>();
            in.readTypedList(innerList, CategoryModel.CREATOR);
            nestedList.add(innerList);
        }
    }

    public static final Creator<OrderModel> CREATOR = new Creator<OrderModel>() {
        @Override
        public OrderModel createFromParcel(Parcel in) {
            return new OrderModel(in);
        }

        @Override
        public OrderModel[] newArray(int size) {
            return new OrderModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(currentDateTime);

        dest.writeInt(nestedList.size());  // Write size of outer list
        for (List<CategoryModel> innerList : nestedList) {
            dest.writeTypedList(innerList);  // Write each inner list
        }
    }
}
