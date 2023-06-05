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
import android.widget.Toast;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private Button backBtn;
    private Button signInBtn;
    private Button logInBtn;
    private EditText email;
    private EditText password;
    private DatabaseHelper db;
    private Context context;
    private ArrayList<User> usersFromDB;
    private FirebaseAuth mAuth;
   private String userEmail, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        context = getApplicationContext();

        signInBtn = findViewById(R.id.signInBtn);
        backBtn = findViewById(R.id.backBtn);
        logInBtn = findViewById(R.id.logInBtn);
        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.editTextTextPassword3);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));

        db = new DatabaseHelper(context);
        //db.deleteAllUsers();
        Log.d("db.getAllUsers()", String.valueOf(db.getAllUsers()));
        userEmail = email.getText().toString();
        userPassword = password.getText().toString();

        signInBtn.setOnClickListener(v -> moveToActivity(SigninActivity.class));

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new DatabaseHelper(context);
                //db.deleteAllUsers();
                loginUser();
                mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void loginUser() {
        //todo shared references for loin button.
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

        long userId = db.getUserIdByEmail(userEmail);
        if (userId > -1) {
            String passwordByEmail = db.getPasswordByEmail(userEmail);
            if (passwordByEmail.equals(userPassword)) {
                Toast.makeText(getApplicationContext(), "login:success", Toast.LENGTH_SHORT).show();
                Log.d("login:success", "login:success");
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                String userName = db.getUserNameById((int) userId);
                i.putExtra("USER_NAME", userName);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "login:failed - passwords do not match", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "login:failed - user not found, please sign in", Toast.LENGTH_SHORT).show();
            moveToActivity(SigninActivity.class);
        }
    }

    private void moveToActivity(Class<?> cls) {
        Intent i = new Intent(getApplicationContext(), cls);
        i.putExtra("msg", "Data collector");
        startActivity(i);
    }
}
