package com.example.super_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.Cart;
import com.example.super_app.db.entity.Order;
import com.example.super_app.db.entity.Product;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminActivity extends Activity {
    private FireBaseHelper fireBaseHelper;
    private static final int REQUEST_IMAGE_SELECT = 1;
    private EditText productNameEditText;
    private EditText productPriceEditText;
    private ImageView productImageView;
    String selectedImageUrl = "drawable://" + R.drawable.default_image;
    private String selectedCategory;
    private Product product;
    private DatabaseHelper dbHelper;
    private List<Product> productListFromFB;

    private List<Order> orderListFromFB;
    private Cart cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        fireBaseHelper = new FireBaseHelper(this);
        dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        productListFromFB = new ArrayList<>();
        orderListFromFB = new ArrayList<>();

        Context context = getApplicationContext();

        //fetch data from firebase to sqlite
        fetchAllProductsFromFireBase();
        fetchAllOrdersFromFireBase();


        Button logOutBtn = findViewById(R.id.logOutBtn);
        Button createProductBtn = findViewById(R.id.createProductBtn);
        createProductBtn.setOnClickListener(v -> showAddProductDialog());

        Button updateProductBtn = findViewById(R.id.updateProductBtn);
        updateProductBtn.setOnClickListener(v -> showAllProducts());

        Button deleteProductBtn = findViewById(R.id.deleteProductBtn);
        deleteProductBtn.setOnClickListener(v->showAllProductsDelete());

        Button editOrderBtn = findViewById(R.id.editOrderBtn);
        editOrderBtn.setOnClickListener(v->showAllOrdersEdit());

        Button deleteOrderBtn = findViewById(R.id.deleteOrderBtn);
        deleteOrderBtn.setOnClickListener(v->showAllOrdersDelete());
        SharedPreferences sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        logOutBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logout")
                    .setMessage("Are you sure you want to Log Out?")
                    .setPositiveButton("Log Out", (dialog, which) -> {
                        // Remove user name from SharedPreferences
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove("userName");
                        editor.apply();
                        FireBaseHelper.logOutUser();
                        logOutBtn.setVisibility(View.INVISIBLE);
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {

                    })
                    .setCancelable(false)
                    .show();
        });

        //dbHelper.printAllProducts();
    }
    protected void onResume() {
        super.onResume();
        // Fetch the updated data from Firebase
        fetchAllProductsFromFireBase();
        fetchAllOrdersFromFireBase();
//        fetchAllCartsFromFireBase();
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);
        builder.setTitle("Add New Product");

        productNameEditText = dialogView.findViewById(R.id.productNameEditText);
        productPriceEditText = dialogView.findViewById(R.id.productPriceEditText);
        Spinner productCategorySpinner = dialogView.findViewById(R.id.productCategorySpinner);
        productImageView = dialogView.findViewById(R.id.productImageView);
        CheckBox healthyTagCheckBox = dialogView.findViewById(R.id.healthyTagCheckBox);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categoryArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productCategorySpinner.setAdapter(adapter);

        productCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "Category";
            }
        });

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = productNameEditText.getText().toString().trim();
            String priceStr = productPriceEditText.getText().toString().trim();
            boolean isHealthyTag = healthyTagCheckBox.isChecked();
            if (!name.isEmpty() && !priceStr.isEmpty() && !selectedCategory.isEmpty()) {
                double price = Double.parseDouble(priceStr);
                int discount = 1; // by default no discount
                fireBaseHelper.handleProductToFirestore(name, selectedCategory, price, discount, isHealthyTag, selectedImageUrl, name);
                //  Product(String name, String img, String category, double price, int discount, int quantity, String description)
                product = new Product(name, selectedImageUrl, selectedCategory, price, discount, 1);

                //dbHelper.insertProductToProductDB(product);



            } else {
                // Handle case when any of the fields are empty
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        productImageView.setImageResource(R.drawable.default_image);
        productImageView.setOnClickListener(view -> showImageSelection());
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void showImageSelection() {
        Intent intent = new Intent(this, ImageSelectionActivity.class);
        startActivityForResult(intent, REQUEST_IMAGE_SELECT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_SELECT && resultCode == RESULT_OK && data != null) {
            int selectedImageResourceId = data.getIntExtra("imageResourceId", R.drawable.default_image);
            productImageView.setImageResource(selectedImageResourceId); // Set the chosen image to the productImageView
            selectedImageUrl = "drawable://" + selectedImageResourceId;
        }
    }
    private void showAllProducts() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("All Products");

        // Create a list of product names to display in the dialog
        List<String> productNames = new ArrayList<>();
        for (Product product : productListFromFB) {
            productNames.add(product.getName());
        }

        // Convert the list of product names to an array for the dialog
        final String[] productNamesArray = productNames.toArray(new String[0]);

        builder.setItems(productNamesArray, (dialog, which) -> {
            // Get the selected product based on the index
            Product selectedProduct = productListFromFB.get(which);
            // Call the edit dialog for the selected product
            showEditProductDialog(selectedProduct);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEditProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);
        builder.setTitle("Edit Product");

        productNameEditText = dialogView.findViewById(R.id.productNameEditText);
        productPriceEditText = dialogView.findViewById(R.id.productPriceEditText);
        Spinner productCategorySpinner = dialogView.findViewById(R.id.productCategorySpinner);
        productImageView = dialogView.findViewById(R.id.productImageView);
        CheckBox healthyTagCheckBox = dialogView.findViewById(R.id.healthyTagCheckBox);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categoryArray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productCategorySpinner.setAdapter(adapter);

        // Pre-fill the fields with the existing product data
        productNameEditText.setText(product.getName());
        productPriceEditText.setText(String.valueOf(product.getPrice()));
        healthyTagCheckBox.setChecked(product.isHealthyTag());
        int categoryPosition = adapter.getPosition(product.getCategory());
        productCategorySpinner.setSelection(categoryPosition);

        productCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "Category";
            }
        });

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = productNameEditText.getText().toString().trim();
            String priceStr = productPriceEditText.getText().toString().trim();
            boolean isHealthyTag = healthyTagCheckBox.isChecked();
            if (!name.isEmpty() && !priceStr.isEmpty() && !selectedCategory.isEmpty()) {
                double price = Double.parseDouble(priceStr);
                int discount = 1; // by default no discount
                fireBaseHelper.handleProductToFirestore(name, selectedCategory, price, discount, isHealthyTag, selectedImageUrl, name);

                // Update the product with the edited data
                product.setName(name);
                product.setCategory(selectedCategory);
                product.setPrice(price);
                product.setHealthy_tag(isHealthyTag);

                dbHelper.editProductFromProductDB(product);
                fireBaseHelper.updateProductInFireBase(product);

                // You may want to notify the adapter or refresh the product list if applicable

            } else {
                // Handle case when any of the fields are empty
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        productImageView.setImageResource(R.drawable.default_image);
        productImageView.setOnClickListener(view -> showImageSelection());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAllProductsDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("All Products");

        // Create a list of product names to display in the dialog
        List<String> productNames = new ArrayList<>();
        for (Product product : productListFromFB) {
            productNames.add(product.getName());
        }

        // Convert the list of product names to an array for the dialog
        final String[] productNamesArray = productNames.toArray(new String[0]);

        builder.setItems(productNamesArray, (dialog, which) -> {
            // Get the selected product based on the index
            Product selectedProduct = productListFromFB.get(which);
            // Call the delete dialog for the selected product
            showDeleteProductDialog(selectedProduct);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteProductDialog(Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Product");
        builder.setMessage("Are you sure you want to delete this product?");

        builder.setPositiveButton("Delete", (dialog, which) -> {
            // Perform the delete operation
            fireBaseHelper.deleteProductFromFireBase(product);
            dbHelper.deleteProductFromProductDB(product);

            // You may want to notify the adapter or refresh the product list if applicable
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAllOrdersEdit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("All Orders");

        // Create a list of order names to display in the dialog
        List<String> orderNames = new ArrayList<>();
        for (Order order : orderListFromFB) {
            orderNames.add(order.getFullNameOwner() + " - " + order.getPhoneNumberOwner());
        }

        // Convert the list of order names to an array for the dialog
        final String[] orderNamesArray = orderNames.toArray(new String[0]);

        builder.setItems(orderNamesArray, (dialog, which) -> {
            // Get the selected order based on the index
            Order selectedOrder = orderListFromFB.get(which);
            // Call the edit dialog for the selected order
            showEditOrderDialog(selectedOrder);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEditOrderDialog(Order order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_order, null);
        builder.setView(dialogView);
        builder.setTitle("Edit Order");

        // Find and initialize the views for editing the order details
        EditText orderNameEditText = dialogView.findViewById(R.id.orderNameEditText);
        EditText orderPriceEditText = dialogView.findViewById(R.id.order_price);
        ListView orderProductsListView = dialogView.findViewById(R.id.orderProductsListView);
        TextView orderTotalPriceTextView = dialogView.findViewById(R.id.orderTotalPriceTextView);

        // Set the current order details in the EditTexts
        orderNameEditText.setText(order.getFullNameOwner());
        orderPriceEditText.setText(String.valueOf(order.getTotalPrice()));

        // Initialize an empty list of products for now
        HashMap<String, Integer> productsIDQuantity = new HashMap<>();

        // Set up the adapter to display the products and quantities in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        orderProductsListView.setAdapter(adapter);

        // Display the total price of the order
        orderTotalPriceTextView.setText("Total Price: " + order.getTotalPrice());

        // Create an instance of the listener to handle the fetched cart data
        FireBaseHelper.CartsFetchListener cartsFetchListener = new FireBaseHelper.CartsFetchListener() {

            @Override
            public void onCartFetch(HashMap<String, Object> items) {
                // Initialize the List to hold the product names and quantities
                List<String> productsInOrderList = new ArrayList<>();

                // Iterate over the items HashMap and extract the product names and quantities
                for (Map.Entry<String, Object> entry : items.entrySet()) {
                    String product = entry.getKey();
                    Object value = entry.getValue();

                    // Check if the value is a valid Long
                    if (value instanceof Long) {
                        Long longValue = (Long) value;
                        int quantity = longValue.intValue();
                        String productWithQuantity = product + " - Quantity: " + quantity;
                        productsInOrderList.add(productWithQuantity);
                    }
                }

                // Update the ArrayAdapter with the new data
                adapter.clear();
                adapter.addAll(productsInOrderList);
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onFailure(String errorMessage) {
                // Handle the failure to fetch the cart if needed
                Toast.makeText(AdminActivity.this, "Error fetching cart: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        };

        // Fetch the cart data from Firebase using the order ID
        fireBaseHelper.fetchCartsForOrder(order.getId(), cartsFetchListener);

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Get the updated values from the views
            String newName = orderNameEditText.getText().toString().trim();
            String newPriceStr = orderPriceEditText.getText().toString().trim();

            // Validate the new data (you may add more validation checks as needed)
            if (!newName.isEmpty() && !newPriceStr.isEmpty()) {
                // Convert the new price to double
                double newPrice = Double.parseDouble(newPriceStr);

                // Update the order details
                order.setFullNameOwner(newName);
                order.setTotalPrice(newPrice);

                // Get the updated product names and quantities
                HashMap<String, Integer> updatedProductsIDQuantity = new HashMap<>();
                for (int i = 0; i < orderProductsListView.getCount(); i++) {
                    String item = orderProductsListView.getItemAtPosition(i).toString();
                    String[] parts = item.split(" - Quantity: ");
                    if (parts.length == 2) {
                        String product = parts[0];
                        int quantity = Integer.parseInt(parts[1]);
                        updatedProductsIDQuantity.put(product, quantity);
                    }
                }

                // Update the products and quantities in the Order object
                //cart.setProductsIDQuantity(updatedProductsIDQuantity);

                // Perform other updates or actions if needed

                // Update the order in the database (Firebase or SQLite) based on your implementation
                fireBaseHelper.updateOrderInFireBase(order);
                //dbHelper.editCart(cart);
                dbHelper.editOrder(order);

                // Show a toast or perform any other actions to indicate successful update
                showSnackbar(this, "Order updated successfully");
                //Toast.makeText(this, "Order updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                // Handle case when any of the fields are empty
                showSnackbar(this,"Please fill in all fields");
                //Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            }
        });


        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showAllOrdersDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("All Orders");

        // Create a list of order names to display in the dialog
        List<String> orderNames = new ArrayList<>();
        for (Order order : orderListFromFB) {
            orderNames.add(order.getFullNameOwner() + " - " + order.getPhoneNumberOwner());
        }

        // Convert the list of order names to an array for the dialog
        final String[] orderNamesArray = orderNames.toArray(new String[0]);

        builder.setItems(orderNamesArray, (dialog, which) -> {
            // Get the selected order based on the index
            Order selectedOrder = orderListFromFB.get(which);

            // Show a confirmation dialog before deleting the order
            AlertDialog.Builder confirmDeleteBuilder = new AlertDialog.Builder(this);
            confirmDeleteBuilder.setTitle("Confirm Delete");
            confirmDeleteBuilder.setMessage("Are you sure you want to delete this order?");
            confirmDeleteBuilder.setPositiveButton("Delete", (confirmDialog, confirmWhich) -> {
                // Call the method to delete the order
                fireBaseHelper.deleteOrder(selectedOrder);
            });
            confirmDeleteBuilder.setNegativeButton("Cancel", (confirmDialog, confirmWhich) -> confirmDialog.dismiss());
            AlertDialog confirmDeleteDialog = confirmDeleteBuilder.create();
            confirmDeleteDialog.show();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
















    private void moveToActivity(Class<?> cls) {
        Intent i = new Intent(getApplicationContext(),  cls);
        i.putExtra("msg", "msg");
        startActivity(i);
    }
    //Get all products from firebase
    public void fetchAllProductsFromFireBase() {
        // Create an instance of the listener to handle the fetched products or errors
        FireBaseHelper.AllProductsFetchListener listener = new FireBaseHelper.AllProductsFetchListener() {
            @Override
            public void onProductFetch(List<Product> productList) {
                // Process the fetched products here
                // For example, you can update the UI with the products or do any other processing
                // productList contains the list of fetched products
                productListFromFB.clear();
                productListFromFB.addAll(productList);
                System.out.println("Fetched products: " + productList);
                for(Product p: productListFromFB){
                    dbHelper.insertProductToProductDB(p);
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle the failure here
                // For example, show an error message to the user
                System.err.println("Error fetching products: " + errorMessage);
            }
        };

        // Call the fetchAllProductsFromFireBase method with the listener
        fireBaseHelper.fetchAllProductsFromFireBase(listener);
    }
    //get all orders of all users
    public void fetchAllOrdersFromFireBase() {
        // Create an instance of the listener to handle the fetched orders or errors

        FireBaseHelper.AllOrdersFetchListener listener = new FireBaseHelper.AllOrdersFetchListener() {
            @Override
            public void onOrdersFetch(List<Order> orderList) {
                orderListFromFB.clear();
                orderListFromFB.addAll(orderList);
                System.out.println("Fetched orders: " + orderList);
                for (Order o : orderListFromFB) {
                    dbHelper.insertOrder(o);
                }
                dbHelper.printAllOrders();
            }
            @Override
            public void onFailure(String errorMessage) {

                System.err.println("Error fetching orders: " + errorMessage);
            }
        };

        // Call the fetchAllOrdersFromFireBase method with the listener
        fireBaseHelper.fetchAllOrdersFromFireBase(listener);
    }
    private void showSnackbar(Activity activity, String message) {
        // Make sure the activity is not null before showing the Snackbar
        if (activity != null) {
            View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
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



}