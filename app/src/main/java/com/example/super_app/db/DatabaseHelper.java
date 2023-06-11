package com.example.super_app.db;



import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.super_app.R;
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
        sqLiteDatabase.execSQL(User.CREATE_TABLE);



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }


    public void createShopping(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS shoppingCart");
        String createTableQuery = "CREATE TABLE shoppingCart (name TEXT PRIMARY KEY, price TEXT,quantity INTEGER, pic INTEGER)";
        sqLiteDatabase.execSQL(createTableQuery);
        int apple = R.drawable.apple;

    }

    // Insert Data into Database
    //-------------------USER-----------------------
    //todo - change next ite to firebase auth
    public long insertUser(String name, String email,String password, String address,String date){
        User currentUser = new User(name, email, password, address,date);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(User.COLUMN_NAME, name);
        values.put(User.COLUMN_EMAIL, email);
        values.put(User.COLUMN_PASSWORD,currentUser.getPassword());
        values.put(User.COLUMN_ADDRESS,currentUser.getPassword());

        try {
            long id = db.insert(User.TABLE_NAME, null, values);
            db.close();
            return id;
        } catch (SQLiteConstraintException e) {
            db.close();
            return -1;
        }
    }




    // Getting Contact from DataBase
    public User getUser(long id){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(User.TABLE_NAME,
                new String[]{
                        User.COLUMN_ID,
                        User.COLUMN_NAME,
                        User.COLUMN_EMAIL, User.COLUMN_PASSWORD, User.COLUMN_ADDRESS},
                User.COLUMN_ID + "=?",
                new String[]{
                        String.valueOf(id)
                },
                null,
                null,
                null,
                null);

        if (cursor !=null)
            cursor.moveToFirst();
        User contact=null;

//        User contact = new User(
//                cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME)),
//                cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_EMAIL)),
//                cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_PASSWORD)),
//                cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_ADDRESS)));

        cursor.close();
        return contact;

    }

    //todo - change to firebase auth
    public ArrayList<User> getAllUsers(){
        ArrayList<User> users = new ArrayList<>();


        String selectQuery = "SELECT * FROM " +User.TABLE_NAME + " ORDER BY "+
                User.COLUMN_ID + " DESC";
        //String selectQuery = "SELECT " + User.COLUMN_EMAIL + ", " + User.COLUMN_NAME + " FROM " + User.TABLE_NAME + " ORDER BY " + User.COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()){
            do{
                User user = new User();
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_EMAIL)));
                user.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_ADDRESS)));
                user.setPassword(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_PASSWORD)));

                users.add(user);

            }while(cursor.moveToNext());
        }

        db.close();

        return users;
    }




    @SuppressLint("Range")
    public long getUserIdByEmail(String userEmail) {
        long userId = -1;

        // Define the query to search for users by email
        String query = "SELECT " + User.COLUMN_ID +
                " FROM " + User.TABLE_NAME +
                " WHERE " + User.COLUMN_EMAIL + " = ?";

        // Execute the query
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{userEmail});

        if (cursor.moveToFirst()) {
            // Extract the user ID from the cursor
            userId = cursor.getLong(cursor.getColumnIndex(User.COLUMN_ID));
        }

        // Close the cursor
        cursor.close();

        return userId;
    }


    @SuppressLint("Range")
    public String getPasswordByEmail(String userEmail) {
        String password = null;

        // Define the query to search for users by email
        String query = "SELECT " + User.COLUMN_PASSWORD +
                " FROM " + User.TABLE_NAME +
                " WHERE " + User.COLUMN_EMAIL + " = ?";

        // Execute the query
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{userEmail});

        if (cursor.moveToFirst()) {
            // Extract the password from the cursor
            password = cursor.getString(cursor.getColumnIndex(User.COLUMN_PASSWORD));
        }

        // Close the cursor
        cursor.close();

        return password;
    }

    @SuppressLint("Range")
    public String getUserNameById(int userId) {
        String userName = null;

        // Define the query to search for a user by ID
        String query = "SELECT " + User.COLUMN_NAME +
                " FROM " + User.TABLE_NAME +
                " WHERE " + User.COLUMN_ID + " = ?";

        // Execute the query
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            // Extract the user name from the cursor
            userName = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME));
        }

        // Close the cursor
        cursor.close();

        return userName;
    }

    //-------------------USER-----------------------









}
