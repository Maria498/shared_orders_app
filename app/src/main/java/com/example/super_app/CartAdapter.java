package com.example.super_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.super_app.db.entity.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<Product> productList;
    private Context context;

    public CartAdapter(Context context, List<Product> productList) {
        this.productList = productList;
        this.context = context;
    }
    public void updateCartProductsList(List<Product> products) {
        this.productList = products;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }
    // Inside CartAdapter
    private void loadImage(String imageUrl, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image);

        Glide.with(context)
                .load(imageUrl)
                .apply(requestOptions)
                .into(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productName.setText(product.getName());
        holder.productQuantity.setText("Quantity: " + product.getQuantity());
        holder.productTotalPrice.setText("Total Price: $" + String.format("%.2f", product.getQuantity() * product.getPrice()));

        // Load the product image using Glide
        loadImage(product.getImageUrl(), holder.cartProductImage);
    }


    // Define an interface to handle item clicks
    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    // Member variable to hold the click listener
    private OnItemClickListener itemClickListener;

    // Method to set the click listener from the outside
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public TextView productQuantity;
        public TextView productTotalPrice;
        public ImageView cartProductImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.cartProductName);
            productQuantity = itemView.findViewById(R.id.cartProductQuantity);
            productTotalPrice = itemView.findViewById(R.id.cartProductTotalPrice);
            cartProductImage = itemView.findViewById(R.id.cartProductImage); // <-- Initialize it here

        }
    }
}
