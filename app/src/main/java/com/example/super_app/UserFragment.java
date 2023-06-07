package com.example.super_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class UserFragment extends Fragment {

    private TextView textViewWelcome;
    private Button buttonOrderHistory;
    private Button buttonUpdateUserData;
    private Button backBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        textViewWelcome = view.findViewById(R.id.textViewWelcome);
        buttonOrderHistory = view.findViewById(R.id.buttonOrderHistory);
        buttonUpdateUserData = view.findViewById(R.id.buttonUpdateUserData);
        //todo - change navigation
        buttonOrderHistory.setOnClickListener(v -> moveToActivity(MainActivity.class));
        buttonUpdateUserData.setOnClickListener(v -> moveToActivity(MainActivity.class));
        backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));

        buttonOrderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click for order history
                // Add your code here
            }
        });

        DisplaySavedText();

        return view;
    }


    private void DisplaySavedText() {
//        //Reading values from shared preference
//        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//        String defaultText = sharedPref.getString("userName", "Log In");
//        //logInBtn.setEnabled(true);
//        textViewWelcome.setText(defaultText);
    }

    private void moveToActivity (Class<?> cls) {
        Intent i = new Intent(getActivity(),  cls);
        i.putExtra("msg", "msg");
        startActivity(i);
    }
}