package com.example.greentrailapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;

import com.example.greentrailapp.Adapters.Marker_RecyclerViewAdapter;
import com.example.greentrailapp.Models.Marker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference fb;
    Marker_RecyclerViewAdapter marker_recyclerViewAdapter;
    ArrayList<Marker> markerList;
    RecyclerView markerRecyclerView;
    BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.explore);
        //Bottom nav bar navigation
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.explore:
                        return true;
                    case R.id.qr:
                        startActivity(new Intent(MainActivity.this, QRActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.map:
                        startActivity(new Intent(MainActivity.this, MapActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        setMarkers();
    }

    private void setMarkers(){
        markerRecyclerView = findViewById(R.id.markerRecyclerView);
        fb = FirebaseDatabase.getInstance().getReference("Markers");
        markerRecyclerView.setHasFixedSize(true);
        markerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        markerList = new ArrayList<>();
        marker_recyclerViewAdapter = new Marker_RecyclerViewAdapter(this,markerList);
        markerRecyclerView.setAdapter(marker_recyclerViewAdapter);

        fb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Marker marker = dataSnapshot.getValue(Marker.class);
                    markerList.add(marker);
                }
                marker_recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}