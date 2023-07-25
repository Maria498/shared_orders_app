package com.example.super_app.db;

import static com.example.super_app.db.entity.Cart.COLUMN_CART_DATE;
import static com.example.super_app.db.entity.Cart.COLUMN_CART_DISCOUNT;
import static com.example.super_app.db.entity.Cart.COLUMN_CART_ID;
import static com.example.super_app.db.entity.Cart.COLUMN_CART_TOTAL;
import static com.example.super_app.db.entity.Cart.COLUMN_PRODUCT_NAME;
import static com.example.super_app.db.entity.Cart.COLUMN_PRODUCT_QUANTITY;
import static com.example.super_app.db.entity.Cart.TABLE_CART;
import static com.example.super_app.db.entity.Cart.TABLE_CART_ITEM;
import static com.example.super_app.db.entity.Order.TABLE_ORDER;
import static com.example.super_app.db.entity.Product.COLUMN_PRODUCT_PRICE;
import static com.example.super_app.db.entity.Product.TABLE_PRODUCT;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.super_app.db.entity.Cart;
import com.example.super_app.db.entity.Order;
import com.example.super_app.db.entity.OrderProduct;
import com.example.super_app.db.entity.Product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ShoppingApp4.db";


    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create order table
        String CREATE_ORDER_TABLE = "CREATE TABLE " + Order.TABLE_ORDER + "("
                + Order.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Order.COLUMN_FULL_NAME + " TEXT,"
                + Order.COLUMN_PHONE_NUMBER + " TEXT,"
                + Order.COLUMN_DELIVERY_DATE + " TEXT,"
                + Order.COLUMN_ADDRESS + " TEXT,"
                + Order.COLUMN_TOTAL_PRICE + " REAL"
                + ")";
        sqLiteDatabase.execSQL(CREATE_ORDER_TABLE);
        //create product table
        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + Product.TABLE_PRODUCT + "("
                + Product.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Product.COLUMN_PRODUCT_NAME + " TEXT,"
                + Product.COLUMN_PRODUCT_PRICE + " REAL,"
                + Product.COLUMN_PRODUCT_IMAGE + " TEXT,"
                + Product.COLUMN_PRODUCT_CATEGORY + " TEXT,"
                + Product.COLUMN_PRODUCT_DISCOUNT + " INTEGER,"
                + Product.COLUMN_PRODUCT_QUANTITY + " INTEGER,"
                + Product.COLUMN_ORDER_ID + " INTEGER"
                + ")";

        sqLiteDatabase.execSQL(CREATE_PRODUCT_TABLE);
        // CREATE OrderProduct table
        String CREATE_ORDER_PRODUCT_TABLE = "CREATE TABLE " + OrderProduct.TABLE_ORDER_PRODUCT + "("
                + OrderProduct.COLUMN_ORDER_ID + " INTEGER,"
                + OrderProduct.COLUMN_PRODUCT_ID + " INTEGER,"
                + "PRIMARY KEY (" + OrderProduct.COLUMN_ORDER_ID + ", " + OrderProduct.COLUMN_PRODUCT_ID + "),"
                + "FOREIGN KEY(" + OrderProduct.COLUMN_ORDER_ID + ") REFERENCES " + Order.TABLE_ORDER + "(" + Order.COLUMN_ID + "),"
                + "FOREIGN KEY(" + OrderProduct.COLUMN_PRODUCT_ID + ") REFERENCES " + Product.TABLE_PRODUCT + "(" + Product.COLUMN_ID + ")"
                + ")";

        sqLiteDatabase.execSQL(CREATE_ORDER_PRODUCT_TABLE);

        //todo - history orders can be extracted from here
        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + COLUMN_CART_ID + " TEXT PRIMARY KEY,"
                + COLUMN_CART_DATE + " DATE,"
                + COLUMN_CART_TOTAL + " REAL,"
                + COLUMN_CART_DISCOUNT + " INTEGER"
                + ")";

        String CREATE_CART_ITEM_TABLE = "CREATE TABLE " + TABLE_CART_ITEM + "("
                + COLUMN_CART_ID + " TEXT,"
                + COLUMN_PRODUCT_NAME + " TEXT,"
                + COLUMN_PRODUCT_QUANTITY + " REAL"
                + ")";

        sqLiteDatabase.execSQL(CREATE_CART_TABLE);
        sqLiteDatabase.execSQL(CREATE_CART_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CART_ITEM);
        onCreate(sqLiteDatabase);
    }

    public void createShopping(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS shoppingCart");
        String createTableQuery = "CREATE TABLE shoppingCart (name TEXT PRIMARY KEY, price TEXT,quantity INTEGER, pic INTEGER)";
        sqLiteDatabase.execSQL(createTableQuery);
        //int apple = R.drawable.apple;

    }

    //------------ORDER-----------------------------
    public void insertOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Check if the user already has an order
        String query = "SELECT * FROM " + TABLE_ORDER + " WHERE " + Order.COLUMN_PHONE_NUMBER + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{order.getPhoneNumberOwner()});
        if (cursor.getCount() < 0) {
            // Insert the new order
            ContentValues values = new ContentValues();
            values.put(Order.COLUMN_FULL_NAME, order.getFullNameOwner());
            values.put(Order.COLUMN_PHONE_NUMBER, order.getPhoneNumberOwner());
            values.put(Order.COLUMN_DELIVERY_DATE, order.getDeliveryDate());
            values.put(Order.COLUMN_ADDRESS, order.getAddress());
            values.put(Order.COLUMN_TOTAL_PRICE, order.getTotalPrice());
            db.close();
            long id = db.insert(TABLE_ORDER, null, values);
            order.setId(id);
            Log.d("id", String.valueOf(id));
        }
        cursor.close();
    }

    public void deleteOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDER, Order.COLUMN_ID + " = ?",
                new String[]{String.valueOf(order.getId())});
        db.close();
    }

    public void editOrder(Order order) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Order.COLUMN_FULL_NAME, order.getFullNameOwner());
        values.put(Order.COLUMN_PHONE_NUMBER, order.getPhoneNumberOwner());
        values.put(Order.COLUMN_DELIVERY_DATE, order.getDeliveryDate());
        values.put(Order.COLUMN_ADDRESS, order.getAddress());
        values.put(Order.COLUMN_TOTAL_PRICE, order.getTotalPrice());
        db.update(TABLE_ORDER, values, Order.COLUMN_ID + " = ?",
                new String[]{String.valueOf(order.getId())});
        db.close();
    }

    //------------ORDER-----------------------------



    //----------CART--------------------------------
    public void addCart(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cartValues = new ContentValues();
        cartValues.put(COLUMN_CART_ID, cart.getCartId());
        cartValues.put(COLUMN_CART_DATE, cart.getDate().toString());
        cartValues.put(COLUMN_CART_TOTAL, cart.getTotal());
        cartValues.put(COLUMN_CART_DISCOUNT, cart.getDiscount());
        Cursor cursor = db.query(TABLE_CART, null, COLUMN_CART_ID + " = ?", new String[]{cart.getCartId()}, null, null, null);
        if (cursor.moveToFirst()) {
            // If cart with the given cart_id already exists, update the row
            db.update(TABLE_CART, cartValues, COLUMN_CART_ID + " = ?", new String[]{cart.getCartId()});
            cursor.close();
        } else {
            // If cart with the given cart_id doesn't exist, insert a new row
            cartValues.put(COLUMN_CART_ID, cart.getCartId());
            db.insert(TABLE_CART, null, cartValues);
            cursor.close();
        }
        //db.insert(TABLE_CART, null, cartValues);
        //insert product to cart
        HashMap<Product, Integer> productsQuantity = (HashMap<Product, Integer>) cart.getProductsQuantity();
        for (Map.Entry<Product, Integer> entry : productsQuantity.entrySet()) {
            Product product = entry.getKey();
            double quantity = entry.getValue();

            ContentValues itemValues = new ContentValues();
            itemValues.put(COLUMN_CART_ID, cart.getCartId());
            itemValues.put(COLUMN_PRODUCT_NAME, product.getName());
            itemValues.put(COLUMN_PRODUCT_QUANTITY, quantity);
            db.insert(TABLE_CART_ITEM, null, itemValues);
        }

        db.close();
    }

    // Update the getAllProductsInCart method in the DatabaseHelper class
    public List<Product> getAllProductsInCart() {
        List<Product> cartProducts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to retrieve all products in the cart along with their quantities
        String query = "SELECT " + TABLE_CART_ITEM + "." + COLUMN_PRODUCT_NAME + ", " +
                TABLE_CART_ITEM + "." + COLUMN_PRODUCT_QUANTITY + ", " +
                TABLE_PRODUCT + ".* " +
                "FROM " + TABLE_CART_ITEM +
                " JOIN " + TABLE_PRODUCT +
                " ON " + TABLE_CART_ITEM + "." + COLUMN_PRODUCT_NAME + " = " + TABLE_PRODUCT + "." + COLUMN_PRODUCT_NAME;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(COLUMN_PRODUCT_NAME);
            int quantityIndex = cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY);
            int priceIndex = cursor.getColumnIndex(COLUMN_PRODUCT_PRICE);

            do {
                String productName = cursor.getString(nameIndex);
                double productQuantity = 0;
                if (quantityIndex >= 0) {
                    productQuantity = cursor.getDouble(quantityIndex);
                }
                double productPrice = 0;
                if (priceIndex >= 0) {
                    productPrice = cursor.getDouble(priceIndex);
                }
                // ... and other product details

                // Create a new Product object and set its properties
                Product product = new Product();
                product.setName(productName);
                product.setPrice(productPrice);
                // ... and other product details

                // Set the quantity of the product in the cart
                product.setQuantity((int)productQuantity);

                cartProducts.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartProducts;
    }



    public void deleteCart(String cartId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_CART_ID + " = ?", new String[]{String.valueOf(cartId)});
        db.delete(TABLE_CART_ITEM, COLUMN_CART_ID + " = ?", new String[]{String.valueOf(cartId)});
        db.close();
    }

    public void editCart(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cartValues = new ContentValues();
        cartValues.put(String.valueOf(COLUMN_CART_DATE), cart.getDate().toString());
        cartValues.put(COLUMN_CART_TOTAL, cart.getTotal());
        cartValues.put(COLUMN_CART_DISCOUNT, cart.getDiscount());
        db.update(TABLE_CART, cartValues, COLUMN_CART_ID + " = ?",
                new String[]{String.valueOf(cart.getId())});

        HashMap<Product, Integer> productsQuantity = (HashMap<Product, Integer>) cart.getProductsQuantity();
        for (Map.Entry<Product, Integer> entry : productsQuantity.entrySet()) {
            Product product = entry.getKey();
            double quantity = entry.getValue();

            ContentValues itemValues = new ContentValues();
            itemValues.put(COLUMN_PRODUCT_QUANTITY, quantity);
            db.update(TABLE_CART_ITEM, itemValues,
                    COLUMN_CART_ID + " = ? AND " + COLUMN_PRODUCT_NAME + " = ?",
                    new String[]{String.valueOf(cart.getId()), product.getName()});
        }

        db.close();
    }
    public Cart getCartById(String cartId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_CART_ID + " = ?";
        String[] selectionArgs = {cartId};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        Cart cart = null;

        if (cursor.moveToFirst()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Replace this pattern with your actual date pattern

            @SuppressLint("Range") String dateString = cursor.getString(cursor.getColumnIndex(COLUMN_CART_DATE));
            Date cartDate = null;
            try {
                cartDate = dateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            @SuppressLint("Range") double cartTotal = cursor.getDouble(cursor.getColumnIndex(COLUMN_CART_TOTAL));
            @SuppressLint("Range") int cartDiscount = cursor.getInt(cursor.getColumnIndex(COLUMN_CART_DISCOUNT));

            cart = new Cart(cartId, cartDate, cartTotal, cartDiscount, "");
        }

        cursor.close();
        return cart;
    }


    public void printAllCarts() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CART;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String cartId = cursor.getString(cursor.getColumnIndex(COLUMN_CART_ID));
                @SuppressLint("Range") String cartDate = cursor.getString(cursor.getColumnIndex(COLUMN_CART_DATE));
                @SuppressLint("Range") double cartTotal = cursor.getDouble(cursor.getColumnIndex(COLUMN_CART_TOTAL));
                @SuppressLint("Range") int cartDiscount = cursor.getInt(cursor.getColumnIndex(COLUMN_CART_DISCOUNT));

                // You can print or process the cart details as needed
                System.out.println("Cart ID: " + cartId);
                System.out.println("Cart Date: " + cartDate);
                System.out.println("Cart Total: " + cartTotal);
                System.out.println("Cart Discount: " + cartDiscount);
                System.out.println("---------------");

            } while (cursor.moveToNext());
        }

        cursor.close();
    }
    //----------CART--------------------------------
    //-----------PRODUCT----------------------------
    public long insertProductToProductDB(Product product) {
        if (isProductExist(product)) {
            // Product already exists in the database, so return -1 (an error code) to indicate that the insertion was not successful.
            return -1;
        }
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Product.COLUMN_PRODUCT_NAME, product.getName());
        values.put(Product.COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(Product.COLUMN_PRODUCT_IMAGE, product.getImageUrl());
        values.put(Product.COLUMN_PRODUCT_CATEGORY, product.getCategory());
        values.put(Product.COLUMN_PRODUCT_DISCOUNT, product.getDiscount());
        values.put(Product.COLUMN_PRODUCT_QUANTITY, product.getQuantity());

        return db.insert(Product.TABLE_PRODUCT, null, values);
    }

    // Method to delete a product from the Product table
    public int deleteProductFromProductDB(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = Product.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(product.getId())};

        return db.delete(Product.TABLE_PRODUCT, selection, selectionArgs);
    }

    // Method to update a product in the Product table
    public int editProductFromProductDB(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Product.COLUMN_PRODUCT_NAME, product.getName());
        values.put(Product.COLUMN_PRODUCT_PRICE, product.getPrice());
        values.put(Product.COLUMN_PRODUCT_IMAGE, product.getImageUrl());
        values.put(Product.COLUMN_PRODUCT_CATEGORY, product.getCategory());
        values.put(Product.COLUMN_PRODUCT_DISCOUNT, product.getDiscount());
        values.put(Product.COLUMN_PRODUCT_QUANTITY, product.getQuantity());

        String selection = Product.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(product.getId())};

        return db.update(Product.TABLE_PRODUCT, values, selection, selectionArgs);
    }
    //isProductExist method queries the table to check if the product already exists based on its name.
    //It returns a boolean value indicating whether the product exists or not.
    private boolean isProductExist(Product product) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_PRODUCT + " WHERE " + Product.COLUMN_PRODUCT_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{product.getName()});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public void printAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Product.TABLE_PRODUCT, null);
        if (cursor.moveToFirst()) {
            do {
                // Retrieve data from the cursor
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex(Product.COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(Product.COLUMN_PRODUCT_NAME));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex(Product.COLUMN_PRODUCT_PRICE));
                // ... and so on for other columns

                // Print the data to the console or logcat
                Log.d("ProductData", "ID: " + id + ", Name: " + name + ", Price: " + price);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
    //-----------PRODUCT----------------------------
    //-----------PRODUCT&ORDER----------------------
    // Method to insert a product into an order in the OrderProduct table
    public long insertProductToOrder(Cart cart, Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OrderProduct.COLUMN_ORDER_ID, cart.getId());
        values.put(OrderProduct.COLUMN_PRODUCT_ID, product.getId());

        return db.insert(OrderProduct.TABLE_ORDER_PRODUCT, null, values);
    }

    // Method to delete a product from an order in the OrderProduct table
    public int deleteProductFromOrder(Cart cart, Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = OrderProduct.COLUMN_ORDER_ID + " = ? AND " + OrderProduct.COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(cart.getId()), String.valueOf(product.getId())};

        return db.delete(OrderProduct.TABLE_ORDER_PRODUCT, selection, selectionArgs);
    }

    // Method to update a product in an order in the OrderProduct table
    public int editProductInOrder(Cart cart, Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // Add any specific order-product related data to be updated (if any)

        String selection = OrderProduct.COLUMN_ORDER_ID + " = ? AND " + OrderProduct.COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(cart.getId()), String.valueOf(product.getId())};

        return db.update(OrderProduct.TABLE_ORDER_PRODUCT, values, selection, selectionArgs);
    }
    //-----------PRODUCT&ORDER----------------------





    // Method to edit the quantity of a product in the cart in the CartItem table


    // Insert Data into Database
    //-------------------USER-----------------------
    //todo - change next ite to firebase auth
//    public long insertUser(String name, String email,String password, String address,String date){
//        User currentUser = new User(name, email, password, address,date);
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//
//        values.put(User.COLUMN_NAME, name);
//        values.put(User.COLUMN_EMAIL, email);
//        values.put(User.COLUMN_PASSWORD,currentUser.getUserPassword());
//        values.put(User.COLUMN_ADDRESS,currentUser.getUserPassword());
//
//        try {
//            long id = db.insert(User.TABLE_NAME, null, values);
//            db.close();
//            return id;
//        } catch (SQLiteConstraintException e) {
//            db.close();
//            return -1;
//        }
//    }




    // Getting Contact from DataBase
//    public User getUser(long id){
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(User.TABLE_NAME,
//                new String[]{
//                        User.COLUMN_ID,
//                        User.COLUMN_NAME,
//                        User.COLUMN_EMAIL, User.COLUMN_PASSWORD, User.COLUMN_ADDRESS},
//                User.COLUMN_ID + "=?",
//                new String[]{
//                        String.valueOf(id)
//                },
//                null,
//                null,
//                null,
//                null);
//
//        if (cursor !=null)
//            cursor.moveToFirst();
//        User contact=null;

//        User contact = new User(
//                cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME)),
//                cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_EMAIL)),
//                cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_PASSWORD)),
//                cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_ADDRESS)));

//        cursor.close();
//        return contact;
//
//    }

    //todo - change to firebase auth
//    public ArrayList<User> getAllUsers(){
//        ArrayList<User> users = new ArrayList<>();
//
//
//        String selectQuery = "SELECT * FROM " +User.TABLE_NAME + " ORDER BY "+
//                User.COLUMN_ID + " DESC";
//        //String selectQuery = "SELECT " + User.COLUMN_EMAIL + ", " + User.COLUMN_NAME + " FROM " + User.TABLE_NAME + " ORDER BY " + User.COLUMN_ID + " DESC";
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()){
//            do{
//                User user = new User();
//                user.setUserName(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME)));
//                user.setUserEmail(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_EMAIL)));
//                user.setUserAdd(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_ADDRESS)));
//                user.setUserPassword(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_PASSWORD)));
//
//                users.add(user);
//
//            }while(cursor.moveToNext());
//        }
//
//        db.close();
//
//        return users;
//    }




//    @SuppressLint("Range")
//    public long getUserIdByEmail(String userEmail) {
//        long userId = -1;
//
//        // Define the query to search for users by email
//        String query = "SELECT " + User.COLUMN_ID +
//                " FROM " + User.TABLE_NAME +
//                " WHERE " + User.COLUMN_EMAIL + " = ?";
//
//        // Execute the query
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, new String[]{userEmail});
//
//        if (cursor.moveToFirst()) {
//            // Extract the user ID from the cursor
//            userId = cursor.getLong(cursor.getColumnIndex(User.COLUMN_ID));
//        }
//
//        // Close the cursor
//        cursor.close();
//
//        return userId;
//    }
//
//
//    @SuppressLint("Range")
//    public String getPasswordByEmail(String userEmail) {
//        String password = null;
//
//        // Define the query to search for users by email
//        String query = "SELECT " + User.COLUMN_PASSWORD +
//                " FROM " + User.TABLE_NAME +
//                " WHERE " + User.COLUMN_EMAIL + " = ?";
//
//        // Execute the query
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, new String[]{userEmail});
//
//        if (cursor.moveToFirst()) {
//            // Extract the password from the cursor
//            password = cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD));
//        }
//
//        // Close the cursor
//        cursor.close();
//
//        return password;
//    }

//    @SuppressLint("Range")
//    public String getUserNameById(int userId) {
//        String userName = null;
//
//        // Define the query to search for a user by ID
//        String query = "SELECT " + User.COLUMN_NAME +
//                " FROM " + User.TABLE_NAME +
//                " WHERE " + User.COLUMN_ID + " = ?";
//
//        // Execute the query
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
//
//        if (cursor.moveToFirst()) {
//            // Extract the user name from the cursor
//            userName = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME));
//        }
//
//        // Close the cursor
//        cursor.close();
//
//        return userName;
//    }

    //-------------------USER-----------------------









}
