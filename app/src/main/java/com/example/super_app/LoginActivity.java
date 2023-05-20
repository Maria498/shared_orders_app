package com.example.super_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button backBtn;
    Button signInBtn;
    Button logInBtn;
    EditText email;
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInBtn = findViewById(R.id.signInBtn);
        backBtn = findViewById(R.id.backBtn);
        logInBtn = findViewById(R.id.logInBtn);
        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.editTextTextPassword3);

        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));

        logInBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));
        signInBtn.setOnClickListener(v -> moveToActivity(SigninActivity.class));

    }

    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "Data collector");
        startActivity(i);

    }


}