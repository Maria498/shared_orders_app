package com.example.super_app;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.entity.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SigninActivity  extends AppCompatActivity {

    private Button backBtn;
    private Button signInBtn;
    private EditText userName;
    private EditText userAddress;
    private EditText email;
    private EditText password;
    private Context context;
    private ArrayList<User> usersFromDB;
    private DatabaseHelper db;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        context = getApplicationContext();

        signInBtn = findViewById(R.id.signInBtn);
        backBtn = findViewById(R.id.backBtn);
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.editTextTextPassword);
        userAddress = findViewById(R.id.address);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));

        db = new DatabaseHelper(context);
        Log.d("db.getAllUsers()", String.valueOf(db.getAllUsers()));


        signInBtn.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                db = new DatabaseHelper(context);
                addUserToDB();
            }
        }));

    }

    private void addUserToDB(){
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userName1 = userName.getText().toString();
        String userAdd = userAddress.getText().toString();
        if(userName1.isEmpty()) {
            userName.setError("User name is required");
            userName.requestFocus();
            return;// Stop further processing
        }
        if(userAdd.isEmpty()) {
            userAddress.setError("User name is required");
            userAddress.requestFocus();
            return;// Stop further processing
        }
        if(userEmail.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return; // Stop further processing
        }
        if(userPassword.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;// Stop further processing
        }



        long currentId;
        Log.d("usersFromDB", String.valueOf(db.getAllUsers()));

        try{
            currentId = db.insertUser(userName1, userEmail, userPassword, userAdd);
            if(currentId >=0){
                Log.d("currentId", String.valueOf(currentId));
                Toast.makeText(getApplicationContext(), "createUserWithEmail:success", Toast.LENGTH_SHORT).show();
                User currentUserFromDB = db.getUser(currentId);
                Log.d("currentUserFromDB", " " + currentUserFromDB + "");
                moveToActivity(MainActivity.class);
            }
            else {
                Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
            }

        } catch (SQLiteConstraintException e) {
            Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
            Log.e("SQLiteException", e.getMessage());
            e.printStackTrace();
        }


    }

    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "Data collector");
        startActivity(i);

    }
}
