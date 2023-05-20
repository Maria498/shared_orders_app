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
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));


        signInBtn.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                db = new DatabaseHelper(context);
                //db.deleteAllUsers();

                addUserToDB();
            }
        }));

    }

    private void addUserToDB(){
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userName1 = userName.getText().toString();
        User currentUser = new User(userName1, userEmail);
        currentUser.setPassword(userPassword);

        long currentId;
        Log.d("usersFromDB", String.valueOf(db.getAllUsers()));

        try{
            currentId = db.insertUser(userName1, userEmail);
            if(currentId >=0){
                Log.d("currentId", String.valueOf(currentId));
                Toast.makeText(getApplicationContext(), "createUserWithEmail:success", Toast.LENGTH_SHORT).show();
                User currentUserFromDB = db.getUser(currentId);
                Log.d("currentUserFromDB", " " + currentUserFromDB + "");
                signInBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));
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
