package com.example.super_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));
    }

    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "msg");
        startActivity(i);

    }
}