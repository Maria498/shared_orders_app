package com.example.super_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

    private HomeFragment home = new HomeFragment();
    private OrderFragment newOrder = new OrderFragment();
    private UserFragment profile = new UserFragment();
    private FireBaseHelper fireBaseHelper;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
              fireBaseHelper = new FireBaseHelper(this);
        if(Objects.equals(FireBaseHelper.getCurrentUser(), "admin")){
            startActivity(new Intent(MainActivity.this, AdminActivity.class));
        }
        else{
            bottomNavigationView.setSelectedItemId(R.id.home);
        }
        dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //load all products from firebase to sqlite
        fetchAllProductsFromFireBase();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_order:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, newOrder).commit();
                return true;
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, home).commit();
                return true;
            case R.id.profile:
                // Navigate to user profile
                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profile).commit();
                return true;
            case R.id.cart:
                // Navigate to cartActivity
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                finish();
                return true;
            case R.id.admin:
                startActivity(new Intent(MainActivity.this, AdminActivity.class));
                finish();
                return true;

        }

        return false;
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
//        logInBtn.setText("Hey, "+userName);

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
//        logInBtn.setText(defaultText);
    }

    public void fetchAllProductsFromFireBase() {
        // Create an instance of the listener to handle the fetched products or errors
        List<Product> productListFromFB = new ArrayList<>();
        FireBaseHelper.AllProductsFetchListener listener = new FireBaseHelper.AllProductsFetchListener() {
            @Override
            public void onProductFetch(List<Product> productList) {
                // Process the fetched products here
                // For example, you can update the UI with the products or do any other processing
                // productList contains the list of fetched products
                productListFromFB.clear();
                productListFromFB.addAll(productList);
                System.out.println("Fetched products: " + productList);
                for(Product p: productListFromFB){
                    dbHelper.insertProductToProductDB(p);
                }
                dbHelper.printAllProducts();
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle the failure here
                // For example, show an error message to the user
                System.err.println("Error fetching products: " + errorMessage);
            }
        };

        // Call the fetchAllProductsFromFireBase method with the listener
        fireBaseHelper.fetchAllProductsFromFireBase(listener);
    }





}