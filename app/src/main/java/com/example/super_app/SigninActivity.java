package com.example.super_app;

import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SigninActivity extends AppCompatActivity {

    private EditText userName;

    private EditText email;
    private EditText password;
    private EditText rePassword;
    private TextView dateCal;
    private EditText street;
    private EditText apartmentNum;
    private Spinner city;
    private Context context;

    private FirebaseAuth mAuth;

    private String userName1;
    private String date ;

    private HashMap<String, Object> userMap = new HashMap<>();
    private FireBaseHelper fireBaseHelper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mAuth = FirebaseAuth.getInstance();
        context = getApplicationContext();
        fireBaseHelper = new FireBaseHelper(this);

        Button signInBtn = findViewById(R.id.signInBtn);
        ImageView backBtn = findViewById(R.id.backBtn);
        userName = findViewById(R.id.userName);
        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.editTextTextPassword);
        rePassword = findViewById(R.id.editTextRePassword);
        dateCal = findViewById(R.id.dateCal);
        city = findViewById(R.id.spinnerCity);
        street = findViewById(R.id.Street);
        apartmentNum = findViewById(R.id.ApartmentNum);
        backBtn.setOnClickListener(v -> moveToActivity(LoginActivity.class));
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            currentUser.reload();
        }
        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        dateCal.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog pickerDialog = new DatePickerDialog(SigninActivity.this, (datePicker, year1, monthOfYear, dayOfMonth) -> {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(Calendar.YEAR, year1);
                selectedDate.set(Calendar.MONTH, monthOfYear);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Calendar currentDate = Calendar.getInstance();

                if (selectedDate.getTimeInMillis() > currentDate.getTimeInMillis()) {
                    // Selected date is in the future
                    Toast.makeText(SigninActivity.this, "Please select a valid birth date.", Toast.LENGTH_SHORT).show();
                } else {
                    dateCal.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1);
                    date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                }
            }, year, month, day);
            pickerDialog.show();

        });

        setDate(dateCal);
        signInBtn.setOnClickListener(v -> saveUserToDB());
    }


    private void saveUserToDB() {
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        userName1 = userName.getText().toString();
        String userRePassword = rePassword.getText().toString();
        String userStreet = street.getText().toString();
        String userApart = apartmentNum.getText().toString();

        if (userName1.isEmpty() || userName1.length() < 1 || !userName1.matches("[a-zA-Z\\s]+")) {
            userName.setError("User name is required");
            userName.requestFocus();
            return;
            // Stop further processing
        }
        if (userEmail.isEmpty() || (!(userEmail.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$")))) {
            email.setError("Email is required");
            email.requestFocus();
            return; // Stop further processing
        }
        if (userPassword.isEmpty() || userRePassword.isEmpty()&& userPassword.length()<7) {
            password.setError("Password is required and should contain 7 characters");
            password.requestFocus();
            return;
        }
        if (!userRePassword.equals(userPassword)) {
            rePassword.setError("password dont match!");
            rePassword.requestFocus();
            return;
        }
       if (city == null || city.getSelectedItem() == null ||city.getSelectedItem().equals("Filter By city")|| city.getSelectedItem().toString().isEmpty()) {
            Toast.makeText(context, "Please choose a city", Toast.LENGTH_SHORT).show();
            return;
        }
       if (userStreet.isEmpty() || !userStreet.matches("[a-zA-Z]+")) {
            street.setError("Street is required");
            street.requestFocus();
            return;
        }
        if (userApart.isEmpty()|| !userApart.matches("\\d+")) {
            apartmentNum.setError("Apartment num  is required");
            apartmentNum.requestFocus();
            return;
        }
        if(!checkDate())
        {
            dateCal.setError("Sorry, you must be 18 years or older to register");
            dateCal.requestFocus();
            return;
        }

        String userAdd = city.getSelectedItem().toString() + "," + userStreet + "," + userApart;
        User user2 = new User(userName1,userEmail, userAdd,date);
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        Toast.makeText(getApplicationContext(), "createUserWithEmail:success", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        //updating name of user
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userName1)
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task12 -> {
                                    if (task12.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");

                                    } else
                                        Log.d(TAG, "User profile update failed.");
                                });

                        fireBaseHelper.addUserData(user2);
                        //email sanding
                        user.sendEmailVerification()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                        String userName = FireBaseHelper.getCurrentUser();
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        i.putExtra("USER_NAME", userName);
                                        startActivity(i);
                                    } else
                                        Log.d(TAG, "Email not sent.");
                                });

                    }
                });
    }
    private void moveToActivity(Class<?> cls) {

        Intent i = new Intent(getApplicationContext(), cls);
        i.putExtra("msg", "Data collector");
        startActivity(i);

    }
    private String setDate(TextView dateCal) {
        Date date1 = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy");
        String date = format.format(date1);
        dateCal.setText(date);
        return date;

    }
    //check if the age is above 18
    public boolean checkDate() {
        String[] splitDate = date.split("/");
        int day = Integer.parseInt(splitDate[0]);
        int month = Integer.parseInt(splitDate[1]);
        int year = Integer.parseInt(splitDate[2]);

        Calendar currentDate = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.set(year, month - 1, day); // Month is 0-based in Calendar

        int age = currentDate.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // Check if the birthdate has already occurred this year
        if (currentDate.get(Calendar.MONTH) < birthDate.get(Calendar.MONTH) ||
                (currentDate.get(Calendar.MONTH) == birthDate.get(Calendar.MONTH) &&
                        currentDate.get(Calendar.DAY_OF_MONTH) < birthDate.get(Calendar.DAY_OF_MONTH))) {
            age--;
        }
        // Check if the age is at least 18
        return age >= 18;
    }

}