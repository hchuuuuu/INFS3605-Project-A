package com.example.greentrailapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.greentrailapp.Models.Marker;
import com.google.firebase.database.DatabaseReference;

public class InfoActivity extends AppCompatActivity {

    TextView intentTest;
    private DatabaseReference fb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        intentTest = findViewById(R.id.intentTest);
        Marker selectedMarker = getIntent().getParcelableExtra("Marker");
        intentTest.setText(selectedMarker.getmName());

    }
}