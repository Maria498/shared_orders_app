package com.example.super_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome;
    private Button buttonOrderHistory;
    private Button buttonUpdateUserData;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Find views by their IDs
        textViewWelcome = findViewById(R.id.textViewWelcome);
        buttonOrderHistory = findViewById(R.id.buttonOrderHistory);
        buttonUpdateUserData = findViewById(R.id.buttonUpdateUserData);
        //todo - change navigation
        buttonOrderHistory.setOnClickListener(v -> moveToActivity(MainActivity.class));
        buttonUpdateUserData.setOnClickListener(v -> moveToActivity(MainActivity.class));
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));

        buttonOrderHistory.setOnClickListener(v -> {
            // Handle button click for order history
            // Add your code here
        });

        DisplaySavedText();


    }
    public void onResume() {
        super.onResume();
        DisplaySavedText();
    }

    private void DisplaySavedText() {
        //Reading values from shared preference
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String defaultText = sharedPref.getString("userName", "Log In");
        //logInBtn.setEnabled(true);
        textViewWelcome.setText(defaultText);
    }

    private void moveToActivity (Class<?> cls) {
        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "msg");
        startActivity(i);
    }
}