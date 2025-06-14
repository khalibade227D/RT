package com.example.rt.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rt.Model.Seat;
import com.example.rt.R;
import com.example.rt.databinding.SeatItemBinding;

import java.util.ArrayList;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {
    private final List<Seat> seatList;
    private final Context context;
    private final ArrayList<String> selectedSeatName = new ArrayList<>();

    private SelectedSeat selectedSeat;


    public SeatAdapter(List<Seat> seatList, Context context, SelectedSeat selectedSeat) {
        this.seatList = seatList;
        this.context = context;
        this.selectedSeat = selectedSeat;
    }
    @NonNull
    @Override
    public SeatAdapter.SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SeatItemBinding binding = SeatItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new SeatViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        Seat seat = seatList.get(position);
        holder.binding.SeatImageView.setText(seat.getName());

        switch (seat.getStatus()){
            case AVAILABLE:
                holder.binding.SeatImageView.setBackgroundResource(R.drawable.ic_seat_available);
                holder.binding.SeatImageView.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case SELECTED:
                holder.binding.SeatImageView.setBackgroundResource(R.drawable.ic_seat_selected);
                holder.binding.SeatImageView.setTextColor(context.getResources().getColor(R.color.black));
                break;
            case UNAVAILABLE:
                holder.binding.SeatImageView.setBackgroundResource(R.drawable.ic_seat_unavailable);
                holder.binding.SeatImageView.setTextColor(context.getResources().getColor(R.color.grey));
                break;
            case EMPTY:
                holder.binding.SeatImageView.setBackgroundResource(R.drawable.ic_seat_empty);
                holder.binding.SeatImageView.setTextColor(Color.parseColor("#00000000"));
                break;
        }
        holder.binding.SeatImageView.setOnClickListener(v -> {
            if(seat.getStatus() == Seat.SeatStatus.AVAILABLE){
                seat.setStatus(Seat.SeatStatus.SELECTED);
                selectedSeatName.add(seat.getName());
                notifyItemChanged(position);
            }else if(seat.getStatus() == Seat.SeatStatus.SELECTED){
                seat.setStatus(Seat.SeatStatus.AVAILABLE);
                selectedSeatName.remove(seat.getName());
                notifyItemChanged(position);
            }

            String selected = selectedSeatName.toString()
                    .replace("]","")
                    .replace("[","")
                    .replace("","");

            selectedSeat.Return(selected,selectedSeatName.size());

        });
    }

    @Override
    public int getItemCount() {
        return seatList.size(); // ✅ Return actual list size
    }
    public static class SeatViewHolder extends RecyclerView.ViewHolder {
        SeatItemBinding binding;
        public SeatViewHolder(@NonNull SeatItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
public interface  SelectedSeat{
        void Return(String selectedName,int num);
}
}