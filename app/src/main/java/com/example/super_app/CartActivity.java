package com.example.super_app;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.Cart;
import com.example.super_app.db.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemClickListener {
    private Cart cart;
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

        // Retrieve the cart instance from the FireBaseHelper
        cart = FireBaseHelper.getCart();
        if (cart != null) {
            // Add products from the cart HashMap to the cartProductsList
            cartProductsList.addAll(cart.getProductsQuantity().keySet());

            // Calculate and display the total price
            double totalPrice = calculateTotalPrice(cart.getProductsQuantity());
            cartTotalPrice.setText(String.format("$%.2f", totalPrice));

            // Update the RecyclerView with the cartProductsList
            cartAdapter.updateCartProductsList(cartProductsList);
        }
    }

    private double calculateTotalPrice(Map<Product, Integer> cartHashMap) {
        double totalPrice = 0;
        for (Map.Entry<Product, Integer> entry : cartHashMap.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            totalPrice += (product.getPrice() * quantity);
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
