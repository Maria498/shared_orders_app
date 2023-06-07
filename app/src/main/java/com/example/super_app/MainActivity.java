package com.example.super_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.DatabaseHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Button logInBtn;

    private RecyclerView recyclerViewAddresses;
    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewOrders;
    private MenuCardsAdapter adapter;
    private ArrayList<MenuModel> cardsList;
    private ArrayList<MenuModel> cardsListCath;
    private ArrayList<MenuModel> cardsListOrders;

    private ImageButton shoppingCart;
    private FrameLayout fragmentContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.createShopping(db);

        recyclerViewAddresses = findViewById(R.id.recyclerViewAddresses);
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        shoppingCart = findViewById(R.id.shoppingCartIcon);
        fragmentContainer = findViewById(R.id.fragmentContainer);

        cardsList = new ArrayList<>();
        cardsList.add(new MenuModel("Home address", "Namal str. 6", R.drawable.map_small));
        cardsList.add(new MenuModel("Recently used","Herzel str. 4", R.drawable.map_small));
        adapter = new MenuCardsAdapter(this, cardsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAddresses.setLayoutManager(layoutManager);
        recyclerViewAddresses.setAdapter(adapter);

        cardsListCath = new ArrayList<>();
        cardsListCath.add(new MenuModel("Fruits", "", R.drawable.fruit));
        cardsListCath.add(new MenuModel("Veggies","", R.drawable.veggi));
        cardsListCath.add(new MenuModel("Meat", "", R.drawable.meat));
        adapter = new MenuCardsAdapter(this, cardsListCath);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(layoutManager1);
        recyclerViewCategories.setAdapter(adapter);

        cardsListOrders = new ArrayList<>();
        cardsListOrders.add(new MenuModel("Order1", "Total: 800", R.drawable.bag));
        cardsListOrders.add(new MenuModel("Order2", "Total: 559", R.drawable.bag));
        adapter = new MenuCardsAdapter(this, cardsListOrders);
        RecyclerView.LayoutManager layoutManagerOrder = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewOrders.setLayoutManager(layoutManagerOrder);
        recyclerViewOrders.setAdapter(adapter);

        logInBtn = findViewById(R.id.logInBtn);
        logInBtn.setOnClickListener(v -> moveToActivity(LoginActivity.class));
        DisplaySavedText();

        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ArrayList<ProductModel> shoppingList = new ArrayList<>();
                String selectAllItemsQuery = "SELECT * FROM shoppingCart;";
                Cursor cursor = db.rawQuery(selectAllItemsQuery, null);

// Iterate over the cursor to retrieve all items
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                    String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                    int pic = cursor.getInt(cursor.getColumnIndexOrThrow("pic"));
                    shoppingList.add(new ProductModel(name,pic,price,quantity));
                }

// Close the cursor and database connection when done
                shoppingCartFragment fragment = new shoppingCartFragment();
                fragment.setItemList(shoppingList);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, fragment);
                fragmentTransaction.commit();
            }
        });
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
    }


}