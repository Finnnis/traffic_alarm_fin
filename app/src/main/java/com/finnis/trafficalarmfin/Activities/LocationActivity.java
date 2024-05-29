package com.finnis.trafficalarmfin.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.finnis.trafficalarmfin.AlarmActivity;
import com.finnis.trafficalarmfin.MainActivity;
import com.finnis.trafficalarmfin.R;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class LocationActivity extends AppCompatActivity {

    private TextView nameText;
    private LocationRequest locationRequest;
    private LatLng centerPoint = new LatLng(55.7101252, 37.470135);
    private double radius = 5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activvity_location);

        nameText = findViewById(R.id.nameText);
        View backToMainButton = findViewById(R.id.stopLocation);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);
        nameText.setText(name);
        centerPoint = new LatLng(latitude, longitude);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        getCurrentLocation();

        backToMainButton.setOnClickListener(v -> {
            Intent mainIntent = new Intent(LocationActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        });
    }

    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.getFusedLocationProviderClient(LocationActivity.this)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                if (locationResult != null && locationResult.getLocations().size() > 0) {
                                    int index = locationResult.getLocations().size() - 1;
                                    double latitude = locationResult.getLocations().get(index).getLatitude();
                                    double longitude = locationResult.getLocations().get(index).getLongitude();



                                    Location userLocation = new Location("");
                                    userLocation.setLatitude(latitude);
                                    userLocation.setLongitude(longitude);
                                    if (isUserInZone(userLocation)) {
                                        Intent intent = new Intent(LocationActivity.this, AlarmActivity.class);
                                        startActivity(intent);
                                        onDestroy();
                                    }
                                }
                            }
                        }, Looper.getMainLooper());

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private boolean isUserInZone(Location userLocation) {
        float[] distance = new float[2];
        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(),
                centerPoint.latitude, centerPoint.longitude, distance);
        return distance[0] < radius;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}