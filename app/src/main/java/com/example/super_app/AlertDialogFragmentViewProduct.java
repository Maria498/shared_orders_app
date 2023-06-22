package com.example.super_app;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
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
import com.example.super_app.AlertDialogFragmentListener;
import com.example.super_app.db.entity.Order;
import com.example.super_app.db.entity.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class AlertDialogFragmentViewProduct extends DialogFragment {
    private ImageView imageView;
    private TextView productNameTextView;
    private TextView descriptionTextView;
    private TextView productDescribeTextView;
    private TextView price;
    private ImageButton plusButton;
    private TextView quantityTextView;
    private ImageButton minusButton;
    private Button addButton;
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
        imageView = v.findViewById(R.id.imgPro);
        productNameTextView = v.findViewById(R.id.productNameTextView);
        descriptionTextView = v.findViewById(R.id.descriptionTextView);
        productDescribeTextView = v.findViewById(R.id.productDescribeTextView);
        price = v.findViewById(R.id.price);
        plusButton = v.findViewById(R.id.pluseButton);
        quantityTextView = v.findViewById(R.id.digitEditText);
        minusButton = v.findViewById(R.id.minusButton);
        addButton = v.findViewById(R.id.addbtn);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        if (b != null) {
            product = (Product) b.getSerializable("Product");
            productNameTextView.setText(product.getName());
            if(product.getDiscount()!=0)
            {
                priceAfterDis = product.getPrice()*(100-product.getDiscount())/100;
            }
            else{
                priceAfterDis = product.getPrice();
            }
            price.setText(""+priceAfterDis);

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

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(quantityTextView.getText().toString());
                    if (quantity < 100) {
                        quantity++;
                        quantityTextView.setText(String.valueOf(quantity));
                        double newPrice = priceAfterDis * quantity;
                        price.setText(String.valueOf(newPrice));
                        product.setQuantity(quantity);
                        product.setPrice(newPrice);
                    }
                }
            });

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(quantityTextView.getText().toString());
                    if (quantity > 0) {
                        quantity--;
                        quantityTextView.setText(String.valueOf(quantity));
                        double newPrice = priceAfterDis * quantity;
                        price.setText(String.valueOf(newPrice));
                        product.setQuantity(quantity);
                        product.setPrice(newPrice);
                    }
                }
            });
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Retrieve the Intent that started the current Activity
                Intent intent = getActivity().getIntent();
                // Check if the Intent has extra parameters
                if (intent != null /*&& intent.getExtras() != null*/) {
                   // String type = intent.getStringExtra("typeOfUser");
                    if (/*type.equals("Owner")*/true) {
                        String uid = mAuth.getUid();
                        db.collection("Orders").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null && document.exists()) {
                                        Order order = document.toObject(Order.class);
                                        HashMap<String, ArrayList<Product>> neighProducts = order.getProductsOfNeigh();
                                        if (neighProducts == null) {
                                            neighProducts = new HashMap<>();
                                            listofProduct = new ArrayList<>();
                                        } else {
                                            listofProduct = neighProducts.get(uid);
                                            if (listofProduct == null) {
                                                listofProduct = new ArrayList<>();
                                            }
                                        }
                                        listofProduct.add(product);
                                        neighProducts.put(uid, listofProduct);
                                        order.setProductsOfNeigh(neighProducts);
                                        db.collection("Orders").document(uid).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                                                }
                                                dismiss();

                                            }
                                        });
                                    }
                                } else {
                                    Log.e("Firebase", "Error getting order document: ", task.getException());
                                }
                            }
                        });
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            Toast.makeText(getContext(), "You must join an open order or create a new order to add products", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });

        return v;
    }
}
