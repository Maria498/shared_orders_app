package com.example.super_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.super_app.db.FireBaseHelper;

public class AdminActivity extends Activity {
    FireBaseHelper fireBaseHelper = new FireBaseHelper(this);
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

        EditText productNameEditText = dialogView.findViewById(R.id.productNameEditText);
        EditText productPriceEditText = dialogView.findViewById(R.id.productPriceEditText);
        EditText productCategoryEditText = dialogView.findViewById(R.id.productCategoryEditText);
        EditText productDescriptionEditText = dialogView.findViewById(R.id.productDescriptionEditText);
        EditText productImageUrlEditText = dialogView.findViewById(R.id.productImageUrlEditText);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = productNameEditText.getText().toString().trim();
            String priceStr = productPriceEditText.getText().toString().trim();
            String category = productCategoryEditText.getText().toString().trim();
            String description = productDescriptionEditText.getText().toString().trim();
            String imageUrl = productImageUrlEditText.getText().toString().trim();

            if (!name.isEmpty() && !priceStr.isEmpty() && !category.isEmpty() && !description.isEmpty() && !imageUrl.isEmpty()) {
                double price = Double.parseDouble(priceStr);
                int discount = 1; // by default no discount

                fireBaseHelper.addProductToFirestore(name, category, price, discount, description, imageUrl);
            } else {
                // Handle case when any of the fields are empty
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }




    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "msg");
        startActivity(i);

    }
}