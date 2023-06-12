package com.example.super_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class SuperCategoryActivity extends AppCompatActivity {
    GridView gridview;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_category);
        gridview=(GridView)findViewById(R.id.gridCategory);





    }
}