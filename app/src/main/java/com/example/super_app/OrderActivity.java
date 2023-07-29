package com.example.super_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.Cart;
import com.example.super_app.db.entity.Order;
import com.google.firebase.auth.FirebaseAuth;

public class OrderActivity extends AppCompatActivity {

    FireBaseHelper fireBaseHelper;
    Order currentOrder;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        fireBaseHelper = new FireBaseHelper();
        mAuth = FirebaseAuth.getInstance();
        TextView orderAddressTextView = findViewById(R.id.orderAddressTextView);
        TextView orderDeliveryDateTextView = findViewById(R.id.orderDeliveryDateTextView);
        Button joinButton = findViewById(R.id.joinButton);

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
                    currentOrder = order;
                    Log.w("+++++++ORDER",order.getId()+", "+ currentOrder.getCartsOfNeigh());

                }


                @Override
                public void onFailure(String errorMessage) {
                    // Handle the case where the order is not found
                    Toast.makeText(OrderActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });



            // Display the order details in the TextViews
            orderAddressTextView.setText(address);
            orderDeliveryDateTextView.setText(deliveryDate);
        }

        // Handle the "Join" button click
        joinButton.setOnClickListener(v -> {
            Cart cart = FireBaseHelper.getCart();
            if (cart != null && !cart.getProductsIDQuantity().isEmpty()) {
                // Check if the order does not already contain the cart
                if (!currentOrder.getCartsOfNeigh().containsKey(mAuth.getUid())) {
                    // Add cart to the order
                    fireBaseHelper.addCartToFirestore(OrderActivity.this, currentOrder, currentOrder.getId(), cart);
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
}
