package com.example.super_app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class SuperCategoryActivity extends AppCompatActivity{
    private CardView cardBrush, cardEle, cardMeat, cardFruVegg;
    LinearLayout layout;
    private ImageView back;


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
        back = findViewById(R.id.backButton);
        back.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuperCategoryActivity.this, SuperCategoryActivity.class);
                startActivity(intent);

            }
        });

    }

    public void brushClick(View view) {
        layout.setVisibility(View.GONE);
        back.setVisibility(View.VISIBLE);
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
        back.setVisibility(View.VISIBLE);
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
        back.setVisibility(View.VISIBLE);
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
        back.setVisibility(View.VISIBLE);
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
