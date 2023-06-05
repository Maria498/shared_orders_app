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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SigninActivity extends AppCompatActivity {

    private Button backBtn;
    private Button signInBtn;
    private EditText userName;
    private EditText userAddress;
    private EditText email;
    private EditText password;
    private Context context;
    private ArrayList<User> usersFromDB;
    private DatabaseHelper db;
    private FirebaseAuth mAuth;
    String userEmail, userPassword, userName1, userAdd;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mAuth = FirebaseAuth.getInstance();
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
                userEmail = email.getText().toString();
                userPassword = password.getText().toString();
                userName1 = userName.getText().toString();
                userAdd = userAddress.getText().toString();
                addUserToDB();
                mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent= new Intent(SigninActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SigninActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }));

    }

    private void addUserToDB() {
        if (userName1.isEmpty()) {
            userName.setError("User name is required");
            userName.requestFocus();
            return;// Stop further processing
        }
        if (userAdd.isEmpty()) {
            userAddress.setError("User name is required");
            userAddress.requestFocus();
            return;// Stop further processing
        }
        if (userEmail.isEmpty()) {
            email.setError("Email is required");
            email.requestFocus();
            return; // Stop further processing
        }
        if (userPassword.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;// Stop further processing
        }


        long currentId;
        Log.d("usersFromDB", String.valueOf(db.getAllUsers()));

        try {
            currentId = db.insertUser(userName1, userEmail, userPassword, userAdd);
            if (currentId >= 0) {
                Log.d("currentId", String.valueOf(currentId));
                Toast.makeText(getApplicationContext(), "createUserWithEmail:success", Toast.LENGTH_SHORT).show();
                User currentUserFromDB = db.getUser(currentId);
                Log.d("currentUserFromDB", " " + currentUserFromDB + "");
                moveToActivity(MainActivity.class);
            } else {
                Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
            }

        } catch (SQLiteConstraintException e) {
            Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
            Log.e("SQLiteException", e.getMessage());
            e.printStackTrace();
        }


    }

    private void moveToActivity(Class<?> cls) {

        Intent i = new Intent(getApplicationContext(), cls);
        i.putExtra("msg", "Data collector");
        startActivity(i);

    }
}
