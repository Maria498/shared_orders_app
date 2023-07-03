package com.example.super_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.super_app.db.CartProductAdapter;
import com.example.super_app.db.entity.Order;
import com.example.super_app.db.entity.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {
    private TextView textViewDeliveryDate;
    private TextView personalPay;
    private TextView cardDetails;
    private TextView expirationDate;
    private EditText fullName;
    private EditText phoneNumber;
    private EditText txtAddAddress;
    private EditText cardNumberTxt;
    private EditText cvv;
    private Button continueBtn;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    double sum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        textViewDeliveryDate = findViewById(R.id.textViewDeliveryDate);
        personalPay = findViewById(R.id.personalPay);
        cardDetails = findViewById(R.id.cardDetails);
        expirationDate = findViewById(R.id.expirationDate);
        fullName = findViewById(R.id.fullName);
        phoneNumber = findViewById(R.id.phoneNumber);
        txtAddAddress = findViewById(R.id.txtAddAddress);
        cardNumberTxt = findViewById(R.id.cardNumberTxt);
        cvv = findViewById(R.id.cvv);
        continueBtn = findViewById(R.id.continueBtn);

        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();

        db.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Order order = doc.toObject(Order.class);
                        if (order.getProductsOfNeigh() != null) {
                            HashMap<String, ArrayList<Product>> productsPerUser = order.getProductsOfNeigh();
                            if (productsPerUser.containsKey(mAuth.getUid())) {
                                for (Product product : productsPerUser.get(mAuth.getUid())) {
                                    sum+=(product.getQuantity())*((product.getPrice())*((100-product.getDiscount())/100));
                                }
                                personalPay.setText("The payment for the purchase is: "+sum);
                                fullName.setText(order.getFullNameOwner());
                                phoneNumber.setText(""+order.getPhoneNumberOwner());
                                txtAddAddress.setText(order.getAddress());
                                textViewDeliveryDate.setText(order.getDeliveryDate());
                            }
                        }
                    }

                }

            }
        });
    }
}