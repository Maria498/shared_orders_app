package com.example.super_app;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.super_app.db.DatabaseHelper;
import com.example.super_app.db.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class SigninActivity extends AppCompatActivity {

    private ImageView backBtn;
    private Button signInBtn;
    private EditText userName;

    private EditText email;
    private EditText password;
    private EditText rePassword;
    TextView dateCal;
    private EditText street;
    private EditText apartmentNum;
    private Spinner city;
    private Context context;
    private ArrayList<User> usersFromDB;
    private DatabaseHelper db;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private String userEmail, userPassword, userRePassword, userName1, userAdd, userStreet, userApart,date ;
    private User user;
    private HashMap<String, Object> userMap = new HashMap<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mAuth = FirebaseAuth.getInstance();
        context = getApplicationContext();

        signInBtn = findViewById(R.id.signInBtn);
        backBtn = findViewById(R.id.backBtn);
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.editTextTextPassword);
        rePassword = findViewById(R.id.editTextRePassword);
        dateCal = findViewById(R.id.dateCal);
        city = findViewById(R.id.spinnerCity);
        street = findViewById(R.id.Street);
        apartmentNum = findViewById(R.id.ApartmentNum);
        backBtn.setOnClickListener(v -> moveToActivity(LoginActivity.class));
        dateCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog pickerDialog = new DatePickerDialog(SigninActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        dateCal.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    }
                }, year, month, day);
                pickerDialog.show();
            }
        });

        setDate(dateCal);

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addUserToDB())
                {
                mAuth.createUserWithEmailAndPassword(userEmail,userPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        firestore = FirebaseFirestore.getInstance();
                        firestore.collection("users").document(mAuth.getUid()).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(SigninActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SigninActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                        });
                    }
                });
            }
            }
        });



    }

    private boolean addUserToDB() {
        userEmail = email.getText().toString();
        userPassword = password.getText().toString();
        userName1 = userName.getText().toString();
        userRePassword = rePassword.getText().toString();
        userStreet=street.getText().toString();
        userApart=apartmentNum.getText().toString();

        if (userName1.isEmpty() || userName1.length() < 1 || !userName1.matches("[a-zA-Z]+")) {
            userName.setError("User name is required");
            userName.requestFocus();
            return false;// Stop further processing
        }
        else if (userEmail.isEmpty() || (!(userEmail.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")))) {
            email.setError("Email is required");
            email.requestFocus();
            return false; // Stop further processing
        }
        else if (userPassword.isEmpty() || userRePassword.isEmpty()&&userPassword.length()<7) {
            password.setError("Password is required and should contain 7 characters");
            password.requestFocus();
            return false;
        }
        else if (!userRePassword.equals(userPassword)) {
            rePassword.setError("password dont match!");
            rePassword.requestFocus();
            return false;
        }
        else if (city == null || city.getSelectedItem() == null ||city.getSelectedItem().equals("Filter By city")|| city.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(context, "Please choose a city", Toast.LENGTH_SHORT).show();
            return false;
        }
       else if (userStreet.isEmpty() || !userName1.matches("[a-zA-Z]+")) {
            street.setError("Street is required");
            street.requestFocus();
            return false;
        }
        else if (userApart.isEmpty()|| !userApart.matches("\\d+")) {
            apartmentNum.setError("Apartment num  is required");
            apartmentNum.requestFocus();
            return false;
        }
        else if(!checkDate())
        {
            dateCal.setError("Invalid Date");
            dateCal.requestFocus();
            return false;
        }
        else {
            userAdd = city.getSelectedItem().toString() + "," +userStreet + "," + userApart;
            user = new User(userName1, userEmail, userPassword, userAdd,date);
            userMap.put("userName", userName1);
            userMap.put("userEmail", userEmail);
            userMap.put("userPassword", userPassword);
            userMap.put("userAdd", userAdd);
            userMap.put("birthdate",date );

            return true;
        }

    }

    private void moveToActivity(Class<?> cls) {

        Intent i = new Intent(getApplicationContext(), cls);
        i.putExtra("msg", "Data collector");
        startActivity(i);

    }
    private String setDate(TextView dateCal) {
        Date hrini = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy");
        String date = format.format(hrini);
        dateCal.setText(date);
        return date;

    }

    public boolean checkDate() {
        String[] splitDate = date.split("/");
        String year = splitDate[2];
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (currentYear - Integer.parseInt(year) < 18 && currentYear - Integer.parseInt(year) > 120) {
            return false;
        }
        return true;
    }
}