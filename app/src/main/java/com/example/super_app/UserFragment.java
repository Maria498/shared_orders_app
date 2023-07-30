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
    TextView textCoinBag;
    TextView coinBagValue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        TextView textViewWelcome = view.findViewById(R.id.textViewWelcome);
        textCoinBag = view.findViewById(R.id.textCoinBag);
        textCoinBag.setVisibility(View.INVISIBLE);
        coinBagValue = view.findViewById(R.id.coinBagValue);
        coinBagValue.setVisibility(View.INVISIBLE);
        Button buttonOrderHistory = view.findViewById(R.id.buttonOrderHistory);
        Button buttonUpdateUserData = view.findViewById(R.id.buttonUpdateUserData);
        Button coin_bag = view.findViewById(R.id.coin_bag);
        buttonOrderHistory.setOnClickListener(v -> moveToActivity(OrderHistoryActivity.class));
        buttonUpdateUserData.setOnClickListener(v -> moveToActivity(ProfileActivity.class));
        Button backBtn = view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));
        coin_bag.setOnClickListener(v -> displaySavings());


        return view;
    }



    private void moveToActivity (Class<?> cls) {
        Intent i = new Intent(getActivity(),  cls);
        i.putExtra("msg", "msg");
        startActivity(i);
    }

    private void displaySavings () {
        textCoinBag.setVisibility(View.VISIBLE);


        coinBagValue.setVisibility(View.VISIBLE);
    }
}