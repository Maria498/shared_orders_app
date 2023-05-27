package com.example.super_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.super_app.db.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter adapter;
    private List<Product> productList;
    private Button backBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        backBt = findViewById(R.id.backBtn);
        // Initialize the list of products
        productList = new ArrayList<>();
        productList.add(new Product("Meat", "Meat", 89.99, -30, true, R.drawable.meat));
        productList.add(new Product("Meat", "Meat", 69.89, -20, true, R.drawable.meat));
        productList.add(new Product("Meat", "Meat", 55.99, -10, true, R.drawable.meat));
        productList.add(new Product("Meat", "Meat", 89.99, -30, true, R.drawable.meat));
        productList.add(new Product("Meat", "Meat", 69.89, -20, true, R.drawable.meat));
        productList.add(new Product("Meat", "Meat", 55.99, -10, true, R.drawable.meat));
        // Add more products as needed

        // Create and set the adapter
        adapter = new ProductAdapter(this, productList);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerViewProducts.setAdapter(adapter);
        backBt.setOnClickListener(view ->  moveToActivity(MainActivity.class));
    }

    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "msg");
        startActivity(i);

    }
}