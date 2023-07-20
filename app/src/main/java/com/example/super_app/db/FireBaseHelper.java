package com.example.super_app.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.example.super_app.db.entity.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FireBaseHelper {
    private final DatabaseReference mDatabase;
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // Add this line in the FireBaseHelper constructor
    private final FirebaseStorage mStorage = FirebaseStorage.getInstance();

    private Context context;

    public FireBaseHelper() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    public static String getCurrentUser()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            return name;
        }
        return null;
    }

    public static void logOutUser(){
        mAuth.signOut();
    }


    public FireBaseHelper(Context context) {
        this.context = context;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

// ... (Other imports and code)

    public void addProductToFirestore(String name, String category, double price, int discount, String description, String imageUrl) {
        // Create a new Product instance with the provided data and image URL
        Product product = new Product(name, price, imageUrl, category, discount, description);

        // Check if the image URL is a drawable resource URL (e.g., "drawable://image_resource_id")
        if (imageUrl.startsWith("drawable://")) {
            // Extract the resource ID from the URL
            int resourceId = Integer.parseInt(imageUrl.replace("drawable://", ""));

            // Convert the resource ID to a Bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);

            // Upload the bitmap to Firebase Storage and get the download URL
            uploadImageToStorage(bitmap, product);
        } else {
            // If the image URL is a regular URL, proceed to add the product to Firestore
            product.setImageUrl(imageUrl); // Set the image URL to the product
            addProductToFirestoreWithImageUrl(product);
        }
    }



    // AsyncTask to download image from URL
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        private Product product;

        public DownloadImageTask(Product product) {
            this.product = product;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap imageBitmap = null;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                imageBitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return imageBitmap;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                // Upload the downloaded image to Firebase Storage
                uploadImageToStorage(result, product);
            }
        }
    }

    private void uploadImageToStorage(Bitmap imageBitmap, Product product) {
        // Obtain a reference to your Firebase Storage bucket
        StorageReference storageRef = mStorage.getReference();

        // Create a reference to the image file, and specify a filename (e.g., "product_image.jpg")
        String fileName = "product_image.jpg";
        StorageReference imageRef = storageRef.child(fileName);

        // Compress the imageBitmap if needed (optional)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] data = baos.toByteArray();

        // Upload the image to Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // Image uploaded successfully. Get the download URL
            imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                // Get the download URL as a String
                String downloadUrl = downloadUri.toString();

                // Update the product's imageUrl field with the download URL
                product.setImageUrl(downloadUrl);

                // Now, you can proceed to add the product to Firestore
                addProductToFirestoreWithImageUrl(product);
            }).addOnFailureListener(e -> {
                // Failed to get the download URL
                showToast("Failed to upload image. Please try again.");
            });
        }).addOnFailureListener(e -> {
            // Failed to upload the image
            showToast("Failed to upload image. Please try again.");
        });
    }


    private void addProductToFirestoreWithImageUrl(Product product) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Products")
                .add(product.toMap())
                .addOnSuccessListener(documentReference -> {
                    // Product added successfully to Firestore
                    // You can perform any success actions here if needed
                    showToast("Product added successfully!");
                })
                .addOnFailureListener(e -> {
                    // Failed to add the product to Firestore
                    // You can handle any failure actions here if needed
                    showToast("Failed to add product. Please try again.");
                });
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }




}
