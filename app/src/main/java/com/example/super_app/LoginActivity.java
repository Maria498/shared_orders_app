package com.example.super_app;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.entity.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private Button backBtn;

    private FirebaseAuth auth;
    private Button signInBtn;
    private Button logInBtn;
    private TextView linkSignUp;
    private EditText email;
    private EditText password;
    private DatabaseHelper db;
    private Context context;
    private ArrayList<User> usersFromDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        signInBtn = findViewById(R.id.signInBtn);
        logInBtn = findViewById(R.id.signInBtn);
        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.editTextTextPassword);
        linkSignUp = findViewById(R.id.linkSignUp);
        auth = FirebaseAuth.getInstance();
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));


        db = new DatabaseHelper(context);
        //db.deleteAllUsers();
        Log.d("db.getAllUsers()", String.valueOf(db.getAllUsers()));
        signInBtn.setOnClickListener(v -> moveToActivity(SigninActivity.class));

        logInBtn.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                db = new DatabaseHelper(context);
                String userEmail = email.getText().toString();
                String userPassword =password.getText().toString();
                //field validation
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
                auth.signInWithEmailAndPassword(userEmail,userPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent i = new Intent(getApplicationContext(),  MainActivity.class);
                        String userName =auth.getCurrentUser().getDisplayName();
                        i.putExtra("USER_NAME", userName);
                        startActivity(i);
                    }
                });
                Toast.makeText(LoginActivity.this,"something went wrong",Toast.LENGTH_SHORT).show();


            }
        }));
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });

    }

    public void loginUser(){
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        //todo shared references for loin button.
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


        long userId = db.getUserIdByEmail(userEmail);
        if(userId > -1){
            String passwordByEmail = db.getPasswordByEmail(userEmail);
            if(passwordByEmail.equals(userPassword)){
                Toast.makeText(getApplicationContext(), "login:success", Toast.LENGTH_SHORT).show();
                Log.d("login:success","login:success");
                Intent i = new Intent(getApplicationContext(),  MainActivity.class);
                String userName =db.getUserNameById((int) userId);
                i.putExtra("USER_NAME", userName);
                startActivity(i);

                //moveToActivity(MainActivity.class);
            }
            else {
                Toast.makeText(getApplicationContext(), "login:failed - passwords do not match", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "login:failed - user not fount, please sig in", Toast.LENGTH_SHORT).show();
            moveToActivity(SigninActivity.class);
        }






    }

    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "Data collector");
        startActivity(i);

    }


}