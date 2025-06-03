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

public class SearchActivity extends BaseActivity2 {
    private ActivitySearchBinding binding;
    private String from, to, date;
    private int numPassenger;

    private FlightAdapter adapter;
    private ArrayList<Flight> flightList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();

        getIntentExtra();
        initList();
        setVariable();

    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void initList() {
        binding.progressBarSearch.setVisibility(View.VISIBLE);

        try {
            // Initialize adapter and RecyclerView
            adapter = new FlightAdapter(flightList, this);
            binding.SearchView.setLayoutManager(new LinearLayoutManager(this));
            binding.SearchView.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("SearchActivity", "RecyclerView setup failed", e);
            binding.progressBarSearch.setVisibility(View.GONE);
            showError("Failed to initialize flight list");
            return;
        }

        try {
            DatabaseReference myRef = database.getReference("Flights");
            Query query = myRef.orderByChild("from").equalTo(from);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        flightList.clear();

                        if (snapshot.exists()) {
                            for (DataSnapshot issue : snapshot.getChildren()) {
                                try {
                                    Flight flight = issue.getValue(Flight.class);
                                    if (flight != null && flight.getTo().equals(to)) {
                                        flightList.add(flight);
                                    }
                                } catch (Exception e) {
                                    Log.e("SearchActivity", "Error parsing flight data", e);
                                    // Continue with next item
                                }
                            }
                        }

                        adapter.updateFlights(flightList);

                        if (flightList.isEmpty()) {
                            showEmptyState(true);
                            Log.d("SearchActivity", "No flights found matching criteria");
                        } else {
                            showEmptyState(false);
                            Log.d("SearchActivity", "Found " + flightList.size() + " flights");
                        }

                    } catch (Exception e) {
                        Log.e("SearchActivity", "Error processing flight data", e);
                        showError("Error loading flight data");
                    } finally {
                        binding.progressBarSearch.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    try {
                        Log.e("SearchActivity", "Database error: " + error.getMessage());
                        showError("Failed to load flights: " + error.getMessage());
                    } finally {
                        binding.progressBarSearch.setVisibility(View.GONE);
                    }
                }
            });

        } catch (Exception e) {
            Log.e("SearchActivity", "Database query failed", e);
            binding.progressBarSearch.setVisibility(View.GONE);
            showError("Failed to query flights");
        }
    }

    // Helper methods for UI states
    private void showEmptyState(boolean show) {
        if (binding.noResultsText != null) {
            binding.noResultsText.setVisibility(show ? View.VISIBLE : View.GONE);
        }
        binding.SearchView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    private void showError(String message) {
        // Implement your error display logic here
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        showEmptyState(true);
        if (binding.noResultsText != null) {
            binding.noResultsText.setText(message);
        }
    }

    private void getIntentExtra() {
        from=getIntent().getStringExtra("from");
        to=getIntent().getStringExtra("to");
        date=getIntent().getStringExtra("date");
        //numPassenger = getIntent().getIntExtra("numPassenger", 1);
    }
}