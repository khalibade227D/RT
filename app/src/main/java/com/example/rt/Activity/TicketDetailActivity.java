package com.example.rt.Activity;

import static android.content.Intent.getIntent;
import static android.text.TextUtils.split;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.rt.Model.Flight;
import com.example.rt.R;
import com.example.rt.databinding.ActivityTicketDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TicketDetailActivity extends BaseActivity2{


    private ActivityTicketDetailBinding binding;
    private Flight flight;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final int MANAGE_STORAGE_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTicketDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WELCOME();
        getIntentExtra();
        setVariable();
        TicketCode();
    }

    private void TicketCode() {
        Random random = new Random();
        int num = random.nextInt(10000000)+99999999;
       binding.Code.setText("Code: "+ num);
    }


    private void WELCOME() {
    binding.Welcome.setText("JourneyLine, \nThank you for patronizing  🙌!");
}
private void getIntentExtra() {
        flight = (Flight) getIntent().getSerializableExtra("flight");

    }

    private void setVariable() {
        if (flight == null) {
            Toast.makeText(this, "Flight data is missing", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        binding.backBtn.setOnClickListener(v -> finish());

        binding.fromTxt.setText(flight.getFromShort());
        binding.fromSmallTxt.setText(flight.getFrom());
        binding.toTxt.setText(flight.getToShort());
        binding.toSmallTxt.setText(flight.getTo());
        binding.dateTxt.setText(flight.getDate());
        binding.timeTxt.setText(flight.getTime());
        binding.arrivalTxt.setText(flight.getArriveTime());
        binding.classTxt.setText(flight.getClassSeat());
        binding.priceTxt.setText("₦" + flight.getPrice());
        binding.train.setText(flight.getTrainName());
        binding.seatTxt.setText(flight.getPassenger());

        binding.downloadTicketBtn.setOnClickListener(v -> {

            if (checkPermission()) {
                checkTrain();
                createPdfFromView();
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);

            } else {
                requestPermission();
            }
        });

    }

    private void checkTrain() {
        String trainName = "";

        if(binding.train.getText().toString().equals("Delta"))
            trainName = "flight1";
        else if (binding.train.getText().toString().equals("American")) {
            trainName = "flight2";
        } else if (binding.train.getText().toString().equals("Southwest")) {
            trainName = "flight4";
        } else if (binding.train.getText().toString().equals("United")) {
            trainName = "flight5";
        }else{
            trainName = "flight3";
        }

        // Get reference to flight1 specifically
        DatabaseReference flight1Ref = FirebaseDatabase.getInstance()
                .getReference("Flights")
                .child(trainName);

        flight1Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(getApplicationContext(),
                            "Flight1 not found",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Get current reserved seats
                String currentSeats = snapshot.child("reservedSeats").getValue(String.class);
                if (currentSeats == null) {
                    currentSeats = "";
                }
                // Get seat input (e.g., "A1,B1,C1,D1")
                String seatInput = binding.seatTxt.getText().toString();

            // Split into individual seats
                String[] seatsToAdd = seatInput.split(",");

            // Now seatsToAdd contains: {"A1", "B1", "C1", "D1"}
                String updatedSeats = currentSeats;

                // Check and add each seat
                for (String seat : seatsToAdd) {
                    if (currentSeats.contains(seat)) {
                        Log.d("Firebase", "Seat " + seat + " already reserved");
                        continue;
                    }

                    updatedSeats = updatedSeats.isEmpty() ? seat : updatedSeats + "," + seat;
                }

                // Update Firebase
                snapshot.child("reservedSeats").getRef().setValue(updatedSeats)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Firebase", "Successfully updated seats for flight1");
                            Toast.makeText(getApplicationContext(),
                                    "Seats added Successfully",
                                    Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firebase", "Failed to update seats", e);
                            Toast.makeText(getApplicationContext(),
                                    "Failed to update seats",
                                    Toast.LENGTH_SHORT).show();
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Database error", error.toException());
                Toast.makeText(getApplicationContext(),
                        "Database error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, MANAGE_STORAGE_PERMISSION_CODE);
            } catch (Exception e) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, MANAGE_STORAGE_PERMISSION_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MANAGE_STORAGE_PERMISSION_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                createPdfFromView();
            } else {
                Toast.makeText(this, "Permission denied! Cannot save PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createPdfFromView() {
        try {
            binding.SV.setVisibility(View.INVISIBLE);
            View contentView = binding.Ticket.getChildAt(0);
            contentView.measure(
                    View.MeasureSpec.makeMeasureSpec(binding.Ticket .getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(binding.Ticket .getHeight(), View.MeasureSpec.EXACTLY)
            );

            int width = contentView.getMeasuredWidth();
            int height = contentView.getMeasuredHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(Color.WHITE);
            contentView.draw(canvas);

            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
            PdfDocument.Page page = document.startPage(pageInfo);
            page.getCanvas().drawBitmap(bitmap, 0, 0, null);
            document.finishPage(page);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = "Ticket_" + flight.getFromShort() + "_to_" + flight.getToShort() + "_" + timestamp + ".pdf";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                savePdfWithMediaStore(document, fileName);
            } else {
                savePdfLegacy(document, fileName);
            }

            document.close();
            binding.SV.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            binding.SV.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Error creating PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void savePdfWithMediaStore(PdfDocument document, String fileName) {
        ContentResolver resolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);

        if (uri != null) {
            try (OutputStream out = resolver.openOutputStream(uri)) {
                document.writeTo(out);
                Toast.makeText(this, "Ticket saved to Downloads", Toast.LENGTH_LONG).show();
                openPdfFile(uri);
            } catch (IOException e) {
                Toast.makeText(this, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("PDF_ERROR", "Error saving PDF", e);
            }
        } else {
            Toast.makeText(this, "Failed to create PDF file", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePdfLegacy(PdfDocument document, String fileName) {
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            document.writeTo(fos);
            MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, null);
            Toast.makeText(this, "Ticket saved to Downloads", Toast.LENGTH_LONG).show();
            openPdfFile(Uri.fromFile(file));
        } catch (IOException e) {
            Toast.makeText(this, "Error saving PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("PDF_ERROR", "Error saving PDF", e);
        }
    }

    private void openPdfFile(Uri uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No PDF viewer installed", Toast.LENGTH_SHORT).show();
        }
    }
}
