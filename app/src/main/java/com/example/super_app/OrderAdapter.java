package com.example.super_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.entity.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the order_item.xml layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        // Bind the order data to the views in the order_item.xml layout
        Order order = orderList.get(position);
        holder.orderNameTextView.setText(order.getFullNameOwner());
        holder.orderPriceTextView.setText(String.valueOf(order.getTotalPrice()));
        holder.deliveryDateTextView.setText(order.getDeliveryDate()); // Set delivery date
        holder.addressTextView.setText(order.getAddress()); // Set address
        holder.orderStatusTextView.setText(order.isOpen() ? "Open" : "Closed"); // Set order status (open or closed)
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderNameTextView;
        TextView orderPriceTextView;
        TextView deliveryDateTextView; // TextView for delivery date
        TextView addressTextView; // TextView for address
        TextView orderStatusTextView; // TextView for order status (open or closed)

        public OrderViewHolder(@NonNull View itemView) {
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
