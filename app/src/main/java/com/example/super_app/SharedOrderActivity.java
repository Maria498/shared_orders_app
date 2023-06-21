package com.example.super_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import com.example.super_app.db.entity.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private String userStreet, userApart, selectedDate, userName, phoneNum;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    boolean isOpen=false;

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

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

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
            }
        });

        setDate(dateCal);

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = fullName.getText().toString().trim();
                phoneNum = phoneNumber.getText().toString().trim();
                userStreet = street.getText().toString().trim();
                userApart = apartmentNum.getText().toString().trim();

                if (userName.isEmpty() || userName.length() < 3 || !userName.matches("[a-zA-Z\\s]+")) {
                    Toast.makeText(SharedOrderActivity.this, "INVALID NAME", Toast.LENGTH_SHORT).show();
                } else if (phoneNum.isEmpty() || phoneNum.length() < 9) {
                    Toast.makeText(SharedOrderActivity.this, "INVALID PHONE Number", Toast.LENGTH_SHORT).show();

                } else if (spinnerCity == null || spinnerCity.getSelectedItem() == null || spinnerCity.getSelectedItem().equals("Filter By city") || spinnerCity.getSelectedItem().toString().isEmpty()) {
                    Toast.makeText(SharedOrderActivity.this, "Please choose a city", Toast.LENGTH_SHORT).show();
                } else if (userStreet.isEmpty() || !userStreet.matches("[a-zA-Z\\s]+"))
                {
                    street.setError("Street is required");
                    street.requestFocus();
                } else if (userApart.isEmpty() || !userApart.matches("\\d+")) {
                    apartmentNum.setError("Apartment number is required");
                    apartmentNum.requestFocus();
                } else if (selectedDate == null || !isDateWithinOneWeek(selectedDate)) {
                    Toast.makeText(SharedOrderActivity.this, "Please select a valid date within one week", Toast.LENGTH_SHORT).show();

                } else {
                    String uid = mAuth.getUid();
                    db.collection("Orders").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.getId().equals(uid))
                                    {
                                        Toast.makeText(SharedOrderActivity.this, "You already have an open order", Toast.LENGTH_SHORT).show();
                                        isOpen=true;
                                    }
                                }
                                if(!isOpen)
                                {
                                    String address=spinnerCity.getSelectedItem().toString()+","+userStreet+","+userApart;
                                    Order order=new Order(userName,phoneNum,address,selectedDate);
                                    db.collection("Orders").document(uid).set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(SharedOrderActivity.this, "An order has been opened that you own", Toast.LENGTH_SHORT).show();
                                            Intent intent=new Intent(SharedOrderActivity.this,SuperCategoryActivity.class);
                                            intent.putExtra("typeOfUser","Owner");
                                            startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SharedOrderActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            }
                        }

                    });

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
            return (differenceInDays <= 7&&differenceInDays>=1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
