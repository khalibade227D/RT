package com.example.rt.Activity;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.view.View;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity2 {
    private ActivitySearchBinding binding;
    private String from, to, date;
    private int numPassenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        DatabaseReference myRef = database.getReference("Flights");
        ArrayList<Flight> list = new ArrayList<>();
        Query query = myRef.orderByChild("from").equalTo(from);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        Flight fligt = issue.getValue(Flight.class);
                        if(fligt.getTo().equals(to)){
                            list.add(fligt);
                        }
                        if(!list.isEmpty()){
                            binding.SearchView.setLayoutManager(new LinearLayoutManager(SearchActivity.this,LinearLayoutManager.VERTICAL,false ));
                            binding.SearchView.setAdapter(new FlightAdapter(list));
                        }
                        binding.progressBarSearch.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getIntentExtra() {
        from=getIntent().getStringExtra("from");
        to=getIntent().getStringExtra("to");
        date=getIntent().getStringExtra("date");
    }
}