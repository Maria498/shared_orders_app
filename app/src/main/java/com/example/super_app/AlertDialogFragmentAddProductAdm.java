package com.example.super_app;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.super_app.db.entity.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.HashMap;

public class AlertDialogFragmentAddProductAdm extends DialogFragment {
    private ImageView imageView;
    private EditText productName;
    private EditText productPrice;
    private Spinner spinnerCategory;
    private EditText productDescribeTextView;
    private Button addButton;
    private ImageButton uploadImg;
    String URL;
    Uri imageURIProduct;
    int PICK_IMAGE_REQUEST = 100;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_alert_add_product_admin_dialog, container, false);

        imageView = view.findViewById(R.id.imgPro);
        productName = view.findViewById(R.id.EditTextProductName);
        productPrice = view.findViewById(R.id.EditTextPrice);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        productDescribeTextView = view.findViewById(R.id.productDescribeEditText);
        addButton = view.findViewById(R.id.addbtn);
        uploadImg = view.findViewById(R.id.uploadImg);
        productDescribeTextView.setVisibility(View.GONE);
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerCategory.getSelectedItem().toString().equals("MakeUpAndBrush") || spinnerCategory.getSelectedItem().toString().equals("Electronics")) {
                    productDescribeTextView.setVisibility(View.VISIBLE);
                } else {
                    productDescribeTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productName.getText().toString().isEmpty() || productPrice.getText().toString().isEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Toast.makeText(getContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    }
                    return;
                } else if (imageView.getDrawable() == null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Toast.makeText(getContext(), "You must upload an image!", Toast.LENGTH_SHORT).show();
                    }
                    return;
                } else if (spinnerCategory.getSelectedItem().toString().equals("Category")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Toast.makeText(getContext(), "You must choose a category!", Toast.LENGTH_SHORT).show();
                    }
                    return;
                } else {
                    // Upload the image to Firebase Storage
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference(imageURIProduct.toString());
                    storageRef.putFile(imageURIProduct).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        URL = uri.toString();
                                        double price = Double.parseDouble(productPrice.getText().toString());
                                        Product product = new Product(productName.getText().toString(), price, URL, spinnerCategory.getSelectedItem().toString());
                                        HashMap<String, Object> productData = new HashMap<>();
                                        productData.put("name", product.getName());
                                        productData.put("price", product.getPrice());
                                        productData.put("img", product.getimg());
                                        productData.put("category", product.getCategory());
                                        db.collection(product.getCategory()).add(productData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    Toast.makeText(getContext(), "New product added to company", Toast.LENGTH_SHORT).show();
                                                }
                                                dismiss();
                                            }
                                        });
                                    }
                                });
                            } else {
                                // Handle unsuccessful image upload
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageURIProduct = data.getData();
            imageView.setImageURI(imageURIProduct);
        }
    }
}
