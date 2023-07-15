package com.example.super_app.db;



import static com.example.super_app.db.entity.Order.TABLE_ORDER;
import static com.example.super_app.db.entity.Product.TABLE_PRODUCT;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


import com.example.super_app.LoginActivity;
import com.example.super_app.R;
import com.example.super_app.db.entity.Order;
import com.example.super_app.db.entity.Product;
import com.example.super_app.db.entity.User;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ShoppingApp1";


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
        String CREATE_PRODUCT_TABLE = "CREATE TABLE " + TABLE_PRODUCT + "("
                + Product.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Product.COLUMN_PRODUCT_NAME + " TEXT,"
                + Product.COLUMN_PRODUCT_PRICE + " REAL,"
                + Product.COLUMN_PRODUCT_IMAGE + " TEXT,"
                + Product.COLUMN_PRODUCT_CATEGORY + " TEXT,"
                + Product.COLUMN_PRODUCT_DESCRIPTION + " TEXT,"
                + Product.COLUMN_PRODUCT_DISCOUNT + " INTEGER,"
                + Product.COLUMN_ORDER_ID + " INTEGER,"
                + "FOREIGN KEY(" + Product.COLUMN_ORDER_ID + ") REFERENCES " + TABLE_ORDER + "(" + Product.COLUMN_ID + ")"
                + ")";
        sqLiteDatabase.execSQL(CREATE_PRODUCT_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        onCreate(sqLiteDatabase);

    }


    public void createShopping(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS shoppingCart");
        String createTableQuery = "CREATE TABLE shoppingCart (name TEXT PRIMARY KEY, price TEXT,quantity INTEGER, pic INTEGER)";
        sqLiteDatabase.execSQL(createTableQuery);
        int apple = R.drawable.apple;

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

    //-----------PRODUCT----------------------------
    public void insertProduct(Product product, long orderId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the product already exists
        if (isProductExist(product)) {
            db.close();
        } else {
            // Product does not exist, insert a new product
            ContentValues values = new ContentValues();
            values.put(Product.COLUMN_PRODUCT_NAME, product.getName());
            values.put(Product.COLUMN_PRODUCT_PRICE, product.getPrice());
            values.put(Product.COLUMN_PRODUCT_IMAGE, product.getImg());
            values.put(Product.COLUMN_PRODUCT_CATEGORY, product.getCategory());
            values.put(Product.COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
            values.put(Product.COLUMN_PRODUCT_DISCOUNT, product.getDiscount());
            values.put(Product.COLUMN_ORDER_ID, orderId);
            long id = db.insert(TABLE_PRODUCT, null, values);
            db.close();
            product.setId(id);
        }
    }

    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the product exists before deleting
        if (isProductExist(product)) {
            db.delete(TABLE_PRODUCT, Product.COLUMN_PRODUCT_NAME + " = ?",
                    new String[]{product.getName()});
        }

        db.close();
    }

    public void editProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the product exists before editing
        if (isProductExist(product)) {
            ContentValues values = new ContentValues();
            values.put(Product.COLUMN_PRODUCT_NAME, product.getName());
            values.put(Product.COLUMN_PRODUCT_PRICE, product.getPrice());
            values.put(Product.COLUMN_PRODUCT_IMAGE, product.getImg());
            values.put(Product.COLUMN_PRODUCT_CATEGORY, product.getCategory());
            values.put(Product.COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
            values.put(Product.COLUMN_PRODUCT_DISCOUNT, product.getDiscount());
            db.update(TABLE_PRODUCT, values, Product.COLUMN_PRODUCT_NAME + " = ?",
                    new String[]{product.getName()});
        }

        db.close();
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

    //-----------PRODUCT----------------------------

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
