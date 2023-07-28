package com.example.super_app;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.super_app.db.FireBaseHelper;
import com.example.super_app.db.entity.Cart;
import com.example.super_app.db.entity.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CreateNewOrderActivity extends AppCompatActivity {

    private TextView dateCal;
    private EditText fullName;
    private EditText phoneNumber;
    private Spinner spinnerCity;
    private EditText street;
    private EditText apartmentNum;
    private String userStreet, userApart, selectedDate, userName, phoneNum;
    boolean isOpen=false;
    FireBaseHelper fireBaseHelper;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_order);
        CheckBox addCartCheckBox = findViewById(R.id.addCartCheckBox);
        fireBaseHelper = new FireBaseHelper(this);
        dateCal = findViewById(R.id.dateCal);
        fullName = findViewById(R.id.fullName);
        phoneNumber = findViewById(R.id.phoneNumber);
        spinnerCity = findViewById(R.id.spinnerCity);
        street = findViewById(R.id.Street);
        apartmentNum = findViewById(R.id.ApartmentNum);
        Button createOrderBtn = findViewById(R.id.createOrderBtn);
        BottomNavigationView menu = findViewById(R.id.menu);

        menu.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.cart:
                    startActivity(new Intent(CreateNewOrderActivity.this, CartActivity.class));
                    finish();
                    return true;
                case R.id.profile:
                    startActivity(new Intent(CreateNewOrderActivity.this, ProfileActivity.class));
                    finish();
                    return true;
                case R.id.home:
                    startActivity(new Intent(CreateNewOrderActivity.this, MainActivity.class));
                    finish();
                    return true;
            }
            return false;
        });
        dateCal.setOnClickListener(view -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog pickerDialog = new DatePickerDialog(CreateNewOrderActivity.this, (datePicker, year1, monthOfYear, dayOfMonth) -> {
                selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                dateCal.setText(selectedDate);
            }, year, month, day);

            // Get tomorrow's date
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            long tomorrowInMillis = calendar.getTimeInMillis();

            // Set the minimum date to tomorrow
            pickerDialog.getDatePicker().setMinDate(tomorrowInMillis);

            // Set the maximum date to one week from today
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
            long maxDate = calendar.getTimeInMillis();
            pickerDialog.getDatePicker().setMaxDate(maxDate);
            pickerDialog.show();
        });

        setDate(dateCal);

        createOrderBtn.setOnClickListener(v -> {
            userName = fullName.getText().toString().trim();
            phoneNum = phoneNumber.getText().toString().trim();
            userStreet = street.getText().toString().trim();
            userApart = apartmentNum.getText().toString().trim();

            if (userName.isEmpty() || userName.length() < 3 || !userName.matches("[a-zA-Z\\s]+")) {
                Toast.makeText(CreateNewOrderActivity.this, "INVALID NAME", Toast.LENGTH_SHORT).show();
            } else if (phoneNum.isEmpty() || phoneNum.length() < 9) {
                Toast.makeText(CreateNewOrderActivity.this, "INVALID PHONE Number", Toast.LENGTH_SHORT).show();

            } else if (spinnerCity == null || spinnerCity.getSelectedItem() == null || spinnerCity.getSelectedItem().equals("Filter By city") || spinnerCity.getSelectedItem().toString().isEmpty()) {
                Toast.makeText(CreateNewOrderActivity.this, "Please choose a city", Toast.LENGTH_SHORT).show();
            } else if (userStreet.isEmpty() || !userStreet.matches("[a-zA-Z\\s]+"))
            {
                street.setError("Street is required");
                street.requestFocus();
            } else if (userApart.isEmpty() || !userApart.matches("\\d+")) {
                apartmentNum.setError("Apartment number is required");
                apartmentNum.requestFocus();
            } else if (selectedDate == null || !isDateWithinOneWeek(selectedDate)) {
                Toast.makeText(CreateNewOrderActivity.this, "Please select a valid date within one week", Toast.LENGTH_SHORT).show();

            } else {
                String address = spinnerCity.getSelectedItem().toString() + ", " + userStreet + ", " + userApart;
                boolean shouldAddCart = addCartCheckBox.isChecked();

                // Add the new order to Firebase
                fireBaseHelper.addNewOrderToFirebase(CreateNewOrderActivity.this, userName, phoneNum, selectedDate, address, shouldAddCart);
            }


        });
    }

    private void setDate(TextView dateCal) {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy");
        String date = format.format(currentDate);
        selectedDate = date;
        dateCal.setText(date);
    }

    private boolean isDateWithinOneWeek(String date) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        try {
            Date selectedDate = format.parse(date);
            Date currentDate = Calendar.getInstance().getTime();
            long difference = selectedDate.getTime() - currentDate.getTime();
            long differenceInDays = TimeUnit.MILLISECONDS.toDays(difference);
            return (differenceInDays <= 7&&differenceInDays>=1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}