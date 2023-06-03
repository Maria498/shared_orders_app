package com.example.super_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Button logInBtn;

    private RecyclerView recyclerViewAddresses;
    private RecyclerView recyclerViewCategories;
    private MenuCardsAdapter adapter;
    private ArrayList<MenuModel> cardsList;
    private ArrayList<MenuModel> cardsListCath;



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
        cardsListCath.add(new MenuModel(R.string.fruits, R.string.no, R.drawable.fruit));
        cardsListCath.add(new MenuModel(R.string.veggie, R.string.no, R.drawable.veggi));
        cardsListCath.add(new MenuModel(R.string.meat, R.string.no, R.drawable.meat));
        adapter = new MenuCardsAdapter(this, cardsListCath);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(layoutManager1);
        recyclerViewCategories.setAdapter(adapter);

        logInBtn = findViewById(R.id.logInBtn);
        logInBtn.setOnClickListener(v -> moveToActivity(LoginActivity.class));
        DisplaySavedText();


    }

    private void moveToActivity (Class<?> cls) {
        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "msg");
        startActivity(i);
    }

    private void updateRecycled (Class<?> cls) {}

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String userName = intent.getStringExtra("USER_NAME");
        if(userName != null){
            Log.d("i am onResume()", "i am onResume()");
            DisplayAndSaveUserName(userName);

        }
    }
    @SuppressLint("LongLogTag")
    private void DisplayAndSaveUserName(String userName) {
        //Display the text
        logInBtn.setText("Hey, "+userName);

        //-------store data--------
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //write data to shared perf
        SharedPreferences.Editor editor =sharedPref.edit();
        editor.putString("userName","Hey, "+ userName);
        editor.commit();
        Log.d("i am DisplayAndSaveUserName(String userName)", "i am DisplayAndSaveUserName(String userName)");
        //-------store data--------
        //String storedUserName = sharedPref.getString(getString(R.string.log_in), null);
    }

    private void DisplaySavedText() {
        //Reading values from shared preference
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String defaultText = sharedPref.getString("userName", "Log In");
        //logInBtn.setEnabled(true);
        logInBtn.setText(defaultText);
        logInBtn.setOnClickListener(v -> moveToActivity(UserProfileActivity.class));
    }


    }