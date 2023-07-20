package com.example.super_app;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.super_app.db.entity.Order;
import com.example.super_app.db.entity.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class AlertDialogFragmentViewProduct extends DialogFragment {
    private TextView price;
    private TextView quantityTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private AlertDialogFragmentListener mListener;
    private ArrayList<Product> listofProduct;
    private Product product;

    private double priceAfterDis;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AlertDialogFragmentListener) activity;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        View v = inflater.inflate(R.layout.activity_alert_dialog_fragment_view_product, container, false);
        ImageView imageView = v.findViewById(R.id.imgPro);
        TextView productNameTextView = v.findViewById(R.id.productNameTextView);
        TextView descriptionTextView = v.findViewById(R.id.descriptionTextView);
        TextView productDescribeTextView = v.findViewById(R.id.productDescribeTextView);
        price = v.findViewById(R.id.price);
        ImageButton plusButton = v.findViewById(R.id.pluseButton);
        quantityTextView = v.findViewById(R.id.digitEditText);
        ImageButton minusButton = v.findViewById(R.id.minusButton);
        Button addButton = v.findViewById(R.id.addbtn);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (b != null) {
            product = (Product) b.getSerializable("Product");
            productNameTextView.setText(product.getName());
            if (product.getDiscount() != 0) {
                priceAfterDis = product.getPrice() * (100 - product.getDiscount()) / 100;
            } else {
                priceAfterDis = product.getPrice();
            }
            price.setText("" + priceAfterDis);

            if (product.getCategory().equals("Electronics") || product.getCategory().equals("MakeUpAndBrush")) {
                productDescribeTextView.setText(product.getDescription());
            } else {
                productDescribeTextView.setVisibility(View.GONE);
                descriptionTextView.setVisibility(View.GONE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Glide.with(this)
                        .load(product.getImg())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                // Handle image loading failure
                                if (e != null) {
                                    Log.e("Glide", "Image loading failed: " + e.getMessage());
                                }
                                return false; // Return false to allow Glide to handle the error and show any error placeholder you have set
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                // Image successfully loaded
                                return false; // Return false to allow Glide to handle the resource and display it
                            }
                        })
                        .into(imageView);
            }

            plusButton.setOnClickListener(v1 -> {
                int quantity = Integer.parseInt(quantityTextView.getText().toString());
                if (quantity < 100) {
                    quantity++;
                    quantityTextView.setText(String.valueOf(quantity));
                    double newPrice = priceAfterDis * quantity;
                    price.setText(String.valueOf(newPrice));
                    product.setQuantity(quantity);
                    product.setPrice(newPrice);
                }
            });

            minusButton.setOnClickListener(v12 -> {
                int quantity = Integer.parseInt(quantityTextView.getText().toString());
                if (quantity > 0) {
                    quantity--;
                    quantityTextView.setText(String.valueOf(quantity));
                    double newPrice = priceAfterDis * quantity;
                    price.setText(String.valueOf(newPrice));
                    product.setQuantity(quantity);
                    product.setPrice(newPrice);
                }
            });
        }

        addButton.setOnClickListener(view -> {
            String uid = mAuth.getUid();
            db.collection("Orders").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Order order = doc.toObject(Order.class);
                        HashMap<String, ArrayList<Product>> neighProducts = order.getProductsOfNeigh();
                        if (doc.getId().equals(mAuth.getUid()) || (neighProducts != null && neighProducts.containsKey(uid))) {
                            if (neighProducts == null) {
                                neighProducts = new HashMap<>();
                                listofProduct = new ArrayList<>();
                            } else {
                                listofProduct = neighProducts.get(uid);
                                if (listofProduct == null) {
                                    listofProduct = new ArrayList<>();
                                }
                            }
                            if(product.getQuantity()==0)
                            {
                                product.setQuantity(1);
                            }
                            listofProduct.add(product);
                            neighProducts.put(uid, listofProduct);
                            order.setProductsOfNeigh(neighProducts);
                            db.collection("Orders").document(doc.getId()).set(order).addOnSuccessListener(unused -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                                }
                                dismiss();

                            });
                        }
                    }

                } else {
                    Log.e("Firebase", "Error getting order document: ", task.getException());
                }
            });

        });

        return v;
}
}
