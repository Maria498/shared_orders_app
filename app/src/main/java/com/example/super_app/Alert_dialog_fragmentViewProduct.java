package com.example.super_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.app.DialogFragment;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.super_app.db.entity.Product;

public class Alert_dialog_fragmentViewProduct extends DialogFragment {
    private ImageView imageView;
    private TextView productNameTextView;
    private TextView descriptionTextView;
    private TextView productDescribeTextView;
    private TextView price;
    private ImageButton plusButton;
    private EditText digitEditText;
    private ImageButton minusButton;
    private Button addButton;
    AlertDialogFragmentListener mListener;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // this.activity = activity;
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the MyAlertDialogFragmentListener so we can send events to the host
            mListener = (AlertDialogFragmentListener) activity;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle b = getArguments();
        View v = inflater.inflate(R.layout.activity_alert_dialog_fragment_view_product, null);
        imageView = v.findViewById(R.id.imgPro);
        productNameTextView = v.findViewById(R.id.productNameTextView);
        descriptionTextView = v.findViewById(R.id.descriptionTextView);
        productDescribeTextView = v.findViewById(R.id.productDescribeTextView);
        price = v.findViewById(R.id.price);
        plusButton = v.findViewById(R.id.pluseButton);
        digitEditText = v.findViewById(R.id.digitEditText);
        minusButton = v.findViewById(R.id.minusButton);
        addButton = v.findViewById(R.id.addbtn);
        if (b != null) {
            Product product = (Product) b.getSerializable("Product");
            productNameTextView.setText(product.getName());
            price.setText("" + product.getPrice());
            if (product.getCategory().equals("Electronics") || product.getCategory().equals("MakeUpAndBrush")) {
                productDescribeTextView.setText(product.getDescription());
            } else {
                productDescribeTextView.setVisibility(View.GONE);
                descriptionTextView.setVisibility(View.GONE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Glide.with(getContext())
                        .load(product.getImageResId())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                // Handle image loading failure
                                Log.e("Glide", "Image loading failed: " + e.getMessage());
                                return false; // Return false to allow Glide to handle the error and show any error placeholder you have set
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                // Image successfully loaded
                                return false; // Return false to allow Glide to handle the resource and display it
                            }
                        })
                        .into(imageView);
            }

            plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(digitEditText.getText().toString());
                    if (quantity < 100) {
                        quantity++;
                        digitEditText.setText(quantity);
                        price.setText(""+product.getPrice()*quantity);
                    }
                }
            });
            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(digitEditText.getText().toString());
                    if (quantity > 0) {
                        quantity--;
                        digitEditText.setText(quantity);
                        price.setText(""+product.getPrice()*quantity);
                    }
                }
            });

        }
        return v;

    }

}