package com.example.super_app;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.example.super_app.db.entity.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
   // private ImageView userImg;
    private ImageView iconEdit;
    private TextView allOrderMessage;
    private TextView ownOrderMessage;
    private EditText userEmail, birthDate;
    private List<Order> hostList = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isEditing = true;
    private String birthdateField, emailField, birthdateField1, emailField1;
    private List<Order> orderSameAddress;
    private List<Order> yourOrders;

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView userName = findViewById(R.id.userName);
        iconEdit = findViewById(R.id.icon);
        userEmail = findViewById(R.id.userEmail);
        birthDate = findViewById(R.id.birthDateEditTxt);
        RecyclerView recOrder = findViewById(R.id.recOrders);
        RecyclerView recOwnOrders = findViewById(R.id.recYourOrders);
        Button addOrder = findViewById(R.id.btnOpenOrder);
        BottomNavigationView menu = findViewById(R.id.menu);
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

        //sqlite instance
        Context context = getApplicationContext();
        DatabaseHelper dbHelperSQL = new DatabaseHelper(context);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recOrder.setLayoutManager(llm);

        LinearLayoutManager llm2 = new LinearLayoutManager(this);
        llm2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recOwnOrders.setLayoutManager(llm2);

        orderSameAddress = new ArrayList<>();
        yourOrders = new ArrayList<>();

//        menu.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.cart:
//                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
//                    finish(); // Optional: Close the current activity
//                    return true;
//                case R.id.profile:
//                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
//                    finish(); // Optional: Close the current activity
//                    return true;
//                case R.id.search:
//                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
//                    finish(); // Optional: Close the current activity
//                    return true;
//                case R.id.home:
//                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
//                    finish(); // Optional: Close the current activity
//                    return true;
//            }
//            return false;
//        });
        db.collection("Orders").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                orderSameAddress.clear();
                yourOrders.clear();
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
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    //delete orders that delivery date has passed
                    if (orderDateObj.compareTo(currentDateObj) < 0) {
                       //todo fix
                        //dbHelperSQL.deleteOrder(order);

                        db.collection("Orders").document(doc.getId()).delete()
                                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });

                    } else {
                        db.collection("users").document(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (mAuth.getUid().equals(doc.getId())) {
                                        yourOrders.add(order);
                                    } else {
                                        DocumentSnapshot document = task.getResult();
                                        HashMap<String, Object> map = (HashMap<String, Object>) document.getData();
                                        String city = ((String) map.get("userAdd")).split(",")[0];
                                        String street = ((String) map.get("userAdd")).split(",")[1];
                                        if ((order.getAddress().split(",")[0]).equals(city)) {
                                            if ((order.getAddress().split(",")[1]).equals(street)) {
                                                orderSameAddress.add(order);
//                                                if (order.getProductsOfNeigh() != null) {
//                                                    HashMap<String, ArrayList<Product>> list = order.getProductsOfNeigh();
//                                                    if (list.containsKey(mAuth.getUid())) {
//                                                        yourOrders.add(order);
//                                                        if (orderSameAddress.contains(order)) {
//                                                            orderSameAddress.remove(order);
//                                                        }
//
//                                                    }
//                                                }

                                            }
                                        }
                                    }
                                } else {
                                    Log.d("get failed with ", String.valueOf(task.getException()));
                                }

                                if (!orderSameAddress.isEmpty()) {
                                    allOrderMessage.setVisibility(View.GONE);
                                    orderSameAddress.size();

                                } else {
                                    allOrderMessage.setVisibility(View.VISIBLE);
                                }

                                if (!yourOrders.isEmpty()) {
                                    ownOrderMessage.setVisibility(View.GONE);

                                } else {
                                    ownOrderMessage.setVisibility(View.VISIBLE);
                                }
                            }

                        });

                    }
                }


            }
        });


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

        addOrder.setOnClickListener(v -> {
            if (yourOrders.isEmpty()) {
                Intent i = new Intent(getApplicationContext(), CreateNewOrderActivity.class);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), "You already have an open order", Toast.LENGTH_SHORT).show();
            }
        });


    }


//    public void showSimpleAlertDialogDeleteOrder(int position) {
//        Order order = yourOrders.get(position);
//        // 1. Instantiate an AlertDialog.Builder with its constructor
//        AlertDialog.Builder builder = null;
//        builder = new AlertDialog.Builder(ProfileActivity.this);
//
//
//        // 2. Chain together various setter methods to set the dialog characteristics
//        builder.setMessage(R.string.getoutOrder);
//        builder.setTitle(R.string.dialog_title);
//
//        // Add the buttons
//        builder.setPositiveButton(R.string.yes, (dialog, id) -> db.collection("Orders").get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                for (QueryDocumentSnapshot doc : task.getResult()) {
//                    Order currentOrder = doc.toObject(Order.class);
//                    if (currentOrder.equals(order)) {
//                        if (order.getProductsOfNeigh() != null) {
//                            HashMap<String, ArrayList<Product>> neigh = order.getProductsOfNeigh();
//
//                            if (neigh.containsKey(mAuth.getUid())) {
//
//                                neigh.remove(mAuth.getUid());
//                                db.collection("Orders").document(doc.getId()).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Toast.makeText(ProfileActivity.this, "unused func", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                });
//                            }
//                        } else if (doc.getId().equals(mAuth.getUid())) {
//                            order.setFullNameOwner("nobody");
//                            db.collection("Orders").document(doc.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    db.collection("Orders").add(order).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                        @Override
//                                        public void onSuccess(DocumentReference documentReference) {
//                                            Toast.makeText(ProfileActivity.this, "unused func", Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    });
//                                }
//                            });
//                        }
//                    }
//
//                }
//
//            }
//        }));
//        builder.setNegativeButton(R.string.no, (dialog, id) -> {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                Toast.makeText(getApplicationContext(), "Users cancelled the dialog", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // 3. Get the AlertDialog from create()
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }




//    private void showSimpleAlertDialogJoin(int position) {
//        Order order = orderSameAddress.get(position);
//        // 1. Instantiate an AlertDialog.Builder with its constructor
//        AlertDialog.Builder builder = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            builder = new AlertDialog.Builder(ProfileActivity.this);
//        }
//
//        // 2. Chain together various setter methods to set the dialog characteristics
//        builder.setMessage(R.string.joinOrder);
//        builder.setTitle(R.string.dialog_title);
//
//        // Add the buttons
//        builder.setPositiveButton(R.string.yes, (dialog, id) -> {
//            if(!yourOrders.isEmpty())
//            {
//                Toast.makeText(getApplicationContext(), "You allready have an open order", Toast.LENGTH_SHORT).show();
//            }else {
//                db.collection("Orders").get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        for (QueryDocumentSnapshot doc : task.getResult()) {
//                            Order currentOrder = doc.toObject(Order.class);
//                            if (currentOrder.equals(order)) {
//                                HashMap<String, ArrayList<Product>> participants;
//                                if (order.getProductsOfNeigh() == null) {
//                                    participants = new HashMap<>();
//                                } else {
//                                    participants = order.getProductsOfNeigh();
//                                }
//                                participants.put(mAuth.getUid(), new ArrayList<>());
//                                db.collection("Orders").document(doc.getId()).update("productsOfNeigh", participants).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                    orderSameAddress.remove(order);
//                                                    //todo fix
//                                                    yourOrders.add(order);
//
//
//                                                    Toast.makeText(ProfileActivity.this, "unused func", Toast.LENGTH_SHORT).show();
//
//                                                }
//
//                                            }
//                                        })
//                                        .addOnFailureListener(e -> {
//                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                                                Toast.makeText(getApplicationContext(), "There is a problem", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                            }
//                        }
//
//
//                    }
//                });
//            }
//        });
//        builder.setNegativeButton(R.string.no, (dialog, id) -> {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                Toast.makeText(getApplicationContext(), "Users cancelled the dialog", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // 3. Get the AlertDialog from create()
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }


}
