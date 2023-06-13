package com.example.super_app;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.card.MaterialCardView;

public class SuperCategoryActivity extends AppCompatActivity {
    private CardView cardBrush, cardEle, cardMeat, cardFruVegg;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_category);

        // Initialize CardViews
        cardBrush = findViewById(R.id.cardBrush);
        cardEle = findViewById(R.id.cardEle);
        cardMeat = findViewById(R.id.cardMeat);
        cardFruVegg = findViewById(R.id.cardFruVegg);
        layout=findViewById(R.id.linearLay);

    }

    public void brushClick(View view) {
        layout.setVisibility(View.GONE);
        Bundle args = new Bundle();
        args.putString("Type","MakeUpAndBrush");
        fragment_products fragment=new fragment_products();
        fragment.setArguments(args);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.fragmentContainer, fragment);
        t.addToBackStack(null);
        t.commit();
    }

    public void eleClick(View view) {
        layout.setVisibility(View.GONE);
        FragmentManager fm = getFragmentManager();
        Bundle args = new Bundle();
        args.putString("Type","Electronics");
        fragment_products fragment=new fragment_products();
        fragment.setArguments(args);
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.fragmentContainer, fragment);
        t.addToBackStack(null);
        t.commit();

    }

    public void meatClick(View view) {
        layout.setVisibility(View.GONE);
        FragmentManager fm = getFragmentManager();
        Bundle args = new Bundle();
        args.putString("Type","Meat");
        fragment_products fragment=new fragment_products();
        fragment.setArguments(args);
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.fragmentContainer, fragment);
        t.addToBackStack(null);
        t.commit();

    }

    public void vegClick(View view) {
        layout.setVisibility(View.GONE);
        FragmentManager fm = getFragmentManager();
        Bundle args = new Bundle();
        args.putString("Type","FruitsAndVeg");
        fragment_products fragment=new fragment_products();
        fragment.setArguments(args);
        FragmentTransaction t = fm.beginTransaction();
        t.replace(R.id.fragmentContainer, fragment);
        t.addToBackStack(null);
        t.commit();

    }
}
