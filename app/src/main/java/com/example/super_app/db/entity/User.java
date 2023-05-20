package com.example.super_app.db.entity;

public class User {

    //constants for Database
    public static final String TABLE_NAME = "Users";
    public static final String COLUMN_ID = "user_id";
    public static final String COLUMN_NAME = "user_name";
    public static final String COLUMN_EMAIL ="user_email";
    public static final String COLUMN_PASSWORD ="user_password";

    // var
    private String name;
    private String email;
    private int id;
    private String password;

    public User() {

    }

    public User(String name, String email, int id) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.password = null;

    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.password = null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }

    //SQL QUERY - Creating table

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME +
                    " TEXT, " + COLUMN_EMAIL + " TEXT UNIQUE, " + COLUMN_PASSWORD + " TEXT" + ")";

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", id=" + id +
                ", password='" + password + '\'' +
                '}';
    }
}
