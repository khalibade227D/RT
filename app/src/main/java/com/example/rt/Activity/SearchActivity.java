package com.example.rt.Activity;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rt.Adapter.FlightAdapter;
import com.example.rt.Model.Flight;
import com.example.rt.R;
import com.example.rt.databinding.ActivitySearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity2 {
    private ActivitySearchBinding binding;
    private String from, to, date;
    private FlightAdapter adapter;
    private FirebaseDatabase database;
    private DatabaseReference flightsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        flightsRef = database.getReference("Flights"); // Note: "Flights" (uppercase)

        // Initialize RecyclerView
        adapter = new FlightAdapter(new ArrayList<>());
        binding.SearchView.setLayoutManager(new LinearLayoutManager(this));
        binding.SearchView.setAdapter(adapter);

        getIntentExtra();
        setVariable();
        searchFlights();
    }

    private void searchFlights() {

        binding.progressBarSearch.setVisibility(View.VISIBLE);

        // Query flights where "from" matches (case-sensitive)
        Query query = flightsRef.orderByChild("from").equalTo(from.toLowerCase());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Flight> filteredFlights = new ArrayList<>();

                for (DataSnapshot flightSnapshot : snapshot.getChildren()) {
                    Flight flight = flightSnapshot.getValue(Flight.class);
                    if (flight != null && flight.getTo().equalsIgnoreCase(to)) {
                        filteredFlights.add(flight);
                    }
                }

                if (filteredFlights.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "No flights found", Toast.LENGTH_SHORT).show();
                } else {
                    adapter.updateList(filteredFlights); // Update adapter
                }

                binding.progressBarSearch.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarSearch.setVisibility(View.GONE);
                Toast.makeText(SearchActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void getIntentExtra() {
        from = getIntent().getStringExtra("from").toLowerCase(); // Force lowercase
        to = getIntent().getStringExtra("to").toLowerCase();
        date = getIntent().getStringExtra("date");

        Log.d("SearchActivity", "Searching flights from: " + from + " to: " + to);
    }
}