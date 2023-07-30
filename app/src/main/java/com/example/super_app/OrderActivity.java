package com.example.super_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.Cart;
import com.example.super_app.db.entity.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class OrderActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener{

    FireBaseHelper fireBaseHelper;
    Order currentOrder;
    Cart cart = FireBaseHelper.getCart();
    private FirebaseAuth mAuth;
    double saving = 0.03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        fireBaseHelper = new FireBaseHelper();
        mAuth = FirebaseAuth.getInstance();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.new_order);
        TextView orderAddressTextView = findViewById(R.id.orderAddressTextView);
        TextView orderDeliveryDateTextView = findViewById(R.id.orderDeliveryDateTextView);
        TextView orderTotal = findViewById(R.id.orderTotal);
        TextView discount = findViewById(R.id.discount);
        Button joinButton = findViewById(R.id.joinButton);
        discount.setVisibility(View.INVISIBLE);
        // Retrieve order details from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("address") && intent.hasExtra("delivery")) {
            String address = intent.getStringExtra("address");
            String deliveryDate = intent.getStringExtra("delivery");
            fireBaseHelper.fetchOrderByAddressAndDate(address, deliveryDate, new FireBaseHelper.FetchOrderCallback() {
                @Override
                public void onOrderFetched(Order order) {
                    // Display the order details in the TextViews
                    orderAddressTextView.setText(order.getAddress());
                    orderDeliveryDateTextView.setText(order.getDeliveryDate());
                    orderTotal.setText(orderTotal.getText() + " "+ String.format("%.2f", order.getTotalPrice()));
                    currentOrder = order;

                    if (cart != null && !cart.getProductsIDQuantity().isEmpty()) {
//                        Log.w("+++++++ORDER",order.getId()+", "+ currentOrder.getTotalPrice());
                        double overallTotal = cart.getTotal() + order.getTotalPrice();

                        if(overallTotal > 250 && overallTotal < 1000){
                            saving = 0.05;
                        }
                        else if(overallTotal >= 1000){
                            saving = 0.1;
                        }
                        double mySaving = cart.getTotal() - cart.getTotal() * (1 - saving);
                        discount.setVisibility(View.VISIBLE);
                        discount.setText(discount.getText() + String.format("%.2f", mySaving));

                    }
                }
                @Override
                public void onFailure(String errorMessage) {
                    // Handle the case where the order is not found
                    Toast.makeText(OrderActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

         joinButton.setOnClickListener(v -> {
            if (cart != null && !cart.getProductsIDQuantity().isEmpty()) {
                // Check if the order does not already contain the cart
                if (!currentOrder.getCartsOfNeigh().containsKey(mAuth.getUid())) {
                    // Add cart to the order
                    fireBaseHelper.addCartToFirestore(OrderActivity.this, currentOrder, currentOrder.getId(), cart, saving);
                } else {
                    // The cart is already associated with the order, show a message or handle accordingly
                    Toast.makeText(OrderActivity.this, "You have already added a cart to this order", Toast.LENGTH_SHORT).show();
                }
            } else {
                // The cart is empty, show a message or handle accordingly
                Toast.makeText(OrderActivity.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            }
        });

        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));
    }

    private void moveToActivity (Class<?> cls) {
        Intent i = new Intent(getApplicationContext(), cls);
        i.putExtra("msg", "msg");
        startActivity(i);
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_order:
            case R.id.home:
            case R.id.profile:
                startActivity(new Intent(OrderActivity.this, MainActivity.class));
                finish();
                return true;
            case R.id.admin:
                // Navigate to another activity (not a fragment)
                startActivity(new Intent(OrderActivity.this, AdminActivity.class));
                finish();
                return true;
            case R.id.cart:
                // Navigate to another activity (not a fragment)
                startActivity(new Intent(OrderActivity.this, CartActivity.class));
                finish();
                return true;
        }
        return false;
    }
}
