package com.example.rt.Adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rt.Activity.SeatListActivity;
import com.example.rt.Model.Flight;
import com.example.rt.R;
import com.example.rt.databinding.ViewholderFlightBinding;

import java.util.ArrayList;
public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.ViewHolder> {
    private final ArrayList<Flight> flights;
    private final Context context;

    public FlightAdapter(ArrayList<Flight> flights, Context context) {
        this.flights = flights != null ? flights : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderFlightBinding binding = ViewholderFlightBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flight flight = flights.get(position);
        if (flight == null) return;

        try {
            // Image loading with placeholder and error handling
            if (flight.getTrainLogo() != null && !flight.getTrainLogo().isEmpty()) {
                Glide.with(context)
                        .load(flight.getTrainLogo())
                        .placeholder(R.drawable.baba)
                        .error(R.drawable.barcode)
                        .into(holder.binding.logo);
            }

            // Safe text setting
            holder.binding.fromTxt.setText(flight.getFrom() != null ? flight.getFrom() : "");
            holder.binding.fromShortTxt.setText(flight.getFromShort() != null ? flight.getFromShort() : "");
            holder.binding.toTxt.setText(flight.getTo() != null ? flight.getTo() : "");
            holder.binding.toShortTxt.setText(flight.getToShort() != null ? flight.getToShort() : "");
            holder.binding.arrivalTxt.setText(flight.getArriveTime() != null ? flight.getArriveTime() : "");
            holder.binding.classTxt.setText(flight.getClassSeat() != null ? flight.getClassSeat() : "");
            holder.binding.priceTxt.setText(flight.getPrice() != null ? "$" + flight.getPrice() : "");

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, SeatListActivity.class);
                intent.putExtra("flight", flight);
                context.startActivity(intent);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return flights.size();
    }

    public void updateFlights(ArrayList<Flight> newFlights) {
        flights.clear();
        flights.addAll(newFlights);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderFlightBinding binding;

        public ViewHolder(ViewholderFlightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}