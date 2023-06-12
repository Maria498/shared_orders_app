package com.example.super_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
        FragmentManager fm = getFragmentManager();
        Bundle args = new Bundle();

        // Set click listeners for CardViews
        cardBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("Type","MakeUpAndBrush");
                fragment_products fragment_products=new fragment_products();
                fragment_products.setArguments(args);
                FragmentTransaction t = fm.beginTransaction();
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
                t.addToBackStack(null);
                t.commit();            }
        });

        cardFruVegg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                args.putString("Type","FruitsAndVeg");
                fragment_products fragment_products=new fragment_products();
                fragment_products.setArguments(args);
                FragmentTransaction t = fm.beginTransaction();
                t.addToBackStack(null);
                t.commit();            }
        });
    }
}
