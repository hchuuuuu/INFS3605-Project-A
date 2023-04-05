package com.example.greentrailapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.greentrailapp.Models.Person;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    BottomNavigationView nav;
    Button logout;
    TextView progressTV;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference userReference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.profile);

        //Binding views, create instance of FirebaseAuth
        mAuth = FirebaseAuth.getInstance();


        //Bottom nav bar navigation
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.profile:
                        return true;
                    case R.id.explore:
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.map:
                        startActivity(new Intent(ProfileActivity.this, MapActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.qr:
                        startActivity(new Intent(ProfileActivity.this, QRActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.quiz:
                        startActivity(new Intent(ProfileActivity.this, QuizModuleActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            Toast.makeText(ProfileActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
        });

        FirebaseUser user = mAuth.getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        progressTV = findViewById(R.id.tvPoints);
        progressBar = findViewById(R.id.PBpoints);

        userReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Person user = snapshot.getValue(Person.class);
                if (user != null) {
                    String fullName = user.fullName;
                    int progress = user.progress;

                    progressTV.setText(String.valueOf(progress));
                    progressBar.setProgress(progress);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}