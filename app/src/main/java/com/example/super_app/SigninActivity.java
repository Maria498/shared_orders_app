package com.example.super_app;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SigninActivity extends AppCompatActivity {

    private ImageView backBtn;
    private Button signInBtn;
    private EditText userName;

    private EditText email;
    private EditText password;
    private EditText street;
    private EditText apartmentNum;
    private Spinner city;
    private Context context;
    private ArrayList<User> usersFromDB;
    private DatabaseHelper db;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String userEmail, userPassword, userName1, userAdd;
    private User user;
    private HashMap<String, Object> userMap = new HashMap<>();

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
        city = findViewById(R.id.spinnerCity);
        street = findViewById(R.id.Street);
        apartmentNum = findViewById(R.id.ApartmentNum);

        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));

        db = new DatabaseHelper(context);
        firestore = FirebaseFirestore.getInstance();
        Log.d("db.getAllUsers()", String.valueOf(db.getAllUsers()));
        signInBtn.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new DatabaseHelper(context);
                addUserToDB();
                mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    firestore.collection("users").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.d(TAG, "DocumentSnapshot added with ID: " +
                                                    documentReference.getId());
                                            Intent i = new Intent(getApplicationContext(),  MainActivity.class);
                                            String userName =user.getDisplayName();
                                            i.putExtra("USER_NAME", userName);
                                            startActivity(i);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error adding document", e ) ;
                                        }
                                    });

                                } else {
                                    // If sign in fails, display a message to the user.s
                                    Toast.makeText(SigninActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        }));

    }

    private void addUserToDB() {
        userEmail = email.getText().toString();
        userPassword = password.getText().toString();
        userName1 = userName.getText().toString();
        userAdd = city.getSelectedItem().toString() + "," + street.getText().toString() + "," + apartmentNum.getText().toString();

        if (userName1.isEmpty() || userName1.length() < 1 || !userName1.matches("[a-zA-Z]+")) {
            userName.setError("User name is required");
            userName.requestFocus();
            return;// Stop further processing
        }
        if (userEmail.isEmpty() || (!(userEmail.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")))) {
            email.setError("Email is required");
            email.requestFocus();
            return; // Stop further processing
        }
        if (userPassword.isEmpty()) {
            password.setError("Password is required");
            password.requestFocus();
            return;// Stop further processing
        }
        if (city == null || city.getSelectedItem() == null || city.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (street.getText().toString().isEmpty() || apartmentNum.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            user = new User(userName1, userEmail, userPassword, userAdd);
            userMap.put("userName", userName1);
            userMap.put("userEmail", userEmail);
            userMap.put("userPassword", userPassword);
            userMap.put("userAdd", userAdd);
            long currentId;
            Log.d("usersFromDB", String.valueOf(db.getAllUsers()));

            try {
                currentId = db.insertUser(userName1, userEmail, userPassword, userAdd);
                if (currentId >= 0) {
                    Log.d("currentId", String.valueOf(currentId));
                    Toast.makeText(getApplicationContext(), "createUserWithEmail:success", Toast.LENGTH_SHORT).show();
                    User currentUserFromDB = db.getUser(currentId);
                    Log.d("currentUserFromDB", " " + currentUserFromDB + "");
                   // moveToActivity(MainActivity.class);
                } else {
                    Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
                }

            } catch (SQLiteConstraintException e) {
                Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
                Log.e("SQLiteException", e.getMessage());
                e.printStackTrace();
            }
        }

    }

    private void moveToActivity(Class<?> cls) {

        Intent i = new Intent(getApplicationContext(), cls);
        i.putExtra("msg", "Data collector");
        startActivity(i);

    }
}