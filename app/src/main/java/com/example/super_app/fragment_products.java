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
        List<Product> electronics = new ArrayList<>();
        electronics.add(new Product("pc monitor", 1000, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/20.png?alt=media&token=047fd7bb-1671-451c-a90d-d91ba6d093af", "electronics"));
        electronics.add(new Product("fridge", 2500, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/21.png?alt=media&token=5aea7b5e-cbb7-4c86-b7c9-c72c3edffffc", "electronics"));
        electronics.add(new Product("oven", 800, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/22.png?alt=media&token=00ca6055-4fd9-4bc6-8767-f3396275be97", "electronics"));
        electronics.add(new Product("washing machine", 750, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/23.png?alt=media&token=8043d3cf-a194-4162-a2b8-3dfe18156c23", "electronics"));
        electronics.add(new Product("microwave", 150, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/24.png?alt=media&token=d13af68d-73d2-4fe4-8500-4e9f239efed0", "electronics"));
        electronics.add(new Product("kettle", 55, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/25.png?alt=media&token=0cf1fa39-8f0a-42d9-8d18-b06615c0dd3e", "electronics"));
        electronics.add(new Product("toaster", 70, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/26.png?alt=media&token=6f7618cd-4d6b-4031-92de-394ae61d685f", "electronics"));
        electronics.add(new Product("blender", 120, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/27.png?alt=media&token=0a9218db-8db9-49af-8a7b-1d3756e3f64c", "electronics"));
        electronics.add(new Product("I robot", 450, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/28.png?alt=media&token=28faf0a1-7af6-4cfb-a836-081dd05b4f0c", "electronics"));

        for (Product product : electronics) {
            HashMap<String, Object> productData = new HashMap<>();
            productData.put("name", product.getName());
            productData.put("price", product.getPrice());
            productData.put("img", product.getImageResId());
            productData.put("category", product.getCategory());
            db.collection("MakeUpAndBrush").add(productData);
        }
        List<Product> productListMeat = new ArrayList<>();
        productListMeat.add(new Product("steak", 120, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/15.png?alt=media&token=8f85e446-5f0d-4292-a6a2-59db2c2d464e", "Meat"));
        productListMeat.add(new Product("chicken", 60, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/16.png?alt=media&token=a362194c-790d-489f-a2ab-5591a780c215", "Meat"));
        productListMeat.add(new Product("edges", 90, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/17.png?alt=media&token=a25409dd-5382-4f9d-a833-6971fa165410", "Meat"));
        productListMeat.add(new Product("wings", 55, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/18.png?alt=media&token=97459668-aaa1-40e3-9749-5259c7cdb8e4", "Meat"));
        productListMeat.add(new Product("Meatballs", 30, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/19.png?alt=media&token=7cc8caef-dd59-4ef0-a879-447ebfe048d5", "Meat"));
        for (Product product : productListMeat) {
            HashMap<String, Object> productData = new HashMap<>();
            productData.put("name", product.getName());
            productData.put("price", product.getPrice());
            productData.put("img", product.getImageResId());
            productData.put("category", product.getCategory());
            db.collection("Meat").add(productData);
        }
    }




    }




