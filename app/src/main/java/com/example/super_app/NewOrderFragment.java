package com.example.super_app;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class NewOrderFragment extends Fragment {

    private Button backBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_order, container, false);
        backBtn = view.findViewById(R.id.backBtn);

        backBtn.setOnClickListener(v -> moveToActivity(LoginActivity.class));
        // Inflate the layout for this fragment
        return view;
    }

    private void moveToActivity (Class<?> cls) {

        Intent i = new Intent(getActivity(),  cls);
        startActivity(i);

    }
}