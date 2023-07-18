package com.example.super_app;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;


import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.FireBaseHelper;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    private Button logInBtn;
    private Button logOutBtn;

    private RecyclerView recyclerViewAddresses;
    private RecyclerView recyclerViewCategories;
    private RecyclerView recyclerViewOrders;
    private MenuCardsAdapter adapter;
    private ArrayList<MenuModel> cardsList;
    private ArrayList<MenuModel> cardsListCath;
    private ArrayList<MenuModel> cardsListOrders;

    private ImageButton shoppingCart;
    private FrameLayout fragmentContainer;
    private FragmentTransaction fragmentTransaction = null;
    private shoppingCartFragment fragment = null;

    private boolean isFragmentOpen = false;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        DatabaseHelper dbHelper = new DatabaseHelper(this.getContext());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.createShopping(db);

        recyclerViewAddresses = view.findViewById(R.id.recyclerViewAddresses);
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewOrders = view.findViewById(R.id.recyclerViewOrders);
        shoppingCart = view.findViewById(R.id.shoppingCartIcon);
        fragmentContainer = view.findViewById(R.id.fragmentContainer);
        logOutBtn = view.findViewById(R.id.logOutBtn);

        cardsList = new ArrayList<>();
        cardsList.add(new MenuModel("Home address", "Namal str. 6", R.drawable.map_small));
        cardsList.add(new MenuModel("Recently used","Herzel str. 4", R.drawable.map_small));
        adapter = new MenuCardsAdapter(this.getContext(), cardsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAddresses.setLayoutManager(layoutManager);
        recyclerViewAddresses.setAdapter(adapter);

        cardsListCath = new ArrayList<>();
        cardsListCath.add(new MenuModel("Fruits", "", R.drawable.fruit));
        cardsListCath.add(new MenuModel("Veggies","", R.drawable.veggi));
  //      cardsListCath.add(new MenuModel("Meat", "", R.drawable.meatCategory));
        adapter = new MenuCardsAdapter(this.getContext(), cardsListCath);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(layoutManager1);
        recyclerViewCategories.setAdapter(adapter);

        cardsListOrders = new ArrayList<>();
        cardsListOrders.add(new MenuModel("Order1", "Total: 800", R.drawable.bag));
        cardsListOrders.add(new MenuModel("Order2", "Total: 559", R.drawable.bag));
        adapter = new MenuCardsAdapter(this.getContext(), cardsListOrders);
        RecyclerView.LayoutManager layoutManagerOrder = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewOrders.setLayoutManager(layoutManagerOrder);
        recyclerViewOrders.setAdapter(adapter);

        logInBtn = view.findViewById(R.id.logInBtn);
        logInBtn.setOnClickListener(v -> moveToActivity(LoginActivity.class));
//        DisplaySavedText();

        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFragmentOpen) {
                    fragmentContainer.setVisibility(View.GONE);
                    isFragmentOpen = false;
                } else {
                    fragmentContainer.setVisibility(View.VISIBLE);
                    ArrayList<ProductModel> shoppingList = new ArrayList<>();
                    String selectAllItemsQuery = "SELECT * FROM shoppingCart;";
                    Cursor cursor = db.rawQuery(selectAllItemsQuery, null);

// Iterate over the cursor to retrieve all items
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                        String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                        int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                        int pic = cursor.getInt(cursor.getColumnIndexOrThrow("pic"));
                        shoppingList.add(new ProductModel(name, pic, price, quantity));
                    }

// Close the cursor and database connection when done
                    fragment = new shoppingCartFragment();
                    fragment.setItemList(shoppingList);
                   // fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragmentContainer, fragment);
                    fragmentTransaction.commit();
                    isFragmentOpen = true;
                }
            }
        });
        DisplaySavedText();
        return view;
    }

    private void moveToActivity (Class<?> cls) {
        Intent i = new Intent(getActivity(),  cls);
        startActivity(i);
    }

    public void onResume() {
        super.onResume();
        Intent intent = getActivity().getIntent();
        String userName = intent.getStringExtra("USER_NAME");

        if(userName != null && FireBaseHelper.getCurrentUser() != null ){
            Log.d("i am onResume()", "i am onResume()");
            DisplayAndSaveUserName(userName);
        }

    }

    @SuppressLint("LongLogTag")
    private void DisplayAndSaveUserName(String userName) {
        //Display the text
        logInBtn.setText("Hey, "+userName);

        //-------store data--------
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //write data to shared perf
        SharedPreferences.Editor editor =sharedPref.edit();
        editor.putString("userName","Hey, "+ userName);
        editor.commit();
        Log.d("i am DisplayAndSaveUserName(String userName)", "i am DisplayAndSaveUserName(String userName)");
        //-------store data--------
        //String storedUserName = sharedPref.getString(getString(R.string.log_in), null);
    }

    @SuppressLint("LongLogTag")
    private void DisplaySavedText() {
        //Reading values from shared preference
        SharedPreferences sharedPref = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String defaultText = sharedPref.getString("userName","Log In");
        //logInBtn.setEnabled(true);
        logInBtn.setText(defaultText);

        if(defaultText!="Log In") {
            logOutBtn.setVisibility(View.VISIBLE);
            logInBtn.setEnabled(false);
            Log.d("i am defaultText!=Log In", "i am defaultText!=Log In");
            logOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Logout")
                            .setMessage("Are you sure you want to Log Out?")
                            .setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Remove user name from SharedPreferences
                                    SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.remove("userName");
                                    editor.apply();
                                    FireBaseHelper.logOutUser();
                                    Toast.makeText(getContext(), "Successfully logged out.",
                                            Toast.LENGTH_SHORT).show();
                                    logOutBtn.setVisibility(View.INVISIBLE);
                                    logInBtn.setEnabled(true);
                                    logInBtn.setText("Log In");
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getContext(), "You are still logged in.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            });
        }
        else {
            //Toast.makeText(getContext(),"Hello, Please log in or sign in for using the app :)",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getContext(),  LoginActivity.class);
            String userName=null;
            i.putExtra("USER_NAME", userName);
            //startActivity(i);
        }
    }




}