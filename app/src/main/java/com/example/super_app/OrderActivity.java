package com.example.super_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        TextView orderAddressTextView = findViewById(R.id.orderAddressTextView);
        TextView orderDeliveryDateTextView = findViewById(R.id.orderDeliveryDateTextView);
        Button joinButton = findViewById(R.id.joinButton);

        // Retrieve order details from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("address") && intent.hasExtra("delivery")) {
            String address = intent.getStringExtra("address");
            String deliveryDate = intent.getStringExtra("delivery");

            // Display the order details in the TextViews
            orderAddressTextView.setText(address);
            orderDeliveryDateTextView.setText(deliveryDate);
        }

        // Handle the "Join" button click
        joinButton.setOnClickListener(v -> {
            // Perform the action when the "Join" button is clicked
            // Add your implementation here
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
