package com.example.super_app;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.entity.Cart;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class ProductsActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener {

    private ArrayList<ProductModel> productList;
    private ProductAdapter productAdapter;
    private Button backBtn;
    Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        Intent intent = getIntent();
        TextView categoryName = findViewById(R.id.categoryName);
        productList = new ArrayList<>();
        if(intent.hasExtra("msg")) {
            if (Objects.equals(intent.getStringExtra("msg"), "Fruits")) {
                categoryName.setText(R.string.fruits);
                productList.add(new ProductModel(1, "apples", R.drawable.apple, 10.99));
                productList.add(new ProductModel(2, "limes", R.drawable.lime, 10.99));
                productList.add(new ProductModel(3, "strawberry", R.drawable.strawberry, 20.99));
                productList.add(new ProductModel(4, "oranges", R.drawable.orange, 5.99));
                productList.add(new ProductModel(5, "bananas", R.drawable.banana, 8.99));
            }
            else if (Objects.equals(intent.getStringExtra("msg"), "Veggies")) {
                categoryName.setText(R.string.veggie);
                productList.add(new ProductModel(1, "pepper", R.drawable.pepper, 10.99));
                productList.add(new ProductModel(2, "cabbage", R.drawable.cabbage, 15.99));
                productList.add(new ProductModel(3, "broccoli", R.drawable.broccoli, 20.99));
            }
            else if (Objects.equals(intent.getStringExtra("msg"), "Meat")) {
                categoryName.setText(R.string.meat);
                productList.add(new ProductModel(1, "steak", R.drawable.steak, 130.99));
                productList.add(new ProductModel(2, "chicken", R.drawable.chicken, 55.99));
            }
        }
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));


        RecyclerView recyclerView = findViewById(R.id.recyclerViewProducts);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        productAdapter = new ProductAdapter(this, productList);
        productAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(productAdapter);

        cart = new Cart("my_first_cart", Calendar.getInstance().getTime(),0,0);
    }

    @Override
    public void onItemClick(ProductModel product) {
        showProductDialog(product);
    }

    private void showProductDialog(ProductModel product) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_product);
        dialog.setCancelable(true);

        ImageView productImage = dialog.findViewById(R.id.popupProductImage);
        TextView productName = dialog.findViewById(R.id.popupProductName);
        TextView productPrice = dialog.findViewById(R.id.popupProductPrice);
        EditText quantityEditText = dialog.findViewById(R.id.quantityEditText);
        Button addToCartButton = dialog.findViewById(R.id.addToCartButton);

        productImage.setImageResource(product.getProductImage());
        productName.setText(product.getProductName());
        productPrice.setText(String.format("$%.2f", product.getProductPrice()));

        addToCartButton.setOnClickListener(v -> {
            String quantityString = quantityEditText.getText().toString().trim();
            //todo add the product to the cart logic
            if (!quantityString.isEmpty()) {
                int quantity = Integer.parseInt(quantityString);
                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                SQLiteDatabase db = dbHelper.getWritableDatabase();
               // cant put image with the method currently implemented needs to change way of keeping images in the class
                String addItemQuery = "INSERT INTO shoppingCart (name, price, quantity, pic) VALUES ('" + productName.getText().toString() + "', '" + productPrice.getText().toString() + "', " + quantity + ", '" + R.drawable.apple + "');";

                db.execSQL(addItemQuery);


            } else {
                // case when the quantity is empty
            }
            dialog.dismiss();
        });

        dialog.show();
    }

    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "msg");
        startActivity(i);

    }
}
