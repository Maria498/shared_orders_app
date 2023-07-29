package com.example.super_app;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerOrderHistory;
    private TextView emptyOrderHistoryTextView;
    private UserOrderHistoryAdapter userOrderHistoryAdapter;
    private List<Order> userOrderHistory;
    private FireBaseHelper fireBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        fireBaseHelper = new FireBaseHelper(this);
        userOrderHistory = new ArrayList<>();

        recyclerOrderHistory = findViewById(R.id.recyclerOrderHistory);
        emptyOrderHistoryTextView = findViewById(R.id.emptyOrderHistoryTextView);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back button on the toolbar

        // Set up the RecyclerView and its adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerOrderHistory.setLayoutManager(layoutManager);
        userOrderHistoryAdapter = new UserOrderHistoryAdapter(userOrderHistory);
        recyclerOrderHistory.setAdapter(userOrderHistoryAdapter);

        // Show or hide the empty order history message
        updateEmptyOrderHistoryMessage();

        getAllUserOrders();
    }

    private void updateEmptyOrderHistoryMessage() {
        if (userOrderHistory.isEmpty()) {
            recyclerOrderHistory.setVisibility(View.GONE);
            emptyOrderHistoryTextView.setVisibility(View.VISIBLE);
        } else {
            recyclerOrderHistory.setVisibility(View.VISIBLE);
            emptyOrderHistoryTextView.setVisibility(View.GONE);
        }
    }

    public void getAllUserOrders() {
        FireBaseHelper.UserOrdersHFetchListener listener = new FireBaseHelper.UserOrdersHFetchListener() {
            @Override
            public void onUserOrdersFetch(List<Order> userOrdersList) {
                userOrderHistory.clear();
                userOrderHistory.addAll(userOrdersList);
                userOrderHistoryAdapter.notifyDataSetChanged();
                updateEmptyOrderHistoryMessage();
            }

            @Override
            public void onFailure(String errorMessage) {
                System.err.println("Error fetching user's orders: " + errorMessage);
            }
        };
        fireBaseHelper.fetchOrdersHistoryOfCurrentUserFromFirebase(listener);
    }
}
