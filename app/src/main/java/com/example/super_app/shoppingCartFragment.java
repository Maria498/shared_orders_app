package com.example.super_app;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class shoppingCartFragment extends Fragment {

    private ArrayList<ProductModel> itemList;
    private ProductAdapter adapter;

    // ...

    public static shoppingCartFragment newInstance(ArrayList<ProductModel> itemList) {
        shoppingCartFragment fragment = new shoppingCartFragment();
        Bundle args = new Bundle();
        return fragment;
    }
    public void setItemList(ArrayList<ProductModel> itemList) {
        this.itemList = itemList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shopping_cart_fragment, container, false);
        RecyclerView listView = view.findViewById(R.id.shoppingCartList);
        adapter = new ProductAdapter(requireContext(), itemList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(adapter);

        return view;
    }
}
