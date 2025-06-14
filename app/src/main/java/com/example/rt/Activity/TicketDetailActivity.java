package com.example.rt.Activity;
import android.os.Bundle;
import android.view.View;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.rt.databinding.ActivityTicketDetailBinding;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import com.example.rt.Model.Flight;
import com.example.rt.R;
import com.example.rt.databinding.ActivityTicketDetailBinding;

public class TicketDetailActivity extends BaseActivity2 {
    private ActivityTicketDetailBinding binding;

    private Flight flight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
        binding.fromTxt.setText(flight.getFromShort());
        binding.fromSmallTxt.setText(flight.getFrom());
        binding.toTxt.setText(flight.getTo());
        binding.toShortTxt.setText(flight.getToShort());
        binding.toSmallTxt.setText(flight.getTo());
        binding.dateTxt.setText(flight.getDate());
        binding.timeTxt.setText(flight.getTime());
        binding.arrivalTxt.setText(flight.getArriveTime());
        binding.classTxt.setText(flight.getClassSeat());
        binding.priceTxt.setText("$"+flight.getPrice());
        binding.train.setText(flight.getTrainName());
        binding.seatTxt.setText(flight.getPassenger());

        Glide.with(TicketDetailActivity.this)
                .load(flight.getTrainLogo())
                .into(binding.logo);


    }

    private void getIntentExtra() {
        flight = (Flight) getIntent().getSerializableExtra("flight");
    }

}