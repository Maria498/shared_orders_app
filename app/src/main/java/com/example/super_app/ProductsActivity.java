package com.example.super_app;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.Cart;
import com.example.super_app.db.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener {

    private Cart cart = FireBaseHelper.getCart();
    private String selectedCategory;
    private ArrayList<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private FireBaseHelper fireBaseHelper;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        setContentView(R.layout.activity_products);
        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));
        fireBaseHelper = new FireBaseHelper(this);
        fireBaseHelper.initializeCart();
        cart = fireBaseHelper.getCart();
        dbHelper.printAllProducts();
        Intent intent = getIntent();
        TextView categoryName = findViewById(R.id.categoryName);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        productAdapter = new ProductAdapter(this, productList);
        productAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(productAdapter);

        //sqlite



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
        productName.setText(product.getName());
        productPrice.setText(String.format("$%.2f", product.getPrice()));
        // Load the product image using Glide
        if (product.getImageUrl() != null) {
            RequestOptions requestOptions = new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .priority(Priority.HIGH)
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image);

            Glide.with(this)
                    .load(product.getImageUrl()) // Use the image URL here
                    .apply(requestOptions)
                    .into(productImage);
        } else {
            // If the image URL is null, load a default image
            productImage.setImageResource(R.drawable.default_image);
        }
        if (cart.getProductsQuantity().containsKey(product)) {
            int existingQuantity = cart.getProductsQuantity().get(product);
            quantityEditText.setText(String.valueOf(existingQuantity));
        }
        // Inside showProductDialog method after adding the product to the cart
        addToCartButton.setOnClickListener(v -> {
            String quantityString = quantityEditText.getText().toString().trim();

            if (!quantityString.isEmpty()) {
                int quantity = Integer.parseInt(quantityString);

                // Set the quantity in the product object
                product.setQuantity(quantity);

                // Check if the product is already in the cart
                if (cart.getProductsQuantity().containsKey(product)) {
                    // If the product already exists in the cart, update the quantity
                    int existingQuantity = cart.getProductsQuantity().get(product);
                    cart.getProductsQuantity().put(product, existingQuantity + quantity);
                } else {
                    // If the product is not in the cart, add it with the given quantity
                    cart.getProductsQuantity().put(product, quantity);
                }
                // Calculate the updated total price of the cart
                double updatedTotal = cart.getTotal() + (product.getPrice() * quantity);
                cart.setTotal(updatedTotal);
                fireBaseHelper.updateCartQuantity(product, quantity);

                // sqllite
                dbHelper.addCart(cart);
                Toast.makeText(this, "Item added to cart!", Toast.LENGTH_SHORT).show();
                Log.w("ProductsActivity", "++++++++++HashMap: " + cart.getProductsQuantity());

                dialog.dismiss();
            } else {
                // Show an error message or handle the case when the quantity is empty
            }
        });

        dialog.show();
    }



    private void moveToActivity (Class<?> cls) {
        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "msg");
        startActivity(i);
    }

}