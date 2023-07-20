package com.example.super_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;


public class OrderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        Button backBtn = view.findViewById(R.id.backBtn);
        Button buttonUpdateOrder = view.findViewById(R.id.buttonUpdateOrder);
        Button buttonCreateOrder = view.findViewById(R.id.buttonCreateOrder);

        backBtn.setOnClickListener(v -> moveToActivity(LoginActivity.class));
        buttonCreateOrder.setOnClickListener(v -> moveToActivity(CreateNewOrderActivity.class));
        RecyclerView recyclerViewOrders = view.findViewById(R.id.recyclerViewOrders);
        ArrayList<MenuModel> cardsListOrders = new ArrayList<>();
        cardsListOrders.add(new MenuModel("Order1", "Total: 800", R.drawable.bag));
        cardsListOrders.add(new MenuModel("Order2", "Total: 559", R.drawable.bag));
        MenuCardsAdapter adapter = new MenuCardsAdapter(this.getContext(), cardsListOrders);
        RecyclerView.LayoutManager layoutManagerOrder = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewOrders.setLayoutManager(layoutManagerOrder);
        recyclerViewOrders.setAdapter(adapter);
        return view;
    }

    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getActivity(),  cls);
        startActivity(i);

    }
}