package com.example.rt.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rt.Adapter.FlightAdapter;
import com.example.rt.Model.Flight;
import com.example.rt.databinding.ActivitySearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends BaseActivity2 {
    private ActivitySearchBinding binding;
    private String from, to, date;
    private FlightAdapter adapter;
    private final List<Flight> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize RecyclerView
        adapter = new FlightAdapter((ArrayList<Flight>) list);
        binding.SearchView.setLayoutManager(new LinearLayoutManager(this));
        binding.SearchView.setAdapter(adapter);

        getIntentExtra();
        setVariable();
        searchFlights();
    }

    private void searchFlights() {
        binding.progressBarSearch.setVisibility(View.VISIBLE);
        DatabaseReference myRef = database.getReference("Flights");

        Log.d("DEBUG", "From: " + from + ", To: " + to);

        Query query = myRef.orderByChild("from").equalTo(from); // Don't use toLowerCase again, already done in getIntentExtra()
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                try {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            try {
                                Flight flight = issue.getValue(Flight.class);
                                if (flight != null &&
                                        flight.getTo() != null &&
                                        flight.getTo().equalsIgnoreCase(to)) {
                                    list.add(flight);
                                    Log.d("DEBUG", "Flight added: " + flight.getFrom() + " -> " + flight.getTo());
                                } else {
                                    Log.d("DEBUG", "Flight skipped: " + (flight != null ? flight.getTo() : "null"));
                                }
                            } catch (Exception e) {
                                Log.e("FlightParse", "Error parsing flight: " + issue.getKey(), e);
                            }
                        }
                    } else {
                        Log.d("DEBUG", "No matching flights found");
                    }
                } catch (Exception e) {
                    Log.e("SearchError", "Data loading failed", e);
                } finally {
                    binding.progressBarSearch.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBarSearch.setVisibility(View.GONE);
                Toast.makeText(SearchActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }

    private void getIntentExtra() {
        if (getIntent() != null) {
            from = getIntent().getStringExtra("from");
            to = getIntent().getStringExtra("to");
            date = getIntent().getStringExtra("date");

            if (from != null) from = from.toLowerCase();
            if (to != null) to = to.toLowerCase();
            if (date != null) date = date.toLowerCase();

            Log.d("SearchActivity", "Searching flights from: " + from + " to: " + to + " on: " + date);
        }
    }
}