package com.example.super_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.super_app.db.FireBaseHelper;

public class AdminActivity extends Activity {
    FireBaseHelper fireBaseHelper = new FireBaseHelper(this);
    private static final int REQUEST_IMAGE_SELECT = 1;
    private EditText productNameEditText;
    private EditText productPriceEditText;
    private EditText productCategoryEditText;
    private EditText productDescriptionEditText;
    private ImageView productImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));
        Button createProductBtn = findViewById(R.id.createProductBtn);
        createProductBtn.setOnClickListener( v -> showAddProductDialog());
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);
        builder.setTitle("Add New Product");

        productNameEditText = dialogView.findViewById(R.id.productNameEditText);
        productPriceEditText = dialogView.findViewById(R.id.productPriceEditText);
        productCategoryEditText = dialogView.findViewById(R.id.productCategoryEditText);
        productDescriptionEditText = dialogView.findViewById(R.id.productDescriptionEditText);
        productImageView = dialogView.findViewById(R.id.productImageView);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = productNameEditText.getText().toString().trim();
            String priceStr = productPriceEditText.getText().toString().trim();
            String category = productCategoryEditText.getText().toString().trim();
            String description = productDescriptionEditText.getText().toString().trim();

            if (!name.isEmpty() && !priceStr.isEmpty() && !category.isEmpty() && !description.isEmpty()) {
                double price = Double.parseDouble(priceStr);
                int discount = 1; // by default no discount
                fireBaseHelper.addProductToFirestore(name, category, price, discount, description, "defaultImageResourceId");
            } else {
                // Handle case when any of the fields are empty
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        productImageView.setImageResource(R.drawable.default_image);
        productImageView.setOnClickListener(view -> showImageSelection());
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void showImageSelection() {
        Intent intent = new Intent(this, ImageSelectionActivity.class);
        startActivityForResult(intent, REQUEST_IMAGE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            productImageView.setImageResource(data.getIntExtra("imageResourceId", R.drawable.default_image));
            String selectedImageUrl = data.getStringExtra("imageUrl");
            if (selectedImageUrl != null) {
                String name = productNameEditText.getText().toString().trim();
                String priceStr = productPriceEditText.getText().toString().trim();
                String category = productCategoryEditText.getText().toString().trim();
                String description = productDescriptionEditText.getText().toString().trim();
                double price = Double.parseDouble(priceStr);
                int discount = 1; // by default no discount
                fireBaseHelper.addProductToFirestore(name, category, price, discount, description, selectedImageUrl);
            }
        }
    }


    private void moveToActivity(Class<?> cls) {
        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "msg");
        startActivity(i);
    }
}