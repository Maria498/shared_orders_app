package com.example.super_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.entity.Order;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class UserOrderHistoryAdapter extends RecyclerView.Adapter<UserOrderHistoryAdapter.OrderViewHolder> {
    private List<Order> userOrderHistory;

    public UserOrderHistoryAdapter(List<Order> userOrderHistory) {
        this.userOrderHistory = userOrderHistory;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_order_item, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        // Bind the data from the userOrderHistory list to the views in user_order_item.xml layout
        Order order = userOrderHistory.get(position);
        holder.orderNameTextView.setText(order.getFullNameOwner());
        holder.orderPriceTextView.setText(String.valueOf(order.getTotalPrice()));
        holder.deliveryDateTextView.setText(order.getDeliveryDate()); // Set delivery date
        holder.addressTextView.setText(order.getAddress()); // Set address
        holder.orderStatusTextView.setText(order.isOpen() ? "Open" : "Closed");
        // Bind other views if needed
    }

    @Override
    public int getItemCount() {
        return userOrderHistory.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderNameTextView;
        TextView orderPriceTextView;
        TextView deliveryDateTextView; // TextView for delivery date
        TextView addressTextView; // TextView for address
        TextView orderStatusTextView; // TextView for order status (open or closed)

        public OrderViewHolder(@androidx.annotation.NonNull View itemView) {
            super(itemView);
            // Initialize the views in the order_item.xml layout
            orderNameTextView = itemView.findViewById(R.id.orderNameTextView);
            orderPriceTextView = itemView.findViewById(R.id.orderPriceTextView);
            deliveryDateTextView = itemView.findViewById(R.id.deliveryDateTextView); // Initialize delivery date view
            addressTextView = itemView.findViewById(R.id.addressTextView); // Initialize address view
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView); // Initialize order status view
            // You can initialize other views here based on your layout
        }
    }
}
