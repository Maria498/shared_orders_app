package com.example.super_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.cardview.widget.CardView;

public class SuperCategoryActivity extends AppCompatActivity {
    private CardView cardBrush, cardEle, cardMeat, cardFruVegg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_category);

        // Initialize CardViews
        cardBrush = findViewById(R.id.cardBrush);
        cardEle = findViewById(R.id.cardEle);
        cardMeat = findViewById(R.id.cardMeat);
        cardFruVegg = findViewById(R.id.cardFruVegg);

        // Set click listeners for CardViews
        cardBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform desired action
            }
        });

        cardEle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform desired action
            }
        });

        cardMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform desired action
            }
        });

        cardFruVegg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform desired action
            }
        });
    }
}
