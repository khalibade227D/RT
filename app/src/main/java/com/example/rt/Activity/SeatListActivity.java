package com.example.rt.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.rt.Adapter.FlightAdapter;
import com.example.rt.Adapter.SeatAdapter;
import com.example.rt.Model.Flight;
import com.example.rt.Model.Seat;
import com.example.rt.R;
import com.example.rt.databinding.ActivitySeatListBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatListActivity extends BaseActivity2{
    private ActivitySeatListBinding binding;
    private Flight flight;
    private Double price = 0.0;
    private int num = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        getIntentExtra();
        initSeatList();
        setVariable();
                
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.confirmBtn.setOnClickListener(v -> {
            if(num>0){
                flight.setPassenger(binding.nameSeatSelectedTxt.getText().toString());
                flight.setPrice(price);
                Intent intent = new Intent(SeatListActivity.this,TicketDetailActivity.class);
                intent.putExtra("flight",flight);
                startActivity(intent);
            }else{
                Toast.makeText(SeatListActivity.this,"Please select your seat",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSeatList() {
        if (flight == null || flight.getNumberSeat() == null || flight.getNumberSeat() == 0) {
            Toast.makeText(this, "No seats available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (flight.getReservedSeats() == null) {
            flight.setReservedSeats("");
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 7 == 3) ? 1 : 1;
            }
        });
        binding.seatRecyclerView.setLayoutManager(gridLayoutManager);

        List<Seat> seatList = new ArrayList<>();
        int row = 0;  // We'll increment this at the start of each row

        Map<Integer, String> seatAlphabetMap = new HashMap<>();
        seatAlphabetMap.put(0, "A");
        seatAlphabetMap.put(1, "B");
        seatAlphabetMap.put(2, "C");
        seatAlphabetMap.put(3, "D");
        seatAlphabetMap.put(4, "E");
        seatAlphabetMap.put(5, "F");
        seatAlphabetMap.put(6, "G");

// Calculate total seats including empty spaces for aisles
        int numberSeat = flight.getNumberSeat() + (flight.getNumberSeat() / 7) + 1;

// Get reserved seats as a list
        String reservedSeatsStr = flight.getReservedSeats() != null ?
                flight.getReservedSeats() : "";
        List<String> reservedSeats = Arrays.asList(reservedSeatsStr.split(","));

        for (int i = 0; i < numberSeat; i++) {
            if (i % 7 == 0) {
                row++;  // Increment row for each new row of seats
            }

            if (i % 7 == 3) {
                // Add empty space for aisle
                seatList.add(new Seat(Seat.SeatStatus.EMPTY, "Aisle"));
            } else {
                String seatName = seatAlphabetMap.get(i % 7) + row;
                boolean isReserved = reservedSeats.contains(seatName);
                Seat.SeatStatus seatStatus = isReserved ?
                        Seat.SeatStatus.UNAVAILABLE :
                        Seat.SeatStatus.AVAILABLE;
                seatList.add(new Seat(seatStatus, seatName));
            }
        }
        
        SeatAdapter seatAdapter = new SeatAdapter(seatList, this, (selectedName, num) -> {
            binding.numberSelectedTxt.setText(num + " Seat Selected: ");
            binding.nameSeatSelectedTxt.setText(selectedName);
            DecimalFormat df = new DecimalFormat("#.##");
            price = Double.valueOf(df.format(num * flight.getPrice()));this.num = num;
            binding.priceTxt.setText("₦" + price);
        });

        binding.seatRecyclerView.setAdapter(seatAdapter);
        binding.seatRecyclerView.setNestedScrollingEnabled(true);
    }

    private void getIntentExtra() {
        flight = (Flight) getIntent().getSerializableExtra("flight");
        if (flight == null) {
            Toast.makeText(this, "Error: Flight data not available", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

    }

}
        

