package com.example.super_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
   // private ImageView userImg;
    private ImageView iconEdit;

    private EditText userEmail, birthDate;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FireBaseHelper fireBaseHelper;
    private boolean isEditing = true;
    private String birthdateField, emailField, birthdateField1, emailField1;
    private List<Order> yourOrders;

    private OrderAdapter orderAdapter;
    private Button backBtn;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView userName = findViewById(R.id.userName);
        fireBaseHelper = new FireBaseHelper(this);


        Context context = getApplicationContext();
        DatabaseHelper dbHelperSQL = new DatabaseHelper(context);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        List<Order> orderSameAddress = new ArrayList<>();
        yourOrders = new ArrayList<>();
        //todo show order history
        orderAdapter = new OrderAdapter(yourOrders);
        getAllUserOrders();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));

        iconEdit = findViewById(R.id.icon);
        userEmail = findViewById(R.id.userEmail);
        birthDate = findViewById(R.id.birthDateEditTxt);
        RecyclerView recOrder = findViewById(R.id.recOrders);
        RecyclerView recOwnOrders = findViewById(R.id.recYourOrders);

        BottomNavigationView menu = findViewById(R.id.menu);
        TextView ownOrderMessage = findViewById(R.id.ownOrderDefualtText);




        userEmail.setEnabled(false);
        userEmail.setFocusable(false);
        userEmail.setFocusableInTouchMode(false);
        birthDate.setEnabled(false);
        birthDate.setFocusable(false);
        birthDate.setFocusableInTouchMode(false);



        //sqlite instance

        recOrder.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recOrder.setLayoutManager(llm);

        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recOwnOrders.setLayoutManager(llm2);

        recOrder.setAdapter(orderAdapter);





        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                        if (doc.getId().equals(mAuth.getUid())) {
                            userName.setText("" + doc.getData().get("userName"));
                            userEmail.setText("" + doc.getData().get("userEmail"));
                            emailField1 = (String) doc.getData().get("userEmail");
                            birthDate.setText("" + doc.get("birthdate"));
                            birthdateField1 = (String) doc.get("birthdate");
                        }
                }
            }
        });
        iconEdit.setOnClickListener(v -> {
            if (isEditing) {
                iconEdit.setImageResource(R.drawable.baseline_save_24);
                isEditing = false;
                userEmail.setEnabled(true);
                userEmail.setFocusable(true);
                userEmail.setFocusableInTouchMode(true);
                userEmail.setTextColor(Color.BLACK);
                birthDate.setEnabled(true);
                birthDate.setFocusable(true);
                birthDate.setFocusableInTouchMode(true);
                birthDate.setTextColor(Color.BLACK);
            } else {
                isEditing = true;
                iconEdit.setImageResource(R.drawable.baseline_mode_edit_24);
                userEmail.setEnabled(false);
                userEmail.setFocusable(false);
                userEmail.setFocusableInTouchMode(false);
                birthDate.setEnabled(false);
                birthDate.setFocusable(false);
                birthDate.setFocusableInTouchMode(false);

                db.collection("users").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getId().equals(mAuth.getUid())) {
                                emailField = userEmail.getText().toString();
                                birthdateField = birthDate.getText().toString();
                                if (emailField.isEmpty() || (!(emailField.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")))) {
                                    userEmail.setText(emailField1);
                                    userEmail.setEnabled(false);
                                    userEmail.setFocusable(false);
                                    userEmail.setFocusableInTouchMode(false);
                                    Toast.makeText(ProfileActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                // Birthdate validation check
                                else if (birthdateField.isEmpty()) {
                                    Toast.makeText(ProfileActivity.this, "birthdate is required", Toast.LENGTH_SHORT).show();
                                    birthDate.setText(birthdateField1);
                                    birthDate.setEnabled(false);
                                    birthDate.setFocusable(false);
                                    birthDate.setFocusableInTouchMode(false);
                                    return;
                                }

                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                dateFormat.setLenient(false);

                                try {
                                    Date birthdate = dateFormat.parse(birthdateField);

                                    Calendar minAgeCalendar = Calendar.getInstance();
                                    minAgeCalendar.add(Calendar.YEAR, -18); // Subtract 18 years from current date

                                    if (birthdate.after(minAgeCalendar.getTime())) {
                                        birthDate.setError("You must be at least 18 years old");
                                        return;
                                    } else {
                                        db.collection("users").document(mAuth.getUid()).update(
                                                "userEmail", userEmail.getText().toString(),
                                                "birthdate", birthDate.getText().toString()
                                        ).addOnSuccessListener(unused -> Toast.makeText(ProfileActivity.this, "User information has changed", Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                                            @SuppressLint("LongLogTag")
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("Error saving user information", e);
                                            }
                                        });
                                    }
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                        }
                    } else {
                        Log.w("Error getting documents.", task.getException());
                    }
                });
            }
        });




    }

    public void getAllUserOrders() {
        FireBaseHelper.UserOrdersFetchListener listener = new FireBaseHelper.UserOrdersFetchListener() {
            @Override
            public void onUserOrdersFetch(List<Order> userOrdersList) {
                yourOrders.clear();
                yourOrders.addAll(userOrdersList);
                orderAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(String errorMessage) {

                System.err.println("Error fetching user's orders: " + errorMessage);
            }
        };

        fireBaseHelper.fetchOrdersOfCurrentUserFromFirebase(listener);
    }

    private void moveToActivity(Class<?> cls) {
        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "msg");
        startActivity(i);
    }









}
