package com.example.super_app;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.entity.Cart;
import com.example.super_app.db.entity.Product;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Cart> cartList;
    private Context context;

    public CartAdapter(Context context) {
        this.context = context;
        this.cartList = new ArrayList<>();
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.bind(cart);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {

        private TextView cartIdTextView;
        private TextView dateTextView;
        private TextView totalTextView;
        private TextView discountTextView;
        private TextView productsTextView;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartIdTextView = itemView.findViewById(R.id.cartIdTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            totalTextView = itemView.findViewById(R.id.totalTextView);
            discountTextView = itemView.findViewById(R.id.discountTextView);
            productsTextView = itemView.findViewById(R.id.productsTextView);
        }

        void bind(Cart cart) {
            cartIdTextView.setText("Cart ID: " + cart.getCardId());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            dateTextView.setText("Date: " + dateFormat.format(cart.getDate()));

            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            totalTextView.setText("Total: $" + decimalFormat.format(cart.getTotal()));

            discountTextView.setText("Discount: " + cart.getDiscount() + "%");

            StringBuilder productsText = new StringBuilder();
            for (Product product : cart.getProductsQuantity().keySet()) {
                double quantity = cart.getProductsQuantity().get(product);
                productsText.append(product.getName()).append(" (Quantity: ").append(quantity).append("), ");
            }
            productsTextView.setText("Products: " + productsText.toString());
        }
    }
}
