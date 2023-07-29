package com.example.super_app;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {


    private Button logInBtn;
    private Button logOutBtn;
    FireBaseHelper fireBaseHelper;
    private ArrayList<MenuModel> cardsListOrders;
    MenuCardsAdapter adapter;

    private FragmentTransaction fragmentTransaction = null;


    private boolean isFragmentOpen = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        DatabaseHelper dbHelper = new DatabaseHelper(this.getContext());
        fireBaseHelper = new FireBaseHelper();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.createShopping(db);

        RecyclerView recyclerViewAddresses = view.findViewById(R.id.recyclerViewAddresses);
        RecyclerView recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        RecyclerView recyclerViewOrders = view.findViewById(R.id.recyclerViewOrders);
        logOutBtn = view.findViewById(R.id.logOutBtn);

        ArrayList<MenuModel> cardsList = new ArrayList<>();
        cardsList.add(new MenuModel("Home address", "Namal str. 6", R.drawable.map_small));
        cardsList.add(new MenuModel("Recently used","Herzel str. 4", R.drawable.map_small));
        adapter = new MenuCardsAdapter(this.getContext(), cardsList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewAddresses.setLayoutManager(layoutManager);
        recyclerViewAddresses.setAdapter(adapter);

        ArrayList<MenuModel> cardsListCath = new ArrayList<>();
        cardsListCath.add(new MenuModel("Fruits", "", R.drawable.orange));
        cardsListCath.add(new MenuModel("Veggies","", R.drawable.cabbage));
        cardsListCath.add(new MenuModel("Meat", "", R.drawable.steak));
        cardsListCath.add(new MenuModel("Beverages", "", R.drawable.coke));
        cardsListCath.add(new MenuModel("Bakery","", R.drawable.croissant));
        cardsListCath.add(new MenuModel("Electronics", "", R.drawable.fridge));
        adapter = new MenuCardsAdapter(this.getContext(), cardsListCath);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(layoutManager1);
        recyclerViewCategories.setAdapter(adapter);

        cardsListOrders = new ArrayList<>();

        adapter = new MenuCardsAdapter(this.getContext(), cardsListOrders);
        RecyclerView.LayoutManager layoutManagerOrder = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewOrders.setLayoutManager(layoutManagerOrder);
        recyclerViewOrders.setAdapter(adapter);
        fetchOrdersFromFirestore();

        logInBtn = view.findViewById(R.id.logInBtn);
        logInBtn.setOnClickListener(v -> moveToActivity(LoginActivity.class));
        logOutBtn.setVisibility(View.INVISIBLE);
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
    //data store
    //logout logic
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
                                    //after logout, go to login
                                    Intent i = new Intent(getContext(),  LoginActivity.class);
                                    startActivity(i);
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

    private void fetchOrdersFromFirestore() {
        fireBaseHelper.fetchOrdersFromFirestore(new FireBaseHelper.FirestoreFetchListener() {
            @Override
            public void onOrderFetch(List<Order> orders) {
                // Clear the current list and add all fetched orders
                cardsListOrders.clear();
                for (Order order : orders) {
                    // Extract the relevant information from the Order object
                    String address = order.getAddress();
                    String deliveryDate = order.getDeliveryDate();
                    String orderId = order.getId();

                    // Create a MenuModel object with the extracted information
                    String orderTitle = address;
                    String orderDescription = deliveryDate;
                    // Replace R.drawable.your_order_image_resource with the appropriate drawable resource ID
                    MenuModel menuModel = new MenuModel(orderTitle, orderDescription, R.drawable.bag);
                    cardsListOrders.add(menuModel);
                }
                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle the failure to fetch orders if needed
            }
        });
    }


}