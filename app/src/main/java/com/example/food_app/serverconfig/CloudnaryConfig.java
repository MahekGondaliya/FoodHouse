package com.example.food_app.serverconfig;

import android.app.Application;
import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;
import com.example.food_app.BuildConfig;

public class CloudnaryConfig extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Cloudinary
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", BuildConfig.CLOUDINARY_CLOUD_NAME);
        config.put("api_key", BuildConfig.CLOUDINARY_API_KEY);
        config.put("api_secret", BuildConfig.CLOUDINARY_API_SECRET);

        MediaManager.init(this, config);
    }
}
