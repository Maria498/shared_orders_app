package com.example.super_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private FireBaseHelper fireBaseHelper;
    private MenuCardsAdapter adapter;
    private ArrayList<MenuModel> cardsListOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        Button backBtn = view.findViewById(R.id.backBtn);
        Button buttonUpdateOrder = view.findViewById(R.id.buttonUpdateOrder);
        Button buttonCreateOrder = view.findViewById(R.id.buttonCreateOrder);

        fireBaseHelper = new FireBaseHelper();
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));
        buttonCreateOrder.setOnClickListener(v -> moveToActivity(CreateNewOrderActivity.class));

        RecyclerView recyclerViewOrders = view.findViewById(R.id.recyclerViewOrders);
        cardsListOrders = new ArrayList<>();

        // Initialize the adapter and set it to the RecyclerView
        adapter = new MenuCardsAdapter(this.getContext(), cardsListOrders);
        RecyclerView.LayoutManager layoutManagerOrder = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewOrders.setLayoutManager(layoutManagerOrder);
        recyclerViewOrders.setAdapter(adapter);

        // Fetch orders from Firestore and populate cardsListOrders
        fetchOrdersFromFirestore();


        return view;
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
                    double totalPrice = order.getTotalPrice();

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

    private void moveToActivity(Class<?> cls) {
        Intent i = new Intent(getActivity(), cls);
        startActivity(i);
    }


}
