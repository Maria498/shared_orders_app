package com.example.super_app.db;

import android.content.Context;
import android.widget.Toast;
import com.example.super_app.db.entity.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireBaseHelper {
    private final DatabaseReference mDatabase;
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Context context;

    public FireBaseHelper() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public static String getCurrentUser()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            return name;
        }
        return null;
    }

    public static void logOutUser(){
        mAuth.signOut();
    }


    public FireBaseHelper(Context context) {
        this.context = context;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

// ... (Other imports and code)

    public void addProductToFirestore(String name, String category, double price, int discount, String description, String imageUrl) {
        // Create a new Product instance with the provided data and image URL
        Product product = new Product(name, price, imageUrl, category, discount, description);

        // Add the new product to Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products")
                .add(product.toMap())
                .addOnSuccessListener(documentReference -> {
                    // Product added successfully to Firestore
                    // You can perform any success actions here if needed
                    showToast("Product added successfully!");
                })
                .addOnFailureListener(e -> {
                    // Failed to add the product to Firestore
                    // You can handle any failure actions here if needed
                    showToast("Failed to add product. Please try again.");
                });
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }




}
