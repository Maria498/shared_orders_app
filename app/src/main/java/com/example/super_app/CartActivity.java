package com.example.super_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.entity.Cart;
import com.example.super_app.db.entity.Product;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartAdapter = new CartAdapter(this);
        recyclerView.setAdapter(cartAdapter);

        // Replace this with your actual cart data from the database
        List<Cart> cartList = createDummyCartList();
        cartAdapter.setCartList(cartList);
    }

    // Replace this method with your actual data retrieval from the database
    private List<Cart> createDummyCartList() {
        List<Cart> cartList = new ArrayList<>();

        // Sample data for demonstration purposes
        Cart cart1 = new Cart("CART-001", new Date(), 100.0, 10);
        HashMap<Product, Double> products1 = new HashMap<>();
//        products1.put(new Product("Product A"), 2.0);
//        products1.put(new Product("Product B"), 1.5);
        cart1.setProductsQuantity(products1);

        Cart cart2 = new Cart("CART-002", new Date(), 75.0, 5);
        HashMap<Product, Double> products2 = new HashMap<>();
//        products2.put(new Product("Product C"), 3.0);
//        products2.put(new Product("Product D"), 2.0);
        cart2.setProductsQuantity(products2);

        cartList.add(cart1);
        cartList.add(cart2);

        return cartList;
    }
}
