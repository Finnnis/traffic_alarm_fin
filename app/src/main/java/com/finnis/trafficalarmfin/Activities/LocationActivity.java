package com.finnis.trafficalarmfin.Activities;
import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import android.location.Location;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class LocationActivity extends AppCompatActivity {


    private TextView nameText;
    private LocationRequest locationRequest;

    //Значения по умолчанию
    private LatLng centerPoint = new LatLng(00.000000, 00.000000);
    private double radius = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activvity_location);

        //Переход значений
        SharedPreferences sP = getSharedPreferences("Settings", MODE_PRIVATE);
        int savedRadius= sP.getInt("radiusIndex", 0);


        //Можно добавить 3000 если добавишь станции электричек
        int[] values = {100, 200, 300, 500, 1000, 2000};
        radius = values[savedRadius];

        //Выбор значений из базы данных
        nameText = findViewById(R.id.nameText);
        View backToMainButton = findViewById(R.id.stopLocation);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        double latitude = intent.getDoubleExtra("latitude", 0);
        double longitude = intent.getDoubleExtra("longitude", 0);
        nameText.setText(name);
        centerPoint = new LatLng(latitude, longitude);

        //Разрешение для локации (всегда при использовании)
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        getCurrentLocation();

        //Если надо отменить поездку. Кнопка шакальная, как и все другие,  надо менять
        backToMainButton.setOnClickListener(v -> {
            Intent mainIntent = new Intent(LocationActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        });
    }

    private void getCurrentLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //Поменяй сточки 79-82 если будет фоновый режим
            if (ActivityCompat.checkSelfPermission(LocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.getFusedLocationProviderClient(LocationActivity.this)
                        .requestLocationUpdates(locationRequest, new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);

                                //                     ^  это оставь, AS ругается без этого
                                if (locationResult != null && locationResult.getLocations().size() > 0) {
                                    int index = locationResult.getLocations().size() - 1;

                                    // Значения из базы данный
                                    double latitude = locationResult.getLocations().get(index).getLatitude();
                                    double longitude = locationResult.getLocations().get(index).getLongitude();

                                    Location local = new Location("");
                                    local.setLatitude(latitude);
                                    local.setLongitude(longitude);

                                    //Срабатывание будильника, обновление локации происходит раз в 2-5 секунд, может срабатывать несколько раз если не нажать через полчаса
                                    if (isUserInZone(local)) {
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
        // НЕ УДАЛЯЙ!!!!
        return distance[0] < radius;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Перепроверка разрешений если вылетает приложение
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                //Если не нажать на разрешение локации
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}