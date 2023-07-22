package com.example.super_app;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemClickListener {
    private DatabaseHelper dbHelper;
    private List<Product> cartProductsList = new ArrayList<>();
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));

        RecyclerView recyclerViewCart = findViewById(R.id.recyclerViewCart);
        TextView cartTotalPrice = findViewById(R.id.cartTotalPrice);

        // Set up RecyclerView
        cartAdapter = new CartAdapter(this, cartProductsList);
        cartAdapter.setOnItemClickListener(this);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);

        // Retrieve cart data from the SQLite database
        dbHelper = new DatabaseHelper(this);
        cartProductsList.addAll(dbHelper.getAllProductsInCart());

        // Calculate and display the total price
        double totalPrice = calculateTotalPrice(cartProductsList);
        cartTotalPrice.setText(String.format("$%.2f", totalPrice));
        cartAdapter.updateCartProductsList(cartProductsList);
    }

    private double calculateTotalPrice(List<Product> products) {
        double totalPrice = 0;
        for (Product product : products) {
            totalPrice += (product.getPrice() * product.getQuantity());
        }
        return totalPrice;
    }

    @Override
    public void onItemClick(Product product) {
        // Handle the click event for the cart item, if needed.
        // You can add logic here to edit or remove the product from the cart.
    }

    private void moveToActivity(Class<?> cls) {
        Intent i = new Intent(getApplicationContext(), cls);
        i.putExtra("msg", "msg");
        startActivity(i);
    }
}

