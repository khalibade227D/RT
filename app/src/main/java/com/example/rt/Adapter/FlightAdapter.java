package com.example.rt.Adapter;
import static android.content.Intent.getIntent;

import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.rt.Activity.SeatListActivity;
import com.example.rt.Model.Flight;
import com.example.rt.R;
import com.example.rt.databinding.ViewholderFlightBinding;
import java.util.ArrayList;
import java.util.List;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.ViewHolder> {
    private List<Flight> flights;
    private Context context;

    public FlightAdapter(List<Flight> flights) {
        this.flights = flights != null ? flights : new ArrayList<>(); // Ensure flights is never null
    }

    public void updateList(List<Flight> newFlights) {
        this.flights = newFlights != null ? newFlights : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderFlightBinding binding = ViewholderFlightBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightAdapter.ViewHolder holder, int position) {
        // Check if flights list is empty or position is invalid
        if (flights == null || position < 0 || position >= flights.size()) {
            return;
        }

        Flight flight = flights.get(position);

        // Add debug log to check flight object
        Log.d("FlightAdapter", "Flight at position " + position + ": " + (flight != null ? flight.toString() : "NULL"));

        // Check if flight is null before using it
        if (flight == null) {
            Log.e("FlightAdapter", "Flight object is null at position: " + position);
            return;
        }

        // Load image only if URL is not null/empty
        if (flight.getTrainLogo() != null && !flight.getTrainLogo().isEmpty()) {
            Glide.with(context)
                    .load(flight.getTrainLogo())
                    .into(holder.binding.logo);
        } else {
            // Set a default image if logo is missing
            holder.binding.logo.setImageResource(R.drawable.baba);
        }

        // Safely set text fields with null checks
        holder.binding.Time.setText(flight.getTime() != null ? flight.getTime() : "N/A");
        holder.binding.departureDateTxt.setText(flight.getDate() != null ? flight.getDate() : "N/A");
        holder.binding.fromTxt.setText(flight.getFrom() != null ? flight.getFrom() : "N/A");
        holder.binding.fromShortTxt.setText(flight.getFromShort() != null ? flight.getFromShort() : "N/A");
        holder.binding.toTxt.setText(flight.getTo() != null ? flight.getTo() : "N/A");
        holder.binding.toShortTxt.setText(flight.getToShort() != null ? flight.getToShort() : "N/A");
        holder.binding.arrivalTxt.setText(flight.getArriveTime() != null ? flight.getArriveTime() : "N/A");
        holder.binding.classTxt.setText(flight.getClassSeat() != null ? flight.getClassSeat() : "N/A");
        Double p =  flight.getPrice();
        holder.binding.priceTxt.setText("₦" + p);

        holder.itemView.setOnClickListener(v -> {
            // Double-check flight is not null before starting new activity
            if (flight != null) {
                Log.d("FlightAdapter", "Passing flight to SeatListActivity: " + flight.toString());
                Intent intent = new Intent(context, SeatListActivity.class);
                intent.putExtra("flight", flight);
                context.startActivity(intent);
            } else {
                Log.e("FlightAdapter", "Cannot navigate - flight is null");
                Toast.makeText(context, "Ticket information not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return flights != null ? flights.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderFlightBinding binding;

        public ViewHolder(ViewholderFlightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

