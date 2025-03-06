package com.example.food_app.sampledata;

import com.example.food_app.model.ItemModel;
import com.example.food_app.model.RecycleViewRecommendModel;

import java.util.ArrayList;
import java.util.List;

public class SampleDataForRecommend {

    public static List<RecycleViewRecommendModel> getFoodItemsRecommend() {
        List<RecycleViewRecommendModel> foodList = new ArrayList<>();


        foodList.add(new RecycleViewRecommendModel(
                "https://res.cloudinary.com/foodappimg/image/upload/v1739465801/item1_vyuucv.jpg",
                "Cheeseburger",
                "$6.99",
                "4.6",
                "A classic cheeseburger with fresh lettuce, tomato, and melted cheddar."
        ));

        // Example data 2
        foodList.add(new RecycleViewRecommendModel(
                "https://res.cloudinary.com/foodappimg/image/upload/v1739465799/item2_ygv5ln.jpg", // Replace with actual image resource ID
                "Margherita Pizza",
                "$9.99",
                "4.8",
                "A simple yet delicious pizza with fresh mozzarella, tomatoes, and basil."
        ));

        // Example data 3
        foodList.add(new RecycleViewRecommendModel(
                "https://res.cloudinary.com/foodappimg/image/upload/v1739465868/item3_zcsx28.jpg", // Replace with actual image resource ID
                "Sushi Rolls",
                "$14.99",
                "4.9",
                "Freshly prepared sushi rolls with salmon, tuna, and avocado."
        ));

        // Example data 4
        foodList.add(new RecycleViewRecommendModel(
                "https://res.cloudinary.com/foodappimg/image/upload/v1739465908/item4_idjakn.jpg", // Replace with actual image resource ID
                "Spaghetti Carbonara",
                "$11.50",
                "4.7",
                "A creamy pasta dish with eggs, cheese, pancetta, and pepper."
        ));

        // Example data 5
        foodList.add(new RecycleViewRecommendModel(
                "https://res.cloudinary.com/foodappimg/image/upload/v1739465800/item5_uyfj2f.jpg", // Replace with actual image resource ID
                "Caesar Salad",
                "$5.99",
                "4.4",
                "A classic Caesar salad with crisp lettuce, croutons, and Caesar dressing."
        ));

        // Example data 6
        foodList.add(new RecycleViewRecommendModel(
                "https://res.cloudinary.com/foodappimg/image/upload/v1739465817/item6_mzctal.jpg", // Replace with actual image resource ID
                "BBQ Ribs",
                "$18.99",
                "4.8",
                "Tender, slow-cooked BBQ ribs served with a smoky barbecue sauce."
        ));

        // Example data 7
        foodList.add(new RecycleViewRecommendModel(
                "https://res.cloudinary.com/foodappimg/image/upload/v1739465817/item7_muv9sf.jpg", // Replace with actual image resource ID
                "Grilled Chicken",
                "$12.50",
                "4.7",
                "Juicy grilled chicken breast served with a side of roasted vegetables."
        ));

        // Example data 8
        foodList.add(new RecycleViewRecommendModel(
                "https://res.cloudinary.com/foodappimg/image/upload/v1739465869/item8_oepieb.jpg", // Replace with actual image resource ID
                "Vegetable Stir Fry",
                "$7.99",
                "4.5",
                "A healthy mix of vegetables stir-fried in a savory soy sauce."
        ));

        // Example data 9
        foodList.add(new RecycleViewRecommendModel(
                "https://res.cloudinary.com/foodappimg/image/upload/v1739465886/item9_dch3vo.jpg", // Replace with actual image resource ID
                "Chicken Tacos",
                "$8.99",
                "4.6",
                "Soft tortillas filled with seasoned chicken, lettuce, cheese, and salsa."
        ));

        // Example data 10
        foodList.add(new RecycleViewRecommendModel(
                "https://res.cloudinary.com/foodappimg/image/upload/v1739465804/item10_enn3nz.jpg", // Replace with actual image resource ID
                "Chocolate Cake",
                "$4.99",
                "4.9",
                "A rich and moist chocolate cake topped with creamy chocolate frosting."
        ));

        return foodList;
    }

    public static List<ItemModel> getFoodItemsBig() {
        List<ItemModel> modelList = new ArrayList<>();

        modelList.add(new ItemModel("https://res.cloudinary.com/foodappimg/image/upload/v1739467288/item1_nb3ezv.jpg", "ItemModel 1"));
        modelList.add(new ItemModel("https://res.cloudinary.com/foodappimg/image/upload/v1739467326/item2_qdfemo.jpg", "ItemModel 2"));
        modelList.add(new ItemModel("https://res.cloudinary.com/foodappimg/image/upload/v1739467386/item3_cce58s.jpg", "ItemModel 3"));

        return modelList;
    }

    public static List<ItemModel> getFoodItem() {
        List<ItemModel> itemModelList = new ArrayList<>();
        itemModelList.add(new ItemModel("https://res.cloudinary.com/foodappimg/image/upload/v1739467685/fruits_vlathf.png", "Fruits"));
        itemModelList.add(new ItemModel("https://res.cloudinary.com/foodappimg/image/upload/v1739467919/vegetables_q8u63x.png", "Vegetables"));
        itemModelList.add(new ItemModel("https://res.cloudinary.com/foodappimg/image/upload/v1739468782/dairy_w5cwsg.png", "Dairy"));
        itemModelList.add(new ItemModel("https://res.cloudinary.com/foodappimg/image/upload/v1739469286/Grains_ggytqx.png", "Grains"));
        itemModelList.add(new ItemModel("https://res.cloudinary.com/foodappimg/image/upload/v1739469712/Nuts_Seeds_feeffe.png", "Nuts & Seeds"));

        return itemModelList;
    }
}


