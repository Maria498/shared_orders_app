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

        List<Product> productListVeg = new ArrayList<>();
        productListVeg.add(new Product("banana", 10.99, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/1.png?alt=media&token=16639688-d525-4e5f-9198-4a0bf12cb6ab", "FruitsAndVeg"));
        productListVeg.add(new Product("lettuce", 10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/10.png?alt=media&token=96378319-c37e-4d61-a423-5fb5d78807ae", "FruitsAndVeg"));
        productListVeg.add(new Product("apple", 5.90, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/2.png?alt=media&token=c9450903-a557-4aa1-8fa1-6778f36e01bf", "FruitsAndVeg"));
        productListVeg.add(new Product("lemon", 6, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/3.png?alt=media&token=5709440c-bca2-48c8-bafd-da1fb2354441", "FruitsAndVeg"));
        productListVeg.add(new Product("orange", 7, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/4.png?alt=media&token=4372bb57-6c67-49b7-9501-fda2483218a0", "FruitsAndVeg"));
        productListVeg.add(new Product("tomato", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/5.png?alt=media&token=e1ca86fc-1148-4111-9ac8-ff6673211542", "FruitsAndVeg"));
        productListVeg.add(new Product("bell pepper", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/6.png?alt=media&token=86c96f14-1784-4135-b159-405f65e83805", "FruitsAndVeg"));
        productListVeg.add(new Product("onion", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/7.png?alt=media&token=e3816691-698a-4117-9247-c34aeb871701", "FruitsAndVeg"));
        productListVeg.add(new Product("watermelon", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/8.png?alt=media&token=baf7e893-4608-4398-9f50-75c52ede8688", "FruitsAndVeg"));
        productListVeg.add(new Product("mushrooms", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/9.png?alt=media&token=feb9940e-d8b0-402f-aeee-ea3054fbbfaa", "FruitsAndVeg"));
        for (Product product : productListVeg) {
            HashMap<String, Object> productData = new HashMap<>();
            productData.put("name", product.getName());
            productData.put("price", product.getPrice());
            productData.put("img", product.getImageResId());
            productData.put("category", product.getCategory());
            db.collection("FruitsAndVeg").add(productData);
        }

        List<Product> productListBrush = new ArrayList<>();
        productListBrush.add(new Product("Eye shadow palette", 100, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/11.png?alt=media&token=323b631a-fa62-4488-979a-8ab6201b15ae", "MakeUpAndBrush"));
        productListBrush.add(new Product("brush", 25, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/12.png?alt=media&token=e34f51ee-f0f6-454d-9f21-e527ec77cb8c", "MakeUpAndBrush"));
        productListBrush.add(new Product("Lipgloss", 40, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/13.png?alt=media&token=4273d45c-ad92-4919-8302-23105bbfbc22", "MakeUpAndBrush"));
        productListBrush.add(new Product("Comb", 34, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/14.png?alt=media&token=40d3c102-752f-4eac-820e-2ed746eb69ee", "MakeUpAndBrush"));
        productListBrush.add(new Product("blush", 50, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/29.png?alt=media&token=7889d7c3-2db6-4e36-888a-65659288de0a", "MakeUpAndBrush"));
        productListBrush.add(new Product("lipstick", 35, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/31.png?alt=media&token=3a414eb3-3e1d-4c0e-a377-4ef09daeb24b", "MakeUpAndBrush"));
        productListBrush.add(new Product("liquid blush", 35, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/32.png?alt=media&token=3e33f987-26bd-43ec-bb93-a8b8733419cd", "MakeUpAndBrush"));
        productListBrush.add(new Product("makeup", 60, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/33.png?alt=media&token=2776cca0-2cf6-4a7d-92e1-1cd7e39abdf1", "MakeUpAndBrush"));
        for (Product product : productListBrush) {
            HashMap<String, Object> productData = new HashMap<>();
            productData.put("name", product.getName());
            productData.put("price", product.getPrice());
            productData.put("img", product.getImageResId());
            productData.put("category", product.getCategory());
            db.collection("MakeUpAndBrush").add(productData);
        }
    }
}
