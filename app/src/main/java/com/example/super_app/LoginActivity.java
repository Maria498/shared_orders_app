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
import com.example.super_app.db.entity.Product;
import com.example.super_app.db.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
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
        signInBtn.setOnClickListener(v -> moveToActivity(SigninActivity.class));


        logInBtn.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View view) {
               db = new DatabaseHelper(context);
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                //field validation
                if (userEmail.isEmpty()) {
                    email.setError("Email is required");
                    email.requestFocus();
                    return; // Stop further processing
                } else if (userPassword.isEmpty()) {
                    password.setError("Password is required");
                    password.requestFocus();
                    return;// Stop further processing
                } else {
                    auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>()  {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Intent i = new Intent(getApplicationContext(), SuperCategoryActivity.class);
                                startActivity(i);
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
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



    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "Data collector");
        startActivity(i);

    }


}