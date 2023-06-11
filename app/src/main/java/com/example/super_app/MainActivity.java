package com.example.super_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.super_app.db.DatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{

//    BottomNavigationView bottomNavigationView;
//    HomeFragment home = new HomeFragment();
//    NewOrderFragment newOrder = new NewOrderFragment();
//    UserFragment userFragment = new UserFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);

//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setOnItemSelectedListener(this);
//        bottomNavigationView.setSelectedItemId(R.id.home);
//
//        if(bottomNavigationView.getSelectedItemId() == R.id.home) {
//            // System.out.println("In Home: ");
//        }



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
//            case R.id.new_order:
//                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, newOrder).commit();
//                return true;
//            case R.id.home:
//                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, home).commit();
//                return true;
//            case R.id.account:
//                getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, userFragment).commit();
//                return true;

        }

        return false;
    }

    private void updateRecycled (Class<?> cls) {}

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String userName = intent.getStringExtra("USER_NAME");
        if(userName != null){
            Log.d("i am onResume()", "i am onResume()");
            DisplayAndSaveUserName(userName);

        }
    }
    @SuppressLint("LongLogTag")
    private void DisplayAndSaveUserName(String userName) {
        //Display the text
//        logInBtn.setText("Hey, "+userName);

        //-------store data--------
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        //write data to shared perf
        SharedPreferences.Editor editor =sharedPref.edit();
        editor.putString("userName","Hey, "+ userName);
        editor.commit();
        Log.d("i am DisplayAndSaveUserName(String userName)", "i am DisplayAndSaveUserName(String userName)");
        //-------store data--------
        //String storedUserName = sharedPref.getString(getString(R.string.log_in), null);
    }

    private void DisplaySavedText() {
        //Reading values from shared preference
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String defaultText = sharedPref.getString("userName", "Log In");
        //logInBtn.setEnabled(true);
//        logInBtn.setText(defaultText);
    }


}