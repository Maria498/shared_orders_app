package com.example.solmatchfinalproject.profile;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.super_app.db.entity.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity   {
    ImageView userImg;
    TextView userName;
    EditText userEmail, birthDate;
    RecyclerView recOrder;
    BottomNavigationView menu;
    List<Order> hostList = new ArrayList<>();
    Button addOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        birthDate = findViewById(R.id.birthDateEditTxt);
        recOrder = findViewById(R.id.recOrders);
        addOrder=findViewById(R.id.btnOpenOrder);
        menu = findViewById(R.id.menu);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recOrder.setLayoutManager(llm);
//        menu.setOnItemReselectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.cart: {
//                    startActivity(new Intent(getApplicationContext(), .class));
//                    overridePendingTransition(0, 0);
//                    break;
//                }
//                case R.id.profile: {
//                    startActivity(new Intent(getApplicationContext(), .class));
//                    overridePendingTransition(0, 0);
//                    break;
//                }
//                case R.id.Search: {
//                    startActivity(new Intent(getApplicationContext(), .class));
//                    overridePendingTransition(0, 0);
//                    break;
//                }
//                case R.id.Home: {
//                    startActivity(new Intent(getApplicationContext(), .class));
//                    overridePendingTransition(0, 0);
//                    break;
//                }
//            }
//        });

        addOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }
}
