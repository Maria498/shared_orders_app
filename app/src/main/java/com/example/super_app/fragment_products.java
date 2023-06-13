package com.example.super_app;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.entity.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class fragment_products extends Fragment implements RecycleViewInterface {
    private FirebaseFirestore db;
    private RecyclerView rec;
    private List<Product> list = new ArrayList<>();

    private ImageView back;
    public fragment_products() {

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_products, container, false);
        rec = view.findViewById(R.id.recyclerView);
        Bundle args = getArguments();

        LinearLayoutManager llm =new LinearLayoutManager(getContext());
        rec.setLayoutManager(new GridLayoutManager(getContext(),2));
        db = FirebaseFirestore.getInstance();

        if(args!=null) {
            String type = String.valueOf(args.getString("Type"));
            if(type.equals("FruitsAndVeg")) {
                list.add(new Product("banana", 10.99, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/1.png?alt=media&token=16639688-d525-4e5f-9198-4a0bf12cb6ab", "FruitsAndVeg"));
                list.add(new Product("lettuce", 10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/10.png?alt=media&token=96378319-c37e-4d61-a423-5fb5d78807ae", "FruitsAndVeg"));
                list.add(new Product("apple", 5.90, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/2.png?alt=media&token=c9450903-a557-4aa1-8fa1-6778f36e01bf", "FruitsAndVeg"));
                list.add(new Product("lemon", 6, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/3.png?alt=media&token=5709440c-bca2-48c8-bafd-da1fb2354441", "FruitsAndVeg"));
                list.add(new Product("orange", 7, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/4.png?alt=media&token=4372bb57-6c67-49b7-9501-fda2483218a0", "FruitsAndVeg"));
                list.add(new Product("tomato", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/5.png?alt=media&token=e1ca86fc-1148-4111-9ac8-ff6673211542", "FruitsAndVeg"));
                list.add(new Product("bell pepper", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/6.png?alt=media&token=86c96f14-1784-4135-b159-405f65e83805", "FruitsAndVeg"));
                list.add(new Product("onion", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/7.png?alt=media&token=e3816691-698a-4117-9247-c34aeb871701", "FruitsAndVeg"));
                list.add(new Product("watermelon", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/8.png?alt=media&token=baf7e893-4608-4398-9f50-75c52ede8688", "FruitsAndVeg"));
                list.add(new Product("mushrooms", 8.10, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/9.png?alt=media&token=feb9940e-d8b0-402f-aeee-ea3054fbbfaa", "FruitsAndVeg"));
                for (Product product : list) {
                    HashMap<String, Object> productData = new HashMap<>();
                    productData.put("name", product.getName());
                    productData.put("price", product.getPrice());
                    productData.put("img", product.getImageResId());
                    productData.put("category", product.getCategory());
                    db.collection("FruitsAndVeg").add(productData);
                }
            }
            else if(type.equals("MakeUpAndBrush")) {
                list.add(new Product("Eye shadow palette", 100, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/11.png?alt=media&token=323b631a-fa62-4488-979a-8ab6201b15ae", "MakeUpAndBrush"));
                list.add(new Product("brush", 25, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/12.png?alt=media&token=e34f51ee-f0f6-454d-9f21-e527ec77cb8c", "MakeUpAndBrush"));
                list.add(new Product("Lipgloss", 40, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/13.png?alt=media&token=4273d45c-ad92-4919-8302-23105bbfbc22", "MakeUpAndBrush"));
                list.add(new Product("Comb", 34, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/14.png?alt=media&token=40d3c102-752f-4eac-820e-2ed746eb69ee", "MakeUpAndBrush"));
                list.add(new Product("blush", 50, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/29.png?alt=media&token=7889d7c3-2db6-4e36-888a-65659288de0a", "MakeUpAndBrush"));
                list.add(new Product("lipstick", 35, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/31.png?alt=media&token=3a414eb3-3e1d-4c0e-a377-4ef09daeb24b", "MakeUpAndBrush"));
                list.add(new Product("liquid blush", 35, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/32.png?alt=media&token=3e33f987-26bd-43ec-bb93-a8b8733419cd", "MakeUpAndBrush"));
                list.add(new Product("makeup", 60, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/33.png?alt=media&token=2776cca0-2cf6-4a7d-92e1-1cd7e39abdf1", "MakeUpAndBrush"));
                for (Product product : list) {
                    HashMap<String, Object> productData = new HashMap<>();
                    productData.put("name", product.getName());
                    productData.put("price", product.getPrice());
                    productData.put("img", product.getImageResId());
                    productData.put("category", product.getCategory());
                    db.collection("MakeUpAndBrush").add(productData);
                }
            }
            else if(type.equals("Meat")) {
                list.add(new Product("steak", 120, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/15.png?alt=media&token=8f85e446-5f0d-4292-a6a2-59db2c2d464e", "Meat"));
                list.add(new Product("chicken", 60, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/16.png?alt=media&token=a362194c-790d-489f-a2ab-5591a780c215", "Meat"));
                list.add(new Product("edges", 90, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/17.png?alt=media&token=a25409dd-5382-4f9d-a833-6971fa165410", "Meat"));
                list.add(new Product("wings", 55, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/18.png?alt=media&token=97459668-aaa1-40e3-9749-5259c7cdb8e4", "Meat"));
                list.add(new Product("Meatballs", 30, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/19.png?alt=media&token=7cc8caef-dd59-4ef0-a879-447ebfe048d5", "Meat"));
                for (Product product : list) {
                    HashMap<String, Object> productData = new HashMap<>();
                    productData.put("name", product.getName());
                    productData.put("price", product.getPrice());
                    productData.put("img", product.getImageResId());
                    productData.put("category", product.getCategory());
                    db.collection("Meat").add(productData);
                }
            }
            else if(type.equals("Electronics")) {
                list.add(new Product("pc monitor", 1000, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/20.png?alt=media&token=047fd7bb-1671-451c-a90d-d91ba6d093af", "Electronics"));
                list.add(new Product("fridge", 2500, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/21.png?alt=media&token=5aea7b5e-cbb7-4c86-b7c9-c72c3edffffc", "Electronics"));
                list.add(new Product("oven", 800, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/22.png?alt=media&token=00ca6055-4fd9-4bc6-8767-f3396275be97", "Electronics"));
                list.add(new Product("washing machine", 750, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/23.png?alt=media&token=8043d3cf-a194-4162-a2b8-3dfe18156c23", "Electronics"));
                list.add(new Product("microwave", 150, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/24.png?alt=media&token=d13af68d-73d2-4fe4-8500-4e9f239efed0", "Electronics"));
                list.add(new Product("kettle", 55, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/25.png?alt=media&token=0cf1fa39-8f0a-42d9-8d18-b06615c0dd3e", "Electronics"));
                list.add(new Product("toaster", 70, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/26.png?alt=media&token=6f7618cd-4d6b-4031-92de-394ae61d685f", "Electronics"));
                list.add(new Product("blender", 120, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/27.png?alt=media&token=0a9218db-8db9-49af-8a7b-1d3756e3f64c", "Electronics"));
                list.add(new Product("I robot", 450, "https://firebasestorage.googleapis.com/v0/b/mind-66851.appspot.com/o/28.png?alt=media&token=28faf0a1-7af6-4cfb-a836-081dd05b4f0c", "Electronics"));

                for (Product product : list) {
                    HashMap<String, Object> productData = new HashMap<>();
                    productData.put("name", product.getName());
                    productData.put("price", product.getPrice());
                    productData.put("img", product.getImageResId());
                    productData.put("category", product.getCategory());
                    db.collection("Electronics").add(productData);
                }
            }

        }
        productItemAdapter productItemAdapter= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            productItemAdapter = new productItemAdapter(list,getContext(), fragment_products.this);
        }
        rec.setAdapter(productItemAdapter);

        return view;


    }


    @Override
    public void onItemClick(int position) {
        Product product=list.get(position);
        AlertDialogFragmentViewProduct frag=new AlertDialogFragmentViewProduct();
        Bundle b=new Bundle();
        b.putSerializable("Product",product);
        frag.setArguments(b);
        frag.show(getFragmentManager(), "dialog");

    }


}


