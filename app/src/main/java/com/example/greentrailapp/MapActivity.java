package com.example.greentrailapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String CORSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    BottomNavigationView nav;
    ArrayList<com.example.greentrailapp.Models.Marker> markerArrayList;

    private static String TAG = "MapActivity";

    private GoogleMap mMapView;
    private DatabaseReference fb;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().hide();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getLocationPermissions();

        fb = FirebaseDatabase.getInstance().getReference("Markers");
        fb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<com.example.greentrailapp.Models.Marker> markers = new ArrayList<com.example.greentrailapp.Models.Marker>();
                for (DataSnapshot markerSnapshot : snapshot.getChildren()) {
                    com.example.greentrailapp.Models.Marker marker = markerSnapshot.getValue(com.example.greentrailapp.Models.Marker.class);
                    markers.add(marker);
                }
                markerArrayList = markers;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        nav = findViewById(R.id.nav);
        nav.setSelectedItemId(R.id.map);
        //Bottom nav bar navigation
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.map:
                        return true;
                    case R.id.explore:
                        startActivity(new Intent(MapActivity.this, MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        startActivity(new Intent(MapActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.qr:
                        startActivity(new Intent(MapActivity.this, QRActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Found Location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLatitude()),
                                    15f);

                        } else {
                            Log.d(TAG, "onComplete: Current Location Is Null");
                            Toast.makeText(MapActivity.this, "unable to gat location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMapView.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap() {
        SupportMapFragment mapFragment1 = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment1.getMapAsync(MapActivity.this);
    }

    private void getLocationPermissions() {
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    CORSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,
                        permission,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permission,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    initMap();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMapView = googleMap;
        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMapView.setMyLocationEnabled(true);
            mMapView.getUiSettings().isMyLocationButtonEnabled();
            mMapView.getUiSettings().isZoomGesturesEnabled();
        }

        //Configure Latitude and Longitude Markers
        LatLng Start = new LatLng(-33.91723883193311, 151.22642348221527);
        LatLng Plant2 = new LatLng(-33.91672859728036, 151.22633954932377);
        LatLng Plant3 = new LatLng(-33.91631373120325, 151.22653743754944);
        LatLng Plant4 = new LatLng(-33.91580969644949, 151.22657126037217);
        LatLng Plant5 = new LatLng(-33.9156068954153, 151.226740446537);
        LatLng Plant6 = new LatLng(-33.91558817529551, 151.22695098931996);
        LatLng Plant7 = new LatLng(-33.915756656225625, 151.22686827608382);
        LatLng Plant8 = new LatLng(-33.91613301745093, 151.22812647551302);
        LatLng Plant9 = new LatLng(-33.917035549106224, 151.23207914508058);
        LatLng Plant10 = new LatLng(-33.91712103068157, 151.23207195840862);
        LatLng Plant11 = new LatLng(-33.917276090064505, 151.2320288383768);
        LatLng Plant12 = new LatLng(-33.91745301645164, 151.2319953005743);
        LatLng Plant13 = new LatLng(-33.91682482670761, 151.2343261778379);
        LatLng Plant14 = new LatLng(-33.9164451275022, 151.2343692978697);
        LatLng Plant15 = new LatLng(-33.916649886970816, 151.23469509366106);
        LatLng Plant16 = new LatLng(-33.91679301951032, 151.23484361821502);
        LatLng Plant17 = new LatLng(-33.91833565497455, 151.2345920846968);
        LatLng Plant18 = new LatLng(-33.91827402960963, 151.23440762678302);
        LatLng Plant19 = new LatLng(-33.918276017525336, 151.23425431111443);
        LatLng Plant20 = new LatLng(-33.917783013009725, 151.23210549620737);
        LatLng Plant21 = new LatLng(-33.91796391419029, 151.2320551895036);
        LatLng Plant22 = new LatLng(-33.91827601752826, 151.23200727835803);
        LatLng Plant23 = new LatLng(-33.91708723564837, 151.23010999696564);
        LatLng Plant24 = new LatLng(-33.917405305999154, 151.23002375690433);
        LatLng Plant25 = new LatLng(-33.91745699231785, 151.22802825766297);


        //Adding the marker on map based on latlng
        mMapView.addMarker(new MarkerOptions()
                .position(Start)
                .title("Start Of Trail"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant2)
                .title("Gymea Lily")
                .snippet("Doryanthes Exceisa"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant3)
                .title("Paperbark")
                .snippet("Melaleuca Quinquinervia"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant4)
                .title("Crimson Bottlebrush")
                .snippet("Callistemon Citrinus"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant5)
                .title("Banksia")
                .snippet("Banksia Ericifolia"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant6)
                .title("Mountain Cedar Wattle")
                .snippet("Acacia Elata"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant7)
                .title("Native Mint")
                .snippet("Prostanthera Ovalifolia"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant8)
                .title("Tuckeroo")
                .snippet("Cupaniopsis Anacardioides"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant9)
                .title("Prickly-leaved Tea Tree")
                .snippet("Melateuca Styphelioides"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant10)
                .title("Water Vine")
                .snippet("Cissus Antartica"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant11)
                .title("Rock Lily")
                .snippet("Dendrobium Speciosum"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant12)
                .title("Sandpaper Fig")
                .snippet("Ficus Coronata"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant13)
                .title("Burrawang")
                .snippet("Macrozamia Communis"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant14)
                .title("Plum Pine/Brown Pine")
                .snippet("Podocarpus Elatus"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant15)
                .title("Tussock Grass")
                .snippet("Poa Labilliarderi"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant16)
                .title("Cabbage Tree Palm")
                .snippet("Livinstona Australis"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant17)
                .title("Bolwarra")
                .snippet("Eupomatia Laurina"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant18)
                .title("Blue Flax Lily/ Blueberry Lily")
                .snippet("Dianella Caerulea"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant19)
                .title("Saw Banksia/ Old Man Banksia")
                .snippet("Banksia Serrata"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant20)
                .title("Spiny-headed Mat-rush")
                .snippet("Lomandra Longifolia"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant21)
                .title("Riberry")
                .snippet("Syzgium Luehmannii"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant22)
                .title("Grass Tree")
                .snippet("Xanthorrhoea Australis"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant23)
                .title("Native Ginger")
                .snippet("Alpinia Caerulea"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant24)
                .title("Illawarra Flame Tree")
                .snippet("Barchychiton Acerifolius"));

        mMapView.addMarker(new MarkerOptions()
                .position(Plant25)
                .title("Port Jackson Fig")
                .snippet("Ficus Rubiginosa"));

        //Map will open at this marker position
        float zoomLevel = 19.0f;
        mMapView.moveCamera(CameraUpdateFactory.newLatLngZoom(Plant2, zoomLevel));

        mMapView.setOnInfoWindowClickListener(this);
        mMapView.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        Toast.makeText(this, "Click Plant Name For More Info", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        for (com.example.greentrailapp.Models.Marker selectedMarker : markerArrayList){
            if(selectedMarker.getmName().toLowerCase().contains(marker.getTitle().toLowerCase())){
                Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
                intent.putExtra("Marker", selectedMarker);
                intent.putExtra("Visible", "F");
                startActivity(intent);
                break;
            }
        }
        Intent intent = new Intent(MapActivity.this, InfoActivity.class);
    }
}