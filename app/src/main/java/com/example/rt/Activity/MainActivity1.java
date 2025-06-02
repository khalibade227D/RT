package com.example.rt.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;

import com.example.rt.Model.Location;
import com.example.rt.R;
import com.example.rt.databinding.ActivityMain1Binding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import android.util.Log;
public class MainActivity1 extends BaseActivity2 {
    private ActivityMain1Binding binding;
    private int adultPassenger = 1, childPassenger = 1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM, yyyy", Locale.ENGLISH);
    private Calendar calendar = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // Verify Firebase initialization
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                Log.e("Firebase", "Not initialized! Check Application class");
                finish(); // Close app if Firebase isn't working
                return;
            }

            database = FirebaseDatabase.getInstance();
            // Rest of your onCreate code...
        } catch (Exception e) {
            Log.e("Firebase", "Initialization error", e);
            // Show error to user and exit
            finish();
        }

        binding = ActivityMain1Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

            initLocations();
            initPassengers();
            initClassSeat();
            initDatePickup();
            setVariable();
        }

    private void setVariable() {
        binding.searchBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity1.this,SearchActivity.class);
            intent.putExtra("from",((Location)binding.fromSp.getSelectedItem()).getName());
            intent.putExtra("to",((Location)binding.toSp.getSelectedItem()).getName());
            intent.putExtra("date",binding.departureDateTxt.getText().toString());
            intent.putExtra("numPassenger",adultPassenger+childPassenger);
            startActivity(intent);
        });
    }

    private void initDatePickup() {
        Calendar calendarToday = Calendar.getInstance();
        String currentDate = dateFormat.format(calendarToday.getTime());
        binding.departureDateTxt.setText(currentDate);

        Calendar calendarTommorow = Calendar.getInstance();
        calendarTommorow.add(Calendar.DAY_OF_YEAR, 1);
        String tommorowDate = dateFormat.format(calendarTommorow.getTime());
        binding.returnDateTxt.setText(tommorowDate);

        binding.departureDateTxt.setOnClickListener(v -> showDatePickerDialog(binding.departureDateTxt));
        binding.returnDateTxt.setOnClickListener(v -> showDatePickerDialog(binding.returnDateTxt));


    }

    private void initClassSeat() {
        binding.progressBarClass.setVisibility(View.VISIBLE);
        ArrayList<String> list = new ArrayList<>();
        list.add("Business Class");
        list.add("First Class");
        list.add("Economy Class");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity1.this,R.layout.sp_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.classSp.setAdapter(adapter);
        binding.progressBarClass.setVisibility(View.GONE);
    }

    private void initPassengers() {
        binding.plusAdultBtn.setOnClickListener(v -> {
        adultPassenger++;
        binding.AdultTxt.setText(adultPassenger+" Adult");
        });
        binding.minusAdultBtn.setOnClickListener(v -> {
                if(adultPassenger>1 ){
                    adultPassenger--;
                    binding.AdultTxt.setText(adultPassenger +" Adult");
                }
        });

        binding.plusChildBtn.setOnClickListener(v -> {
        childPassenger++;
        binding.childTxt.setText(childPassenger+" Child");

        });
        binding.minusChildBtn.setOnClickListener(v -> {
            if(childPassenger>0 ) {
                childPassenger--;
                binding.childTxt.setText(childPassenger + " Child");
            }
        });
    }

    private void initLocations() {
            binding.progressBarFrom.setVisibility(View.VISIBLE);
            binding.progressBarTo.setVisibility(View.VISIBLE);
            DatabaseReference myRef = database.getReference("Locations");
            ArrayList<Location> list = new ArrayList<>();
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue:snapshot.getChildren() ){
                            list.add(issue.getValue(Location.class));
                        }
                        ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity1.this,R.layout.sp_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.fromSp.setAdapter(adapter);
                    binding.toSp.setAdapter(adapter);
                    binding.fromSp.setSelection(1);
                    binding.progressBarFrom.setVisibility(View.GONE);
                    binding.progressBarTo.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
    }

    private void showDatePickerDialog(TextView textView){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,(view,selectedYear,selectedMonth,selectedDay)->{
            calendar.set(selectedYear,selectedMonth,selectedDay);
            String formattedDate = dateFormat.format(calendar.getTime());
            textView.setText(formattedDate);
        },year,month,day);
        datePickerDialog.show();

    }

}
