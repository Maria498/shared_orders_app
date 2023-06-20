package com.example.super_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class SharedOrderActivity extends AppCompatActivity {

    private ImageView backgroundImageView;
    private TextView dateCal;
    private EditText fullName;
    private EditText phoneNumber;
    private Spinner spinnerCity;
    private EditText street;
    private EditText apartmentNum;
    private Button continueBtn;
    private String userStreet;
    private String userApart;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_order);

        backgroundImageView = findViewById(R.id.backgroundImageView);
        dateCal = findViewById(R.id.dateCal);
        fullName = findViewById(R.id.fullName);
        phoneNumber = findViewById(R.id.phoneNumber);
        spinnerCity = findViewById(R.id.spinnerCity);
        street = findViewById(R.id.Street);
        apartmentNum = findViewById(R.id.ApartmentNum);
        continueBtn = findViewById(R.id.continueBtn);

        dateCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog pickerDialog = new DatePickerDialog(SharedOrderActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        dateCal.setText(selectedDate);
                    }
                }, year, month, day);

                // Set the minimum date to today
                pickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

                // Set the maximum date to one week from today
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                long maxDate = calendar.getTimeInMillis();
                pickerDialog.getDatePicker().setMaxDate(maxDate);

                pickerDialog.show();
            }
        });

        setDate(dateCal);
        userStreet = street.getText().toString();
        userApart = apartmentNum.getText().toString();

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerCity == null || spinnerCity.getSelectedItem() == null || spinnerCity.getSelectedItem().equals("Filter By city") || spinnerCity.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(SharedOrderActivity.this, "Please choose a city", Toast.LENGTH_SHORT).show();
                } else if (userStreet.isEmpty() || !userStreet.matches("[a-zA-Z]+")) {
                    street.setError("Street is required");
                    street.requestFocus();
                } else if (userApart.isEmpty() || !userApart.matches("\\d+")) {
                    apartmentNum.setError("Apartment number is required");
                    apartmentNum.requestFocus();
                } else if (selectedDate != null && isDateWithinOneWeek(selectedDate)) {
                    // Proceed with the order
                    Toast.makeText(SharedOrderActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SharedOrderActivity.this, "Please select a valid date within one week", Toast.LENGTH_SHORT).show();
                }
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
            return differenceInDays <= 7;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
