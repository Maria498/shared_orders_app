package com.example.super_app;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.super_app.db.entity.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class fragment_products extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_products);

        db = FirebaseFirestore.getInstance();

        List<Product> productList = new ArrayList<>();
        productList.add(new Product("banana", 10.99, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/1.png?alt=media&token=16639688-d525-4e5f-9198-4a0bf12cb6ab", "FruitsAndVeg"));
        productList.add(new Product("lettuce", 10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/10.png?alt=media&token=96378319-c37e-4d61-a423-5fb5d78807ae", "FruitsAndVeg"));
        productList.add(new Product("apple", 5.90, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/2.png?alt=media&token=c9450903-a557-4aa1-8fa1-6778f36e01bf", "FruitsAndVeg"));
        productList.add(new Product("lemon", 6, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/3.png?alt=media&token=5709440c-bca2-48c8-bafd-da1fb2354441", "FruitsAndVeg"));
        productList.add(new Product("orange", 7, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/4.png?alt=media&token=4372bb57-6c67-49b7-9501-fda2483218a0", "FruitsAndVeg"));
        productList.add(new Product("tomato", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/5.png?alt=media&token=e1ca86fc-1148-4111-9ac8-ff6673211542", "FruitsAndVeg"));
        productList.add(new Product("bell pepper", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/6.png?alt=media&token=86c96f14-1784-4135-b159-405f65e83805", "FruitsAndVeg"));
        productList.add(new Product("onion", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/7.png?alt=media&token=e3816691-698a-4117-9247-c34aeb871701", "FruitsAndVeg"));
        productList.add(new Product("watermelon", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/8.png?alt=media&token=baf7e893-4608-4398-9f50-75c52ede8688", "FruitsAndVeg"));
        productList.add(new Product("mushrooms", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/9.png?alt=media&token=feb9940e-d8b0-402f-aeee-ea3054fbbfaa", "FruitsAndVeg"));

        for (Product product : productList) {
            HashMap<String, Object> productData = new HashMap<>();
            productData.put("name", product.getName());
            productData.put("price", product.getPrice());
            productData.put("img", product.getImageResId());
            productData.put("category", product.getCategory());
            db.collection("FruitsAndVeg").add(productData);
        }
    }
}
