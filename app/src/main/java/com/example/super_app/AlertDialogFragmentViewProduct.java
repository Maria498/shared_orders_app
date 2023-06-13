package com.example.super_app;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.super_app.db.entity.Product;

import org.w3c.dom.Text;

public class AlertDialogFragmentViewProduct extends DialogFragment {
    private ImageView imageView;
    private TextView productNameTextView;
    private TextView descriptionTextView;
    private TextView productDescribeTextView;
    private TextView price;
    private ImageButton plusButton;
    private TextView quantityTextView;
    private ImageButton minusButton;
    private Button addButton;
    AlertDialogFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AlertDialogFragmentListener) activity;
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle b = getArguments();
        View v = inflater.inflate(R.layout.activity_alert_dialog_fragment_view_product,null);
        imageView = v.findViewById(R.id.imgPro);
        productNameTextView = v.findViewById(R.id.productNameTextView);
        descriptionTextView = v.findViewById(R.id.descriptionTextView);
        productDescribeTextView = v.findViewById(R.id.productDescribeTextView);
        price = v.findViewById(R.id.price);
        plusButton = v.findViewById(R.id.pluseButton);
        quantityTextView = v.findViewById(R.id.digitEditText);
        minusButton = v.findViewById(R.id.minusButton);
        addButton = v.findViewById(R.id.addbtn);

        if (b != null) {
            Product product = (Product) b.getSerializable("Product");
            productNameTextView.setText(product.getName());
            price.setText(String.valueOf(product.getPrice()));

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
                                if (e != null) {
                                    Log.e("Glide", "Image loading failed: " + e.getMessage());
                                }
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
                    int quantity = Integer.parseInt(quantityTextView.getText().toString());
                    if (quantity < 100) {
                        quantity++;
                        quantityTextView.setText(String.valueOf(quantity));
                        price.setText(String.valueOf(product.getPrice() * quantity));
                    }
                }
            });

            minusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(quantityTextView.getText().toString());
                    if (quantity > 0) {
                        quantity--;
                        quantityTextView.setText(String.valueOf(quantity));
                        price.setText(String.valueOf(product.getPrice() * quantity));
                    }
                }
            });
        }

        return v;
    }
}
