package com.example.super_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.super_app.db.entity.Product;

import java.util.List;

public class productItemAdapter extends RecyclerView.Adapter<productItemAdapter.ProductItemViewHolder> {
    private List<Product> productList;
    private Context context;
    private RecycleViewInterface recycleViewInterface;
    private boolean isAdmin = false;

    public productItemAdapter(List<Product> productList, Context context, RecycleViewInterface recycleViewInterface) {
        this.productList = productList;
        this.context = context;
        this.recycleViewInterface = recycleViewInterface;
    }


    @Override
    public productItemAdapter.ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ProductItemViewHolder(itemView, recycleViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull productItemAdapter.ProductItemViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.setData(product);

    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView vImg;
        private TextView vName;
        private TextView vPrice;
        private ImageButton vEdit;
        private ImageButton vDelete;
        private Product product = null;

        public ProductItemViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
            super(itemView);
            vImg = itemView.findViewById(R.id.productImg);
            vName = itemView.findViewById(R.id.productName);
            vPrice = itemView.findViewById(R.id.productPrice);
            vEdit = itemView.findViewById(R.id.editBtn);
            vDelete = itemView.findViewById(R.id.removeBtn);

            if (!isAdmin) {
                vEdit.setVisibility(View.GONE);
                vDelete.setVisibility(View.GONE);
            }
            else{
                vDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (recycleViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recycleViewInterface.onItemClick(position);
                        }


                    }
                }
            });
        }

        public void setData(Product product) {
            this.product = product;
            vName.setText(product.getName());
            vPrice.setText("" + product.getPrice());
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
                    .into(vImg);
        }

    }
}
    public void showSimpleAlertDialog(View view) {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message);
        builder. setTitle(R.string.dialog_title);

        // Add the buttons
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Toast.makeText(context, "User clicked OK button",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Toast.makeText(context, "User cancelled the dialog",Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "User clicked on Neutral Btn",Toast.LENGTH_SHORT).show();

            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();

        dialog.show();
    }
}
                });