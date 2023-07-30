package com.example.super_app.db;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.super_app.db.entity.Cart;
import com.example.super_app.db.entity.Order;
import com.example.super_app.db.entity.Product;
import com.example.super_app.db.entity.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class FireBaseHelper {
    private final DatabaseReference mDatabase;
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private static  Cart cart;
    private static Order order;
    private Context context;
    private DatabaseHelper dbHelper;



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

    public static Cart getCart() {
        return cart;
    }
    public static Order getOrder() {
        return order;
    }


    public void initializeCart() {
        if (cart == null) {
            cart = new Cart("my_first_cart", Calendar.getInstance().getTime(), 0, 0, null);
            cart.setProductsIDQuantity(new HashMap<>());
            //dbHelper.addCart(cart);
        }
    }
    public void initializeOrder() {
        if (order == null) {
            order = new Order();
        }
    }


    public void updateCartQuantity(Product product, int newQuantity) {
        if (cart != null) {
            cart.getProductsQuantity().put(product, newQuantity);
            HashMap<String, Integer> productsIDQuantity = new HashMap<>();
            productsIDQuantity.put(product.getName(), newQuantity);
            cart.setProductsIDQuantity(productsIDQuantity);
            //cart.getProductsIDQuantity().put(product.getId(), newQuantity);
            dbHelper.updateCartItemQuantity("my_first_cart", product.getName(), newQuantity);
        }
    }


    public static void logOutUser(){
        mAuth.signOut();
    }

    public FireBaseHelper(Context context) {
        this.context = context;
        this.mDatabase = FirebaseDatabase.getInstance().getReference();
        sqlitePer(context);

    }
    private void sqlitePer (Context context){
        dbHelper = new DatabaseHelper(context);
        SQLiteDatabase dbSqlite = dbHelper.getWritableDatabase();
    }

    public void handleProductToFirestore(String name, String category, double price, int discount,
                                         boolean healthy_tag, String imageUrl, String selectedImageName) {
        Product product = new Product(name, price, imageUrl, category, discount, healthy_tag);
        dbHelper.insertProductToProductDB(product);
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
                    showSnackbar(product.getName() + " added successfully!");
                    //showToast(product.getName() + " added successfully!");
                })
                .addOnFailureListener(e -> {
                    showSnackbar("Failed to add product. Please try again.");
                    //showToast("Failed to add product. Please try again.");
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


    public void addNewOrderToFirebase(Context context, String userName, String phoneNum, String selectedDate, String address, boolean shouldAddCart, String userID) {
        String uid = mAuth.getUid();
        if (uid == null) {
            // User not logged in, show a message or handle accordingly
           // Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show();
            showSnackbar("User not logged in");
            return;
        }

        db.collection("Orders").document(uid).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    // User already has an open order
                    //Toast.makeText(context, "You already have an open order", Toast.LENGTH_SHORT).show();
                    showSnackbar("You already have an open order");
                } else {
                    // Create a new order and add it to Firestore
                    Order newOrder = new Order(userName, phoneNum, selectedDate, address);
                    dbHelper.insertOrder(newOrder);
                    newOrder.setOpen(true);

                    db.collection("Orders").document(uid).set(newOrder)
                            .addOnSuccessListener(unused -> {
                                // New order added successfully
                                Toast.makeText(context, "New order has been opened by you", Toast.LENGTH_SHORT).show();
                                showSnackbar("New order has been opened by you");
                                // Check if the cart should be added
                                if (shouldAddCart) {
                                    addCartToFirestore(context, newOrder, uid, cart, 0); // Pass the current cart here

                                }
                            })
                            .addOnFailureListener(e -> {
                                // Failed to add the new order
                                showSnackbar("Failed to open a new order");
                                //Toast.makeText(context, "Failed to open a new order", Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                // Failed to fetch orders
                showSnackbar("Failed to fetch orders");
                //Toast.makeText(context, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addCartToFirestore(Context context, Order order, String orderId, Cart cart, double saving) {
        sqlitePer(context);
        if (cart != null) {
            cart.setDate(Calendar.getInstance().getTime());
            Map<String, Integer> productsIDQuantityStrings = cart.getProductsIDQuantity();

            Cart updatedCart = new Cart(cart.getCartId(), cart.getDate(), cart.getTotal(), cart.getDiscount(), cart.getOrderId());
            updatedCart.setProductsIDQuantity((HashMap<String, Integer>) productsIDQuantityStrings);
            updatedCart.setOrderId(orderId);
            // Adding updated cart to Firestore using the toMap method
            db.collection("Carts").add(updatedCart.toMap())
                    .addOnSuccessListener(documentReference -> {
                        // Get the generated cartId from the documentReference
                        String cartId = documentReference.getId();
                        updatedCart.setCartId(cartId); // Set the cartId in the updatedCart object
                        dbHelper.addCart(cart);
                        dbHelper.insertCartToOrder(cart);

                        // Cart added successfully to Firestore
                        showSnackbar("Cart added to FireBase");
                        //Toast.makeText(context, "Cart added to Firestore", Toast.LENGTH_SHORT).show();

                        // Update the orderId in the Cart object
                        updatedCart.setOrderId(cartId);

                        // Update the Order document with the new "cartsOfNeigh" HashMap
                        //HashMap<String, String> cartsOfNeigh =new HashMap<>();
                        order.getCartsOfNeigh().put(mAuth.getUid(), cartId);
                       // order.setCartsOfNeigh(cartsOfNeigh);
                        order.setTotalPrice(order.getTotalPrice() + cart.getTotal());
                        // order.getCartsOfNeigh().put(mAuth.getUid(), cartId);
                        order.setTotalPrice((order.getTotalPrice() + cart.getTotal()) * (1 - saving));
                        addSavingToFirestore(cart.getTotal() * saving);
                        db.collection("Orders").document(orderId).set(order)
                                .addOnSuccessListener(unused -> {
                                    // Order updated successfully with the cart information
                                    showSnackbar("Order updated with the cart information");
                                    //Toast.makeText(context, "Order updated with the cart information", Toast.LENGTH_SHORT).show();
                                    saveOrderToOrderHistory(order);
                                })
                                .addOnFailureListener(e -> {
                                    // Failed to update Order with the cart information
                                    showSnackbar("Failed to update Order with the cart information");
                                    //Toast.makeText(context, "Failed to update Order with the cart information", Toast.LENGTH_SHORT).show();
                                    Log.e("FireBaseHelper", "Error updating Order with the cart information", e);
                                });
                        cart.getProductsQuantity().clear();


                    })
                    .addOnFailureListener(e -> {
                        // Failed to add cart to Firebase
                        showSnackbar("Failed to add cart to FireBase");
                        //Toast.makeText(context, "Failed to add cart to FireBase", Toast.LENGTH_SHORT).show();
                        Log.e("FireBaseHelper", "Error adding cart to FireBase", e);
                    });

        }
    }

    private void addSavingToFirestore(double saving) {
        // Get the current user ID
        String userId = mAuth.getUid();
        if (userId == null) {

            return;
        }
        CollectionReference savingsCollection = FirebaseFirestore.getInstance().collection("savings");
        DocumentReference savingDocument = savingsCollection.document();

        Map<String, Object> savingData = new HashMap<>();
        savingData.put("userId", userId); // The user ID
        savingData.put("savingAmount", saving); // The saving amount
        // Add the saving information to Firestore
        savingDocument.set(savingData)
                .addOnSuccessListener(aVoid -> {
                    // Saving added successfully
                    showSnackbar("Saving added successfully: " + saving);
                })
                .addOnFailureListener(e -> {
                    // Failed to add saving
                    showSnackbar("Failed to add saving: " + e.getMessage());
                });
    }
    public interface TotalSavingsCallback {
        void onTotalSavingsFetched(double totalSavings);
    }
    public double getSavingFromFirestore(TotalSavingsCallback callback) {
        String userId = mAuth.getUid();
        if (userId == null) {
            return 0.0;
        }
        CollectionReference savingsCollection = FirebaseFirestore.getInstance().collection("savings");
        Query query = savingsCollection.whereEqualTo("userId", userId);
        AtomicReference<Double> totalSavingsRef = new AtomicReference<>(0.0);
        query.get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        Double savingAmount = documentSnapshot.getDouble("savingAmount");
                        if (savingAmount != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                totalSavingsRef.updateAndGet(v -> v + savingAmount);
                            }
                        }
                    }
                    double totalSavings = totalSavingsRef.get();
                    callback.onTotalSavingsFetched(totalSavings);
                    Log.d("FireBaseHelper", "Total savings for user " + userId + ": " + totalSavings);
                })
                .addOnFailureListener(e -> {

                    Log.e("FireBaseHelper", "Failed to fetch savings: " + e.getMessage());
                });
        return totalSavingsRef.get();
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

    public void fetchOrdersFromFirestore(FirestoreFetchListener listener) {
        CollectionReference ordersRef = db.collection("Orders");

        // Fetch all orders
        ordersRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Order> orders = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Order order = new Order();

                        // Check if the 'id' field is of type String or Long and convert accordingly
                        Object idValue = documentSnapshot.get("id");
                        if (idValue instanceof String) {
                            order.setId((String) idValue);
                        } else if (idValue instanceof Long) {
                            order.setId(String.valueOf((Long) idValue));
                        } else {
                            // Handle the case where 'id' is neither String nor Long
                            // For example, throw an error, skip the order, or set a default value
                        }

                        // Set other fields as before
                        order.setAddress(documentSnapshot.getString("address"));
                        order.setDeliveryDate(documentSnapshot.getString("deliveryDate"));
                        // Set other fields as needed...

                        orders.add(order);
                    }
                    // Notify the listener with the fetched orders
                    listener.onOrderFetch(orders);
                })
                .addOnFailureListener(e -> {
                    // Notify the listener about the failure to fetch orders
                    listener.onFailure("Failed to fetch orders: " + e.getMessage());
                });
    }


    public interface FirestoreFetchListener {
        void onOrderFetch(List<Order> orders);
        void onFailure(String errorMessage);
    }

    public void fetchAllProductsFromFireBase(AllProductsFetchListener listener) {
        List<Product> productList = new ArrayList<>();
        CollectionReference productsRef = db.collection("Products");

        // Query for products in the specified category
        productsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    // Loop through the query results and create Product objects
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Product product = documentSnapshot.toObject(Product.class);
                        product.setId(documentSnapshot.getId());
                        productList.add(product);
                    }
                    // Call the listener's onProductFetch method with the list of products
                    listener.onProductFetch(productList);
                })
                .addOnFailureListener(e -> {
                    // Call the listener's onFailure method with the error message
                    listener.onFailure("Failed to fetch products: " + e.getMessage());
                });
    }

    public interface AllProductsFetchListener {
        void onProductFetch(List<Product> productList);
        void onFailure(String errorMessage);
    }

    public void fetchAllOrdersFromFireBase(AllOrdersFetchListener listener) {
        List<Order> orderList = new ArrayList<>();
        CollectionReference ordersRef = db.collection("Orders");

        // Query for orders
        ordersRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
                    // Loop through the query results and create Order objects
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Order order = documentSnapshot.toObject(Order.class);
                        order.setId(documentSnapshot.getId());
                        orderList.add(order);
                    }
                    // Call the listener's onOrdersFetch method with the list of orders
                    listener.onOrdersFetch(orderList);
                })
                .addOnFailureListener(e -> {
                    // Call the listener's onFailure method with the error message
                    listener.onFailure("Failed to fetch orders: " + e.getMessage());
                });
    }

    public void fetchOrdersInSameStreetFromFireBase(String userStreet, AllOrdersFetchListener listener) {
        List<Order> orderList = new ArrayList<>();
        CollectionReference ordersRef = db.collection("Orders");

        // Query for orders in the same street as the user
        ordersRef.whereEqualTo("street", userStreet)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Loop through the query results and create Order objects
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Order order = documentSnapshot.toObject(Order.class);
                        order.setId(documentSnapshot.getId());
                        orderList.add(order);
                    }
                    // Call the listener's onOrdersFetch method with the list of orders
                    listener.onOrdersFetch(orderList);
                })
                .addOnFailureListener(e -> {
                    // Call the listener's onFailure method with the error message
                    listener.onFailure("Failed to fetch orders: " + e.getMessage());
                });
    }


    public interface AllOrdersFetchListener {
        void onOrdersFetch(List<Order> orderList);
        void onFailure(String errorMessage);
    }

    public interface FetchOrderCallback {
        void onOrderFetched(Order order);
        void onFailure(String errorMessage);
    }
    public void fetchOrderByAddressAndDate(String address, String deliveryDate, FetchOrderCallback callback) {
        CollectionReference ordersRef = db.collection("Orders");

        // Query for orders with the given address and delivery date
        ordersRef.whereEqualTo("address", address)
                .whereEqualTo("deliveryDate", deliveryDate)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        List<Order> orders = new ArrayList<>();
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            Order order = documentSnapshot.toObject(Order.class);
                            order.setId(documentSnapshot.getId());
                            orders.add(order);
                        }

                        if (!orders.isEmpty()) {
                            // If orders with the given address and delivery date are found, return the first order (you can modify as per your requirement)
                            Order foundOrder = orders.get(0);
                            // Now you can use the foundOrder object or perform any other operations with it.
                            // For example, you can update the UI with the order details.
                            callback.onOrderFetched(foundOrder);
                        } else {
                            // No matching order found
                            callback.onFailure("No order found with the given address and delivery date");
                        }
                    } else {
                        // Failed to fetch orders, handle the error here
                        callback.onFailure("Failed to fetch orders: " + task.getException().getMessage());
                    }
                });
    }
    public void updateProductInFireBase(Product updatedProduct) {
        if (updatedProduct == null || updatedProduct.getId() == null) {
            // Invalid product or product ID, return early
            return;
        }

        DocumentReference productRef = db.collection("Products").document(updatedProduct.getId());

        // Create a map with the updated fields to update the Firestore document
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("name", updatedProduct.getName());
        updatedFields.put("category", updatedProduct.getCategory());
        updatedFields.put("price", updatedProduct.getPrice());
        updatedFields.put("discount", updatedProduct.getDiscount());
        updatedFields.put("isHealthyTag", updatedProduct.isHealthyTag());

        // Perform the update
        productRef
                .update(updatedFields)
                .addOnSuccessListener(aVoid -> {
                    // Success
                    showSnackbar("Product updated successfully in Firebase");
                   // Toast.makeText(context, "Product updated successfully in Firebase", Toast.LENGTH_SHORT).show();
                    Log.d("FireBaseHelper", "Product updated successfully in Firebase");
                })
                .addOnFailureListener(e -> {
                    showSnackbar("Error updating product in FireBase: " + e.getMessage());
                    //Toast.makeText(context, "Error updating product in FireBase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Error handling
                    Log.e("FireBaseHelper", "Error updating product in FireBase: " + e.getMessage());
                });
    }

    public void deleteProductFromFireBase(Product product) {
        if (product == null || product.getId() == null) {
            // Invalid product or product ID, return early
            return;
        }
        DocumentReference productRef = db.collection("Products").document(product.getId());
        // Perform the delete operation
        productRef.delete().addOnSuccessListener(aVoid -> {
                    // Success
                    showSnackbar("Product deleted successfully in Firebase");
                    //Toast.makeText(context, "Product deleted successfully in Firebase", Toast.LENGTH_SHORT).show();
                    Log.d("FireBaseHelper", "Product deleted successfully from Firebase");
                })
                .addOnFailureListener(e -> {
                    // Error handling
                    showSnackbar("Error deleting product from Firebase: " + e.getMessage());
                   // Toast.makeText(context, "Error deleting product from Firebase", Toast.LENGTH_SHORT).show();
                    Log.e("FireBaseHelper", "Error deleting product from Firebase: " + e.getMessage());
                });
    }

    public void updateOrderInFireBase(Order order) {
        CollectionReference ordersRef = db.collection("Orders");
        DocumentReference orderDocRef = ordersRef.document(order.getId());

        // Create a Map to represent the updated data
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("fullNameOwner", order.getFullNameOwner());
        updatedData.put("phoneNumberOwner", order.getPhoneNumberOwner());
        updatedData.put("deliveryDate", order.getDeliveryDate());
        updatedData.put("address", order.getAddress());
        updatedData.put("totalPrice", order.getTotalPrice());

        // Update the order document with the new data
        orderDocRef.update(updatedData)
                .addOnSuccessListener(aVoid -> {
                    // Update successful
                    showSnackbar("Order updated successfully.");
                    //Toast.makeText(context, "Order updated successfully.", Toast.LENGTH_SHORT).show();
                    Log.d("FireBaseHelper", "Order updated successfully.");
                })
                .addOnFailureListener(e -> {
                    // Update failed
                    showSnackbar("Failed to update order: " + e.getMessage());
                   // Toast.makeText(context, "Failed to update order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("FireBaseHelper", "Failed to update order: " + e.getMessage());
                });
    }

    //fetch cat from order
    public void fetchCartsForOrder(String orderId, CartsFetchListener listener) {
        // Get a reference to the Firestore "Orders" collection


        db.collection("Orders").document(mAuth.getUid()).get().addOnSuccessListener(orderSnapshot -> {
            if (orderSnapshot.exists()) {
                Order order = orderSnapshot.toObject(Order.class);

                // Get the cart ID from the order's cartsOfNeigh map
                String cartId = order.getCartsOfNeigh().get(orderId);
                if (cartId != null) {
                    Log.d("FireBaseHelper", "Fetching cart with ID: " + cartId);
                    // Fetch the cart using the cart ID from the "carts" collection
                    db.collection("Carts").document(cartId).get().addOnSuccessListener(cartSnapshot -> {
                        if (cartSnapshot.exists()) {
                            Cart cart = cartSnapshot.toObject(Cart.class);
                            cart.setCartId(cartId);
                            // Get the product names and quantities from the fetched cart
                            //Map<String, Long> itemsMap = cartSnapshot.getLong("items");
                            HashMap<String, Object> productsIDQuantity = (HashMap<String, Object>) cartSnapshot.get("items");
                            HashMap<String, Integer> productsIDQuantityForCart = (HashMap<String, Integer>) cartSnapshot.get("items");
                            cart.setProductsIDQuantity(productsIDQuantityForCart);
//                            List<String> productsInOrder = new ArrayList<>();
//
//                            for (Map.Entry<String, Integer> entry : productsIDQuantity.entrySet()) {
//                                String productId = entry.getKey();
//                                int quantity = entry.getValue();
//                                productsInOrder.add(productId + " - Quantity: " + quantity);
//                            }

                            // Notify the listener with the fetched products and quantities
                            listener.onCartFetch(productsIDQuantity);
                        } else {
                            // Handle the case when the cart does not exist
                            Log.d("FireBaseHelper", "Cart not found for the specified order.");
                            listener.onFailure("Cart not found for the specified order.");
                        }
                    }).addOnFailureListener(e -> {
                        // Handle the failure to fetch the cart if needed
                        Log.d("FireBaseHelper", "Failed to fetch cart: " + e.getMessage());
                        listener.onFailure("Failed to fetch cart: " + e.getMessage());
                    });
                } else {
                    // Handle the case when the order does not have a cart ID
                    Log.d("FireBaseHelper", "Cart ID not found for the specified order.");
                    listener.onFailure("Cart ID not found for the specified order.");
                }
//
//               else {
//                    // Handle the case when the order does not have a cart ID
//                    listener.onFailure("Cart ID not found for the specified order.");
//                }
            } else {
                // Handle the case when the order does not exist
                listener.onFailure("Order not found with the specified ID.");
            }
        }).addOnFailureListener(e -> {
            // Handle the failure to fetch the order if needed
            listener.onFailure("Failed to fetch order: " + e.getMessage());
        });
    }




    public interface CartsFetchListener {
        void onCartFetch(HashMap<String, Object> productsIDQuantity);
        void onFailure(String errorMessage);
    }

    public void fetchOrdersOfCurrentUserFromFirebase(UserOrdersFetchListener listener) {
        List<Order> userOrdersList = new ArrayList<>();
//        String uid = mAuth.getCurrentUser().getUid();

    // Get the document by user's UID
        db.collection("Orders").document(mAuth.getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // If the document exists, create an Order object and add it to the list
                        Order order = documentSnapshot.toObject(Order.class);
                        order.setId(documentSnapshot.getId());
                        userOrdersList.add(order);

                        // Call the listener's onUserOrdersFetch method with the list of user's orders
                        listener.onUserOrdersFetch(userOrdersList);
                    } else {
                        // Document with the user's UID does not exist (no orders)
                        // Call the listener's onUserOrdersFetch method with an empty list
                        listener.onUserOrdersFetch(Collections.emptyList());
                    }
                })
                .addOnFailureListener(e -> {
                    // Call the listener's onFailure method with the error message
                    listener.onFailure("Failed to fetch user's orders: " + e.getMessage());
                });

    }



    public interface UserOrdersFetchListener {
        void onUserOrdersFetch(List<Order> userOrdersList);
        void onFailure(String errorMessage);
    }

    public void fetchOrdersHistoryOfCurrentUserFromFirebase(UserOrdersHFetchListener listener) {
        List<Order> userOrdersHList = new ArrayList<>();
        // Get the "Orders" sub-collection under the user's document in the "OrderHistory" collection
        db.collection("OrderHistory").document(mAuth.getUid()).collection("Orders")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    // Loop through the query results and create Order objects
                    for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                        Order order = documentSnapshot.toObject(Order.class);
                        order.setId(documentSnapshot.getId());
                        userOrdersHList.add(order);
                    }

                    // Call the listener's onUserOrdersFetch method with the list of user's orders
                    listener.onUserOrdersFetch(userOrdersHList);
                })
                .addOnFailureListener(e -> {
                    // Call the listener's onFailure method with the error message
                    listener.onFailure("Failed to fetch user's orders: " + e.getMessage());
                });
    }

    public interface UserOrdersHFetchListener {
        void onUserOrdersFetch(List<Order> userOrdersHList);
        void onFailure(String errorMessage);
    }


    public void deleteOrderFromFirebase(String orderId) {
        CollectionReference ordersRef = db.collection("Orders");
        DocumentReference orderDocRef = ordersRef.document(orderId);

        // Perform the delete operation
        orderDocRef.delete()
                .addOnSuccessListener(aVoid -> {
                    // Order deleted successfully
                    showSnackbar("Order deleted successfully");
                    //Toast.makeText(context, "Order deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    showSnackbar("Failed to delete order: " + e.getMessage());
                    //Toast.makeText(context, "Failed to delete order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
    public void updateUserInFirebase(User user) {
        // Get a reference to the "Users" collection in Firebase
        CollectionReference usersRef = db.collection("Users");

        // Use the user ID to identify the document (user) you want to update
        String uid = mAuth.getCurrentUser().getUid();
        DocumentReference userDocRef = usersRef.document(uid);

        // Perform the update operation using the user object
        userDocRef.set(user)
                .addOnSuccessListener(aVoid -> {
                    showSnackbar("User data updated successfully");
                    //Toast.makeText(context, "User data updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    showSnackbar("Failed to update user data: " + e.getMessage());
                    //Toast.makeText(context, "Failed to update user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public void getUserFromFirebase(String userId, UserFetchListener listener) {
        // Get a reference to the "Users" collection in Firebase
        CollectionReference usersRef = db.collection("Users");

        // Use the user ID to identify the document (user) you want to retrieve
        DocumentReference userDocRef = usersRef.document(userId);

        // Perform the get operation to fetch the user data
        userDocRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // User document exists, retrieve the data and convert it to a User object
                        User user = documentSnapshot.toObject(User.class);
                        listener.onUserFetch(user);
                    } else {
                        // User document does not exist or is not found
                        listener.onUserFetch(null);
                    }
                })
                .addOnFailureListener(e -> {
                    // An error occurred while trying to fetch the user data
                    // Handle the error here, if needed
                    listener.onFailure("Failed to fetch user data: " + e.getMessage());
                });
    }

    public interface UserFetchListener {
        void onUserFetch(User user);
        void onFailure(String errorMessage);
    }


    public void saveOrderToOrderHistory(Order order) {
        // Add the order to the "OrderHistory" collection with the user's ID as the document ID
        db.collection("OrderHistory").document(mAuth.getUid()).collection("Orders").add(order)
                .addOnSuccessListener(documentReference -> {
                    // Successfully saved the order to the order history
                    showSnackbar("Order saved to order history");
                    //Toast.makeText(context, "Order saved to order history " , Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Order saved to order history with ID: ");
                })
                .addOnFailureListener(e -> {
                    // Failed to save the order to the order history
                    showSnackbar("Error saving order to order history: "+ e.getMessage());
                   // Toast.makeText(context, "Error saving order to order history: "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error saving order to order history: " + e.getMessage());
                });
    }

    private void showSnackbar(String message) {
        // Make sure the context is not null before showing the Snackbar
        if (context != null) {
            View rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
            if (rootView != null) {
                Snackbar snackbar = Snackbar.make(rootView, message, Snackbar.LENGTH_LONG);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        }
    }

    public void updateCartAndOrder(Context context, Order order, String orderId, Cart cart) {
        sqlitePer(context);

        if (cart != null) {
            cart.setDate(Calendar.getInstance().getTime());
            Map<String, Integer> productsIDQuantityStrings = cart.getProductsIDQuantity();

            Cart updatedCart = new Cart(cart.getCartId(), cart.getDate(), cart.getTotal(), cart.getDiscount(), orderId);
            updatedCart.setProductsIDQuantity((HashMap<String, Integer>) productsIDQuantityStrings);

            // Adding updated cart to Firestore using the toMap method
            db.collection("Carts").document(cart.getCartId())
                    .set(updatedCart.toMap())
                    .addOnSuccessListener(aVoid -> {
                        dbHelper.addCart(cart);
                        dbHelper.insertCartToOrder(cart);

                        // Cart added successfully to Firestore
                        showSnackbar("Cart added to Firebase");

                        // Update the orderId in the Cart object
                        updatedCart.setOrderId(orderId);

                        // Update the Order document with the new "cartsOfNeigh" HashMap
                        HashMap<String, String> cartsOfNeigh = new HashMap<>();
                        cartsOfNeigh.put(mAuth.getUid(), cart.getCartId());
                        order.setCartsOfNeigh(cartsOfNeigh);
                        order.setTotalPrice(order.getTotalPrice() + cart.getTotal());

                        // Update the Order document in Firestore
                        db.collection("Orders").document(orderId)
                                .set(order.toMap()) // Assuming you have a toMap() method in your Order class
                                .addOnSuccessListener(aVoid1 -> {
                                    // Order updated successfully with the cart information
                                    showSnackbar("Order updated with the cart information");
                                    saveOrderToOrderHistory(order);
                                })
                                .addOnFailureListener(e -> {
                                    // Failed to update Order with the cart information
                                    showSnackbar("Failed to update Order with the cart information");
                                    Log.e("FireBaseHelper", "Error updating Order with the cart information", e);
                                });
                    })
                    .addOnFailureListener(e -> {
                        // Failed to add cart to Firebase
                        showSnackbar("Failed to add cart to Firestore");
                        Log.e("FireBaseHelper", "Error adding cart to Firestore", e);
                    });
        }
    }

    public void deleteOrder(Order order) {
        // Delete the order from Firebase or your data source based on your implementation
        // For example, if using Firestore, you can do the following:

        // Assuming you have a reference to the "Orders" collection
        CollectionReference ordersRef = db.collection("Orders");

        // Get the order document reference using the order ID
        DocumentReference orderDocRef = ordersRef.document(order.getId());

        // Delete the order document
        orderDocRef.delete()
                .addOnSuccessListener(aVoid -> {
                    // Order deleted successfully
                    showSnackbar("Order deleted successfully.");
                })
                .addOnFailureListener(e -> {
                    // Failed to delete order
                    showSnackbar("Failed to delete order: " + e.getMessage());
                });
    }




















}