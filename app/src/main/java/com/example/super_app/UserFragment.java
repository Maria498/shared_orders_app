package com.example.super_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


public class UserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        TextView textViewWelcome = view.findViewById(R.id.textViewWelcome);
        Button buttonOrderHistory = view.findViewById(R.id.buttonOrderHistory);
        Button buttonUpdateUserData = view.findViewById(R.id.buttonUpdateUserData);
        //todo - change navigation
        buttonOrderHistory.setOnClickListener(v -> moveToActivity(OrderHistoryActivity.class));
        buttonUpdateUserData.setOnClickListener(v -> moveToActivity(ProfileActivity.class));
        Button backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));

//        buttonOrderHistory.setOnClickListener(v -> {
//            // Handle button click for order history
//            // Add your code here
//        });
//        buttonUpdateUserData.setOnClickListener(v -> moveToActivity(ProfileActivity.class));
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