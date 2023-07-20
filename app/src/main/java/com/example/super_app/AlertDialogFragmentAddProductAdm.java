package com.example.super_app;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.super_app.db.entity.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AlertDialogFragmentAddProductAdm extends DialogFragment {
    private ImageView imageView;
    private EditText productName;
    private EditText productPrice;
    private Spinner spinnerCategory;
    private EditText productDescribeTextView, salePercentageEditText;
    private Button addButton;
    private ImageButton uploadImg;
    String URL;
    Uri imageURIProduct;
    int PICK_IMAGE_REQUEST = 100;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean isEditMode = false;
    Product editProduct;
    String description = "There is no description for this product";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_alert_add_product_admin_dialog, container, false);
        Bundle b = getArguments();
        imageView = view.findViewById(R.id.imgPro);
        productName = view.findViewById(R.id.EditTextProductName);
        productPrice = view.findViewById(R.id.EditTextPrice);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        productDescribeTextView = view.findViewById(R.id.productDescribeEditText);
        addButton = view.findViewById(R.id.addbtn);
        uploadImg = view.findViewById(R.id.uploadImg);
        productDescribeTextView.setVisibility(View.GONE);
        salePercentageEditText = view.findViewById(R.id.salePercentageEditText);
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
        if (b != null) {
            isEditMode = true;
            uploadImg.setVisibility(View.GONE);
            addButton.setText("Edit");
            editProduct = (Product) b.getSerializable("Product");

            productPrice.setText("Price:" + editProduct.getPrice());
            productPrice.setEnabled(false);
            productPrice.setFocusable(false);
            productPrice.setFocusableInTouchMode(false);

            productName.setText(editProduct.getName());
            productName.setEnabled(false);
            productName.setFocusable(false);
            productName.setFocusableInTouchMode(false);
            for (int i = 0; i < spinnerCategory.getCount(); i++) {
                if (spinnerCategory.getItemAtPosition(i).equals(editProduct.getCategory())) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }
            spinnerCategory.setEnabled(false);
            productDescribeTextView.setEnabled(false);
            productDescribeTextView.setFocusable(false);
            productDescribeTextView.setFocusableInTouchMode(false);
            if (editProduct.getDescription() != null) {
                description = editProduct.getDescription();
            }

            productDescribeTextView.setText(description);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Glide.with(getContext())
                        .load(editProduct.getImg())
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
            salePercentageEditText.setEnabled(false);
            salePercentageEditText.setFocusable(false);
            salePercentageEditText.setFocusableInTouchMode(false);
            if (editProduct.getDiscount() != 0) {
                salePercentageEditText.setText("Discount: "+editProduct.getDiscount()+"%");

            }
        } else {
            isEditMode = false;
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode) {
                    productName.setEnabled(true);
                    productName.setFocusable(true);
                    productName.setFocusableInTouchMode(true);
                    productPrice.setEnabled(true);
                    productPrice.setFocusable(true);
                    productPrice.setFocusableInTouchMode(true);
                    productDescribeTextView.setEnabled(true);
                    productDescribeTextView.setFocusable(true);
                    productDescribeTextView.setFocusableInTouchMode(true);
                    addButton.setText("SAVE");
                    uploadImg.setVisibility(View.VISIBLE);
                    salePercentageEditText.setEnabled(true);
                    salePercentageEditText.setFocusable(true);
                    salePercentageEditText.setFocusableInTouchMode(true);
                    isEditMode = false;
                } else {
                    productName.setEnabled(false);
                    productName.setFocusable(false);
                    productName.setFocusableInTouchMode(false);
                    productPrice.setEnabled(false);
                    productPrice.setFocusable(false);
                    productPrice.setFocusableInTouchMode(false);
                    productDescribeTextView.setEnabled(false);
                    productDescribeTextView.setFocusable(false);
                    productDescribeTextView.setFocusableInTouchMode(false);
                    salePercentageEditText.setEnabled(false);
                    salePercentageEditText.setFocusable(false);
                    salePercentageEditText.setFocusableInTouchMode(false);
                    addButton.setText("EDIT");
                    uploadImg.setVisibility(View.GONE);
                    isEditMode = true;
                }
                if (productName.getText().toString().isEmpty() || productPrice.getText().toString().isEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Toast.makeText(getContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }  if (imageView.getDrawable() == null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Toast.makeText(getContext(), "You must upload an image!", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }  if (spinnerCategory.getSelectedItem().toString().equals("Category")) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Toast.makeText(getContext(), "You must choose a category!", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }   if (salePercentageEditText.getText().toString() != null && !salePercentageEditText.getText().toString().isEmpty() && ((Integer.parseInt((salePercentageEditText.getText().toString())) > 100))) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Toast.makeText(getContext(), "discount must be smaller than 100", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    if (productDescribeTextView.getVisibility() == View.VISIBLE) {
                        description = productDescribeTextView.getText().toString();
                    }
                    if (imageURIProduct == null) {
                        if (isEditMode) {

                            db.collection(editProduct.getCategory()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot doc : task.getResult()) {
                                            Product newProduct = doc.toObject(Product.class);
                                            if (newProduct.equals(editProduct)) {
                                                db.collection(editProduct.getCategory()).document(doc.getId()).update(
                                                        "name", productName.getText().toString(),
                                                        "price", Double.parseDouble(productPrice.getText().toString()),
                                                        "category", spinnerCategory.getSelectedItem().toString(),
                                                        "description", description,
                                                        "discount", Integer.parseInt(salePercentageEditText.getText().toString())
                                                ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            Toast.makeText(getContext(), "Product information has changed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @SuppressLint("LongLogTag")
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("Error saving product information", e);
                                                    }
                                                });
                                            }
                                        }
                                    }
                                }
                            });
                        }
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
                                            if (uri != null) {
                                                URL = uri.toString();
                                            }
                                            double price = Double.parseDouble(productPrice.getText().toString());
                                            Product product = new Product(productName.getText().toString(), price, URL, spinnerCategory.getSelectedItem().toString());
                                            HashMap<String, Object> productData = new HashMap<>();
                                            productData.put("name", product.getName());
                                            productData.put("price", product.getPrice());
                                            productData.put("img", product.getImg());
                                            productData.put("category", product.getCategory());
                                            productData.put("description", description);
                                            productData.put("discount", product.getDiscount());

                                            if (isEditMode) {
                                                db.collection(editProduct.getCategory()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                                                Product newProduct = doc.toObject(Product.class);
                                                                if (newProduct.equals(editProduct)) {
                                                                    db.collection(editProduct.getCategory()).document(doc.getId()).update(
                                                                            "name", product.getName(),
                                                                            "price", product.getPrice(),
                                                                            "img", product.getImg(),
                                                                            "category", product.getCategory(),
                                                                            "description", description,
                                                                            "discount", salePercentageEditText.getText().toString()
                                                                    ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                                                Toast.makeText(getContext(), "Product information has changed", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @SuppressLint("LongLogTag")
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.w("Error saving product information", e);
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }
                                                    }
                                                });
                                            } else {

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
