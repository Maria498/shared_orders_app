package com.example.super_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.Cart;
import com.example.super_app.db.entity.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemClickListener {
    private List<Product> cartProductsList = new ArrayList<>();
    private Button backBtn;
    private Button checkout;
    private HashMap<String, ArrayList<Product>> productsInOrder = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase dbSQLite = dbHelper.getWritableDatabase();

        backBtn = findViewById(R.id.backBtn);
        checkout = findViewById(R.id.Checkout);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));
        Context context = getApplicationContext();

        RecyclerView recyclerViewCart = findViewById(R.id.recyclerViewCart);
        TextView cartTotalPrice = findViewById(R.id.cartTotalPrice);

        // Set up RecyclerView
        CartAdapter cartAdapter = new CartAdapter(this, cartProductsList);
        cartAdapter.setOnItemClickListener(this);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCart.setAdapter(cartAdapter);

        // Retrieve the cart instance from the FireBaseHelper
        Cart cart = FireBaseHelper.getCart();
        if (cart != null) {
            // Add products from the cart HashMap to the cartProductsList
            cartProductsList.addAll(cart.getProductsQuantity().keySet());
            dbHelper.addCart(cart);
            dbHelper.printAllCarts();

            // Calculate and display the total price
            double totalPrice = calculateTotalPrice(cart.getProductsQuantity());
            cartTotalPrice.setText(String.format("$%.2f", totalPrice));

            // Update the RecyclerView with the cartProductsList
            cartAdapter.updateCartProductsList(cartProductsList);
        }
        checkout.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
            builder.setTitle("Confirm Checkout");
            builder.setMessage("Are you sure you want to proceed with the checkout?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // Check if the cart object is not null before proceeding
                if (cart != null) {
                    Intent i = new Intent(getApplicationContext(), CreateNewOrderActivity.class);
                    i.putExtra("cart_id", cart.getCartId());
                    startActivity(i);
                } else {
                    // Handle the case when cart is null (optional)
                    Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

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
