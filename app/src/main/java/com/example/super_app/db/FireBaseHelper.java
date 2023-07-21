package com.example.super_app.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class FireBaseHelper {
    private final DatabaseReference mDatabase;
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseStorage mStorage = FirebaseStorage.getInstance();

    private Context context;

    public FireBaseHelper() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static String getCurrentUser()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getDisplayName();
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

    public void addProductToFirestore(String name, String category, double price, int discount, boolean healthy_tag, String imageUrl, String selectedImageName) {
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
                // Get the download URL as a String - use if needed (currently will retrieve by path)
//                imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
//                    String downloadUrl = downloadUri.toString();
                imageRef.getMetadata().addOnSuccessListener(storageMetadata -> {
                    // Get the storage location (path) as a String
                    String storageLocation = storageMetadata.getPath();
                    product.setImageUrl(storageLocation);
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
    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
