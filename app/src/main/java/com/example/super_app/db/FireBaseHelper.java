package com.example.super_app.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.super_app.db.entity.Cart;
import com.example.super_app.db.entity.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FireBaseHelper {
    private final DatabaseReference mDatabase;
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private static  Cart cart;

    private Context context;

    public FireBaseHelper() {

        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        initializeCart();
    }

    public static String getCurrentUser()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getDisplayName();
        }
        return null;
    }

    public static Cart getCart() {
        return cart;
    }

     public void initializeCart() {
        if (cart == null) {
            cart = new Cart("my_first_cart", Calendar.getInstance().getTime(), 0, 0);
        }
    }


    public void updateCartQuantity(Product product, int newQuantity) {
        if (cart != null) {
            cart.getProductsQuantity().put(product, newQuantity);
        }
    }


    public static void logOutUser(){
        mAuth.signOut();
    }

    public FireBaseHelper(Context context) {
        this.context = context;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void handleProductToFirestore(String name, String category, double price, int discount,
                                         boolean healthy_tag, String imageUrl, String selectedImageName) {
        Product product = new Product(name, price, imageUrl, category, discount, healthy_tag);
        if (imageUrl.startsWith("drawable://")) {
            // Extract the resource ID from the URL
            int resourceId = Integer.parseInt(imageUrl.replace("drawable://", ""));
            // Convert the resource ID to a Bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
            uploadImageToStorage(bitmap, product, selectedImageName, category);
        } else {
            product.setImageUrl(imageUrl);
            addProductToFirestoreWithImageUrl(product);
        }
    }


    private void uploadImageToStorage(Bitmap imageBitmap, Product product, String name, String category) {
        // Obtain a reference to your Firebase Storage bucket
        StorageReference storageRef = mStorage.getReference();
        String fileName = "/Products/"+ category + "/" + name + ".jpg";
        StorageReference imageRef = storageRef.child(fileName);
        // Compress the imageBitmap if needed (optional)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();
        // Upload the image to Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Get the download URL as a String
                imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    String downloadUrl = downloadUri.toString();
                    product.setImageUrl(downloadUrl);
                    addProductToFirestoreWithImageUrl(product);
                }).addOnFailureListener(e -> {
                    showToast("Failed to download URL. Please try again.");
                });
            } else {
                showToast("Failed to upload image. Please try again.");
            }
        });
    }

    private void addProductToFirestoreWithImageUrl(Product product) {
        db.collection("Products")
                .add(product.toMap())
                .addOnSuccessListener(documentReference -> {
                    showToast(product.getName() + " added successfully!");
                })
                .addOnFailureListener(e -> {
                    showToast("Failed to add product. Please try again.");
                });
    }

    public void getProductsByCategory(String category, ProductFetchListener listener) {
        List<Product> products = new ArrayList<>();
        // Get a reference to the "Products" collection in Firestore
        CollectionReference productsRef = db.collection("Products");
        // Query for products in the specified category
        productsRef.whereEqualTo("category", category)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    // Loop through the query results and create ProductModel objects
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        Product product = documentSnapshot.toObject(Product.class);
                        product.setId(documentSnapshot.getId());
                        products.add(product);
                    }
                    // Call the listener's onProductFetch method with the list of products
                    listener.onProductFetch(products);
                })
                .addOnFailureListener(e -> {
                    // Call the listener's onFailure method with the error message
                    listener.onFailure("Failed to fetch products: " + e.getMessage());
                });
    }

    public interface ProductFetchListener {
        void onProductFetch(List<Product> products);
        void onFailure(String errorMessage);
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
