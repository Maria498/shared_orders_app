package com.example.super_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.card.MaterialCardView;

public class SuperCategoryActivity extends AppCompatActivity {
    private MaterialCardView cardBrush, cardEle, cardMeat, cardFruVegg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_category);

        // Initialize CardViews
        cardBrush = findViewById(R.id.cardBrush);
        cardEle = findViewById(R.id.cardEle);
        cardMeat = findViewById(R.id.cardMeat);
        cardFruVegg = findViewById(R.id.cardFruVegg);
        FragmentManager fm = getSupportFragmentManager();
        Bundle args = new Bundle();

        // Set click listeners for CardViews
        cardBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("Type","MakeUpAndBrush");
                fragment_products fragment_products=new fragment_products();
                fragment_products.setArguments(args);
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.fragmentContainer, fragment_products);
                t.addToBackStack(null);
                t.commit();
            }
        });

        cardEle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("Type","Electronics");
                fragment_products fragment_products=new fragment_products();
                fragment_products.setArguments(args);
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.fragmentContainer, fragment_products);
                t.addToBackStack(null);
                t.commit();            }
        });

        cardMeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("Type","Meat");
                fragment_products fragment_products=new fragment_products();
                fragment_products.setArguments(args);
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.fragmentContainer, fragment_products);
                t.addToBackStack(null);
                t.commit();
            }
        });

        cardFruVegg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("Type","FruitsAndVeg");
                fragment_products fragment_products=new fragment_products();
                fragment_products.setArguments(args);
                FragmentTransaction t = fm.beginTransaction();
                t.replace(R.id.fragmentContainer, fragment_products);
                t.addToBackStack(null);
                t.commit();
            }
        });
    }
}
