package com.example.super_app;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.super_app.R;
import com.example.super_app.RecycleViewInterface;
import com.example.super_app.db.OrderAdapter;
import com.example.super_app.db.entity.Order;
import com.example.super_app.db.entity.Product;
import com.example.super_app.db.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    private ImageView userImg;
    private ImageView iconEdit;
    private TextView userName,allOrderMessage,ownOrderMessage;
    private EditText userEmail, birthDate;
    private RecyclerView recOrder;
    private RecyclerView recOwnOrders;
    private BottomNavigationView menu;
    private List<Order> hostList = new ArrayList<>();
    private Button addOrder;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isEditing = true;
    private String birthdateField, emailField, birthdateField1, emailField1;
    private List<Order> orderSameAddress;
    private List<Order> ownOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userName = findViewById(R.id.userName);
        iconEdit = findViewById(R.id.icon);
        userEmail = findViewById(R.id.userEmail);
        birthDate = findViewById(R.id.birthDateEditTxt);
        recOrder = findViewById(R.id.recOrders);
        recOwnOrders = findViewById(R.id.recYourOrders);
        addOrder = findViewById(R.id.btnOpenOrder);
        menu = findViewById(R.id.menu);
        ownOrderMessage = findViewById(R.id.ownOrderDefualtText);
        allOrderMessage = findViewById(R.id.allOrderDefualtText);

        userEmail.setEnabled(false);
        userEmail.setFocusable(false);
        userEmail.setFocusableInTouchMode(false);
        birthDate.setEnabled(false);
        birthDate.setFocusable(false);
        birthDate.setFocusableInTouchMode(false);

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recOrder.setLayoutManager(llm);

        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recOwnOrders.setLayoutManager(llm2);

        orderSameAddress = new ArrayList<>();
        ownOrder = new ArrayList<>();
        db.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Order order = doc.toObject(Order.class);
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String currentDate = dateFormat.format(calendar.getTime());
                        String orderDate = order.getDeliveryDate(); // Assuming this is the order date string
                        Date currentDateObj = null;
                        Date orderDateObj = null;
                        try {
                            currentDateObj = dateFormat.parse(currentDate);
                            orderDateObj = dateFormat.parse(orderDate);
                        }
                        catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        if (orderDateObj.compareTo(currentDateObj) < 0) {
                            db.collection("Orders").document(doc.getId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });

                        } else {
                            if (doc.getId().equals(uid)) {
                                //the current user is the owner of this order
                                ownOrder.add(order);
                            } else {
                                db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
                                            String address = ((String) map.get("userAdd")).split(",")[0];
                                            if ((order.getAddress().split(",")[0]).equals(address)) {
                                                orderSameAddress.add(order);
                                            }
                                            for(Order order: ownOrder)
                                            {
                                                if(orderSameAddress.contains(order))
                                                {
                                                    orderSameAddress.remove(order);
                                                }
                                            }
                                            if(!orderSameAddress.isEmpty())
                                            {
                                                allOrderMessage.setVisibility(View.GONE);
                                            }

                                                OrderAdapter orderAdapter = new OrderAdapter(orderSameAddress, ProfileActivity.this);
                                                recOrder.setAdapter(orderAdapter);

                                        } else {
                                            Log.d("get failed with ", String.valueOf(task.getException()));
                                        }
                                    }
                                });
                            }
                        }
                    }



                    if(!ownOrder.isEmpty())
                    {
                        ownOrderMessage.setVisibility(View.GONE);
                    }

                        OrderAdapter ownerOrderAdapter = new OrderAdapter(ownOrder, ProfileActivity.this);
                        recOwnOrders.setAdapter(ownerOrderAdapter);


                }
            }
        });


        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        if (doc.getId().equals(uid)) {
                            userName.setText("" + doc.getData().get("userName"));
                            userEmail.setText("" + doc.getData().get("userEmail"));
                            emailField1 = (String) doc.getData().get("userEmail");
                            birthDate.setText("" + doc.get("birthdate"));
                            birthdateField1 = (String) doc.get("birthdate");
                        }
                    }
                }
            }
        });
        iconEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                    db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
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
                                                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Toast.makeText(ProfileActivity.this, "User information has changed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
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
                        }
                    });
                }
            }
        });

//        menu.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.cart: {
//                    startActivity(new Intent(getApplicationContext(), .class));
//                    overridePendingTransition(0, 0);
//                    break;
//                }
//                case R.id.profile: {
//                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//                    overridePendingTransition(0, 0);
//                    break;
//                }
//                case R.id.search: {
//                    startActivity(new Intent(getApplicationContext(), .class));
//                    overridePendingTransition(0, 0);
//                    break;
//                }
//                case R.id.home: {
//                    startActivity(new Intent(getApplicationContext(), .class));
//                    overridePendingTransition(0, 0);
//                    break;
//                }
//            }
//            return true;
//        });

        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ownOrder.isEmpty())
                {
                    Intent i = new Intent(getApplicationContext(), SharedOrderActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"You allready have an open order",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
