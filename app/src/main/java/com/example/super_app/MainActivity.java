package com.example.super_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    Button logInBtn;

    RecyclerView recyclerViewAddresses;
    RecyclerView recyclerViewCategories;
    MenuCardsAdapter adapter;
    ArrayList<MenuModel> cardsList;
    ArrayList<MenuModel> cardsListCath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewAddresses = findViewById(R.id.recyclerViewAddresses);
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);

        cardsList = new ArrayList<>();
        cardsList.add(new MenuModel(R.string.home_address, R.string.namal, R.drawable.map_small));
        cardsList.add(new MenuModel(R.string.recently_address, R.string.herzel, R.drawable.map_small));
        adapter = new MenuCardsAdapter(this, cardsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAddresses.setLayoutManager(layoutManager);
        recyclerViewAddresses.setAdapter(adapter);

        cardsListCath = new ArrayList<>();
        cardsListCath.add(new MenuModel(R.string.meat, R.string.no, R.drawable.meat));
        cardsListCath.add(new MenuModel(R.string.veggie, R.string.no, R.drawable.veggi));
        adapter = new MenuCardsAdapter(this, cardsListCath);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(layoutManager1);
        recyclerViewCategories.setAdapter(adapter);

        logInBtn = findViewById(R.id.logInBtn);
        logInBtn.setOnClickListener(v -> moveToActivity(LoginActivity.class));;

    }

    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "Data collector");
        startActivity(i);

    }

    private void updateRecycled (Class<?> cls) {



    }
}