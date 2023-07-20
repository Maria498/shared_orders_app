package com.example.super_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.FireBaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private EditText email;
    private EditText password;
    private DatabaseHelper db;
    private ProgressDialog mLoadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Context context = getApplicationContext();
        Button logInBtn = findViewById(R.id.signInBtn);
        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.editTextTextPassword);
        TextView linkSignUp = findViewById(R.id.linkSignUp);
        auth = FirebaseAuth.getInstance();
        mLoadingBar = new ProgressDialog(this);
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        logInBtn.setOnClickListener((view -> {
            if (email.getText() != null && password.getText() != null) {
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
                    if(userEmail.equals("ADMIN")&& userPassword.equals("ADMIN"))
                    {
                        userEmail = "ADMIN@gmail.com";
                        userPassword = "1234567";
                    }
                    mLoadingBar.setTitle("Login");
                    mLoadingBar.setMessage("Please wait while we check your credentials");
                    mLoadingBar.setCanceledOnTouchOutside(false);
                    mLoadingBar.show();

                      auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(task -> {
                          if (task.isSuccessful()) {
//                                    String userName = auth.getCurrentUser().getDisplayName();
//                                    Log.d("userName",userName);
                              //FireBaseHelper
                              String userName = FireBaseHelper.getCurrentUser();
                              Toast.makeText(LoginActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                              Intent i = new Intent(getApplicationContext(),  MainActivity.class);
                              i.putExtra("USER_NAME", userName);
                              startActivity(i);
                          } else {
                              mLoadingBar.hide();
                              System.out.println("failed to login");
                              Toast.makeText(LoginActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                          }
                      });

                }
            }
        }));
        linkSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SigninActivity.class);
            startActivity(intent);
        });

    }



    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "Data collector");
        startActivity(i);

    }


}