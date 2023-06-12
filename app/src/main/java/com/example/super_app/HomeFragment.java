package com.example.super_app;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;


import com.example.super_app.db.DatabaseHelper;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


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
    FragmentTransaction fragmentTransaction = null;
    shoppingCartFragment fragment = null;

    private boolean isFragmentOpen = false;
    FragmentManager fragmentManager;

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

        return view;
    }

    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getActivity(),  cls);
        startActivity(i);

    }


}