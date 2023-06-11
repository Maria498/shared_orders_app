package com.example.super_app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.entity.Product;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import java.util.List;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private static List<Product> productList;
    private final RecycleViewInterface recycleViewInterface;
    private static Context context;

    public OrderAdapter(List<Product> productList, Context context, RecycleViewInterface recycleViewInterface) {
        this.productList = productList;
        this.context = context;
        this.recycleViewInterface = recycleViewInterface;
    }

    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        Log.i("adapter", "ContactViewHolder done!");
        return new OrderViewHolder(itemView,recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        Product product=productList.get(position);
        holder.setData(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImg;
        private TextView productName;
        private TextView productCategory;
        private TextView productQuantity;
        private TextView productPrice;
        private TextView priceAfterDiscount;
        private ImageButton editButton;
        private Product product = null;


        public OrderViewHolder(View v, RecycleViewInterface recycleViewInterface) {
            super(v);
            productImg = v.findViewById(R.id.productImg);
            productName = v.findViewById(R.id.productName);
            productCategory = v.findViewById(R.id.productCategory);
            productQuantity = v.findViewById(R.id.productQuantity);
            productPrice = v.findViewById(R.id.productPrice);
            priceAfterDiscount = v.findViewById(R.id.priceAfterDiscount);
            editButton = v.findViewById(R.id.BtnEdit);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recycleViewInterface!=null)
                    {
                        int position=getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                        {
                            recycleViewInterface.onItemClick(position);
                        }


                    }
                }
            });
        }

        public void setData(Product product) {
            this.product=product;
            productName.setText(product.getName());
            productCategory.setText(product.getCategory());
            productPrice.setText(""+ product.getPrice());
            productQuantity.setText(""+product.getQuantity());
            priceAfterDiscount.setText(" "+ (1-product.getDiscount()/100)*product.getPrice()*product.getQuantity());
            Glide.with(context)
                    .load(product.getImageResId())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // Handle image loading failure
                            Log.e("Glide", "Image loading failed: " + e.getMessage());
                            return false; // Return false to allow Glide to handle the error and show any error placeholder you have set
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // Image successfully loaded
                            return false; // Return false to allow Glide to handle the resource and display it
                        }
                    })
                    .into(productImg);
        }
    }
}
