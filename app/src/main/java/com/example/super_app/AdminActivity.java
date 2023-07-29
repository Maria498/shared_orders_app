package com.example.super_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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
import android.widget.Spinner;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.Order;
import com.example.super_app.db.entity.Product;

import java.util.ArrayList;
import java.util.List;

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
    private Button updateProductBtn;
    private Button createProductBtn;
    private Button backBtn;
    private Button deleteProductBtn;

    //todo - add deletion logic
    //todo - add edit logic

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        fireBaseHelper = new FireBaseHelper(this);
        dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        productListFromFB = new ArrayList<>();

        //fetch data from firebase to sqlite
        fetchAllProductsFromFireBase();
        fetchAllOrdersFromFireBase();

        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> moveToActivity(MainActivity.class));
        createProductBtn = findViewById(R.id.createProductBtn);
        createProductBtn.setOnClickListener( v -> showAddProductDialog());

        updateProductBtn = findViewById(R.id.updateProductBtn);
        updateProductBtn.setOnClickListener(v -> showAllProducts());

        deleteProductBtn = findViewById(R.id.deleteProductBtn);
        deleteProductBtn.setOnClickListener(v->showAllProductsDelete());
        //dbHelper.printAllProducts();
    }
    protected void onResume() {
        super.onResume();
        // Fetch the updated data from Firebase
        fetchAllProductsFromFireBase();
        fetchAllOrdersFromFireBase();
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
        List<Order> orderListFromFB = new ArrayList<>();
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


}