package com.example.food_app.screens.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.food_app.MainActivity;
import com.example.food_app.R;
import com.example.food_app.model.CategoryModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class AddCategoryActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etName, etPrice;
    private Button btnSelectImage, btnSave;
    private ImageView imgPreview;
    private TextView tvCategory;
    private Uri imageUri;
    private String imageUrl, selectedItem;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        tvCategory = findViewById(R.id.tvCategory);
        btnSelectImage = findViewById(R.id.etImageUrl);
        btnSave = findViewById(R.id.btnAdd);
        imgPreview = findViewById(R.id.img_add_item);

        db = FirebaseFirestore.getInstance();

        btnSelectImage.setOnClickListener(v -> openFileChooser());

        tvCategory.setOnClickListener(view -> showCategoryMenu());

        btnSave.setOnClickListener(v -> {
            if (!validateInputs()) return;

            if (imageUri != null) {
                uploadImageToCloudinaryAndSaveData();
            } else {
                Toast.makeText(this, "Please select an image!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imgPreview.setImageURI(imageUri); // Only preview, no upload yet
        } else {
            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageToCloudinaryAndSaveData() {
        if (selectedItem == null || selectedItem.isEmpty()) {
            Toast.makeText(this, "Please select a category first!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Uploading image...", Toast.LENGTH_SHORT).show();
        String folderPath = "category/" + selectedItem;

        MediaManager.get().upload(imageUri)
                .option("folder", folderPath)
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {}

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map result) {
                        imageUrl = (String) result.get("secure_url");
                        Picasso.get().load(imageUrl).into(imgPreview);
                        Toast.makeText(AddCategoryActivity.this, "Image uploaded!", Toast.LENGTH_SHORT).show();
                        addDataToFirestore(); // Add data after image is uploaded
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(AddCategoryActivity.this, "Upload failed: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {}
                }).dispatch();
    }

    private void showCategoryMenu() {
        PopupMenu popupMenu = new PopupMenu(this, tvCategory);
        popupMenu.getMenuInflater().inflate(R.menu.category_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            selectedItem = item.getTitle().toString();
            tvCategory.setText(selectedItem);
            return true;
        });
        popupMenu.show();
    }

    private boolean validateInputs() {
        String name = etName.getText().toString().trim();
        String priceText = etPrice.getText().toString().trim();

        if (name.isEmpty() || priceText.isEmpty() || selectedItem == null) {
            Toast.makeText(this, "Please fill all fields and select a category!", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Enter a valid numeric price!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void addDataToFirestore() {
        double price = Double.parseDouble(etPrice.getText().toString().trim());
        CategoryModel model = new CategoryModel(imageUrl, etName.getText().toString().trim(), price, selectedItem);

        db.collection("categories-items").add(model)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Item added successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddCategoryActivity.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
