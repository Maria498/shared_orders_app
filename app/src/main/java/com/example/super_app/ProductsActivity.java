package com.example.super_app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.Cart;
import com.example.super_app.db.entity.Product;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener {

    Cart cart;
    private String selectedCategory;
    private ArrayList<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;
    FireBaseHelper fireBaseHelper = new FireBaseHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));

        Intent intent = getIntent();
        TextView categoryName = findViewById(R.id.categoryName);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        productAdapter = new ProductAdapter(this, productList);
        productAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(productAdapter);

        if (intent.hasExtra("msg")) {
            String category = intent.getStringExtra("msg");
            categoryName.setText(category);

            // Fetch products from Firestore for the selected category
            fireBaseHelper.getProductsByCategory(category, new FireBaseHelper.ProductFetchListener() {
                @Override
                public void onProductFetch(List<Product> products) {
                    // Update the productList with the retrieved products
                    productList.clear();
                    productList.addAll(products);
                    productAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(String errorMessage) {
                    // Handle any error that occurred during product fetch
                }
            });
        }

        cart = new Cart("my_first_cart", Calendar.getInstance().getTime(), 0, 0);
    }


    @Override
    public void onItemClick(Product product) {
        showProductDialog(product);
    }

    @SuppressLint("DefaultLocale")
    private void showProductDialog(Product product) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_product);
        dialog.setCancelable(true);

        ImageView productImage = dialog.findViewById(R.id.popupProductImage);
        TextView productName = dialog.findViewById(R.id.popupProductName);
        TextView productPrice = dialog.findViewById(R.id.popupProductPrice);
        EditText quantityEditText = dialog.findViewById(R.id.quantityEditText);
        Button addToCartButton = dialog.findViewById(R.id.addToCartButton);

        productPrice.setText(String.format("$%.2f", product.getPrice()));

        addToCartButton.setOnClickListener(v -> {
            String quantityString = quantityEditText.getText().toString().trim();
            //todo add the product to the cart logic
            if (!quantityString.isEmpty()) {
                int quantity = Integer.parseInt(quantityString);
                //todo add the product to the cart logic

            } else {
                // case when the quantity is empty
            }
            dialog.dismiss();
        });

        dialog.show();
    }
    interface FireStoreCallback {
        void onProductsFetched(ArrayList<Product> products);

        void onError(String errorMessage);
    }

    // Method to fetch products from Firestore
    private void fetchProductsFromFirestore(String category, FireStoreCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference productsRef = db.collection("Product");

        productsRef.whereEqualTo("category", category)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Product> productsList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Product product = documentSnapshot.toObject(Product.class);
                        productsList.add(product);
                    }
                    callback.onProductsFetched(productsList);
                })
                .addOnFailureListener(e -> {
                    callback.onError(e.getMessage());
                });
    }

    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "msg");
        startActivity(i);

    }
}
