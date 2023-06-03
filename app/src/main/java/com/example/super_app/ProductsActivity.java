package com.example.super_app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductsActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener {

    private ArrayList<ProductModel> productList;
    private ProductAdapter productAdapter;
    private Button backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));
        productList = new ArrayList<>();
        productList.add(new ProductModel(1, "apples", R.drawable.apple, 10.99));
        productList.add(new ProductModel(2, "limes", R.drawable.lime, 15.99));
        productList.add(new ProductModel(3, "strawberry", R.drawable.strawberry, 20.99));
        productList.add(new ProductModel(4, "oranges", R.drawable.orange, 12.99));
        productList.add(new ProductModel(5, "bananas", R.drawable.banana, 18.99));

        // Set up RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        productAdapter = new ProductAdapter(this, productList);
        productAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(productAdapter);
    }

    @Override
    public void onItemClick(ProductModel product) {
        showProductDialog(product);
    }

    private void showProductDialog(ProductModel product) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_product);
        dialog.setCancelable(true);

        ImageView productImage = dialog.findViewById(R.id.popupProductImage);
        TextView productName = dialog.findViewById(R.id.popupProductName);
        TextView productPrice = dialog.findViewById(R.id.popupProductPrice);
        EditText quantityEditText = dialog.findViewById(R.id.quantityEditText);
        Button addToCartButton = dialog.findViewById(R.id.addToCartButton);

        productImage.setImageResource(product.getProductImage());
        productName.setText(product.getProductName());
        productPrice.setText(String.format("$%.2f", product.getProductPrice()));

        addToCartButton.setOnClickListener(v -> {
            String quantityString = quantityEditText.getText().toString().trim();
            //todo add the product to the cart logic
            if (!quantityString.isEmpty()) {
                int quantity = Integer.parseInt(quantityString);
                //todo add the product to the cart logic

            } else {
                // case when the quantity is empty
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "Data collector");
        startActivity(i);

    }
}
