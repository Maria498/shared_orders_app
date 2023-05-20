package com.example.super_app.db;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;


import androidx.annotation.RequiresApi;

import com.example.super_app.db.entity.User;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ShoppingApp";


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



    // Insert Data into Database

    public long insertUser(String name, String email){
        User currentUser = new User(name, email);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        values.put(User.COLUMN_NAME, name);
        values.put(User.COLUMN_EMAIL, email);
        values.put(User.COLUMN_PASSWORD,currentUser.getPassword());
        try {
            long id = db.insert(User.TABLE_NAME, null, values);
            db.close();
            return id;
        } catch (SQLiteConstraintException e) {
            db.close();
            return -1;
        }




    }

    //todo - insert only distinct email values


    // Getting Contact from DataBase
    public User getUser(long id){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(User.TABLE_NAME,
                new String[]{
                        User.COLUMN_ID,
                        User.COLUMN_NAME,
                        User.COLUMN_EMAIL},
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

        User contact = new User(
                cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_EMAIL)),
                cursor.getInt(cursor.getColumnIndexOrThrow(User.COLUMN_ID))
        );

        cursor.close();
        return contact;

    }

    // Getting all Contacts
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
                user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(User.COLUMN_ID)));
                user.setName(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(User.COLUMN_EMAIL)));

                users.add(user);

            }while(cursor.moveToNext());
        }

        db.close();

        return users;
    }



    public int updateUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.COLUMN_NAME, user.getName());
        values.put(User.COLUMN_EMAIL, user.getEmail());

        return db.update(User.TABLE_NAME, values,User.COLUMN_ID+ " = ? ",
                new String[]{String.valueOf(user.getId())});

    }

    public void deleteUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(User.TABLE_NAME, User.COLUMN_ID + " = ?",
                new String[]{String.valueOf(user.getId())}
        );
        db.close();
    }

    public void deleteAllUsers(){
        ArrayList<User>users =getAllUsers();
        if(!users.isEmpty()) {
            for(User user: users){
                deleteUser(user);
            }
        }
    }





}
