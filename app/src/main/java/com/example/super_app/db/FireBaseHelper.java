package com.example.super_app.db;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.example.super_app.db.entity.Cart;
import com.example.super_app.db.entity.Order;
import com.example.super_app.db.entity.Product;
import com.example.super_app.db.entity.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FireBaseHelper {
    private final DatabaseReference mDatabase;
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private static  Cart cart;
    private static Order order;
    private Context context;

    public FireBaseHelper() {
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        initializeCart();
        initializeOrder();
    }

    public static String getCurrentUser()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getDisplayName();
        }
        return null;
    }
    public static Task<DocumentSnapshot> getUserData() {
        String userId = mAuth.getCurrentUser().getUid();
        DocumentReference userRef = db.collection("users").document(userId);

        return userRef.get();
    }



    public static Cart getCart() {
        return cart;
    }
    public static Order getOrder() {
        return order;
    }


    public void initializeCart() {
        if (cart == null) {
            cart = new Cart("my_first_cart", Calendar.getInstance().getTime(), 0, 0, null);
            //cart.setProductsIDQuantity(new HashMap<>());
        }
    }
    public void initializeOrder() {
        if (order == null) {
            order = new Order();
        }
    }


    public void updateCartQuantity(Product product, int newQuantity) {
        if (cart != null) {
            HashMap<Product, Integer> productsQuantity = new HashMap<>();
            productsQuantity.put(product, newQuantity);
            HashMap<String, Integer> productsQuantity1 = new HashMap<>();
            productsQuantity1.put(product.getName(), newQuantity);

            cart.setProductsIDQuantity(productsQuantity1);
            cart.setProductsQuantity(productsQuantity);

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


    public void addNewOrderToFirebase(Context context, String userName, String phoneNum, String selectedDate, String address, boolean shouldAddCart, Cart currentCart) {
        String uid = mAuth.getUid();
        if (uid == null) {
            // User not logged in, show a message or handle accordingly
            Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("Orders").document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    // User already has an open order
                    Toast.makeText(context, "You already have an open order", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a new order and add it to Firestore
                    Date orderCreationDate = currentCart.getDate();
                    Order newOrder = new Order(userName, phoneNum, selectedDate, address, orderCreationDate);
                    newOrder.setOpen(true);
                    db.collection("Orders").document(uid).set(newOrder)
                            .addOnSuccessListener(unused -> {
                                // New order added successfully
                                Toast.makeText(context, "New order has been opened by you", Toast.LENGTH_SHORT).show();

                                // Check if the cart should be added
                                if (shouldAddCart) {
                                    addCartToOrderFirestore(context, newOrder, uid, currentCart); // Pass the current cart here
                                }
                            })
                            .addOnFailureListener(e -> {
                                // Failed to add the new order
                                Toast.makeText(context, "Failed to open a new order", Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                // Failed to fetch orders
                Toast.makeText(context, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addCartToOrderFirestore(Context context, Order order, String orderId, Cart cart) {
        if (cart != null) {
            cart.setOrderId(orderId);
            cart.setDate(Calendar.getInstance().getTime());

            // Convert the keys of productsIDQuantity to strings
            HashMap<String, Integer> productsIDQuantityStrings = new HashMap<>();
            for (Map.Entry<Product, Integer> entry : cart.getProductsQuantity().entrySet()) {
                Product product = entry.getKey();
                String productName = product.getName(); // Assuming getName() returns the unique identifier of the product
                int quantity = entry.getValue();
                productsIDQuantityStrings.put(productName, quantity);
            }
            cart.setProductsIDQuantity(productsIDQuantityStrings);
            order.setProductsIDQuantity(productsIDQuantityStrings);
            order.setTotalPrice(cart.getTotal());


            // Add the cart to Firestore
            db.collection("Orders").document(orderId)
                    .set(order) // Set the cart object directly to Firestore
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Cart added to Firestore", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to add cart to Firestore", Toast.LENGTH_SHORT).show();
                        Log.e("FireBaseHelper", "Error adding cart to Firestore", e);
                    });


        }
    }


    public void addUserData (User user) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User data added to FireStore successfully.");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding user data to FireStore: ", e);
                });
    }





}
