package com.example.super_app;

import android.content.Context;
import android.util.Log;
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
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> productList;
    private OnItemClickListener onItemClickListener;
    private Context context;
    RequestOptions requestOptions = new RequestOptions();


    public ProductAdapter(Context context, List<Product> productList) {
        this.productList = productList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        // Load the image using Glide and Firebase Storage
        String imageUrl = product.getImageUrl();
        Log.d("ProductAdapter", "========= ImageUrl for " + product.getName() + ": " + product.getImageUrl()); // Add this log to check the imageUrl value

        if (imageUrl != null) {
            // Create a Firebase Storage reference from the image path
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imageUrl);

// Load the image using Glide
            Glide.with(context)
                    .load(storageReference)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(holder.productImage);

        } else {
            // If imageUrl is null, you can handle it accordingly, e.g., show a default image
            holder.productImage.setImageResource(R.drawable.default_image);
        }

        // Bind other data to the views
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf((int) product.getPrice())); // Convert price to String
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(product);
            }
        });
    }



    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }
}
