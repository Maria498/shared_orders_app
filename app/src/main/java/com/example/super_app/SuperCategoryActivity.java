package com.example.super_app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class SuperCategoryActivity extends AppCompatActivity{
    LinearLayout layout;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_category);
        BottomNavigationView menu = findViewById(R.id.menu);

        // Initialize CardViews
        CardView cardBrush = findViewById(R.id.cardBrush);
        CardView cardEle = findViewById(R.id.cardEle);
        CardView cardMeat = findViewById(R.id.cardMeat);
        CardView cardFruVegg = findViewById(R.id.cardFruVegg);
        layout=findViewById(R.id.linearLay);
        back = findViewById(R.id.backButton);
        back.setVisibility(View.GONE);
        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.cart:
                        startActivity(new Intent(SuperCategoryActivity.this, MainActivity.class));
                        finish(); // Optional: Close the current activity
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(SuperCategoryActivity.this, MainActivity.class));
                        finish(); // Optional: Close the current activity
                        return true;
                    case R.id.search:
                        startActivity(new Intent(SuperCategoryActivity.this, MainActivity.class));
                        finish(); // Optional: Close the current activity
                        return true;
                    case R.id.home:
                        startActivity(new Intent(SuperCategoryActivity.this, MainActivity.class));
                        finish(); // Optional: Close the current activity
                        return true;
                }
                return false;
            }
        });
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
