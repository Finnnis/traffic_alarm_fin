package com.finnis.trafficalarmfin;

import android.content.Intent;
import android.media.Image;
import com.finnis.trafficalarmfin.Activities.AboutUsActivity;
import com.finnis.trafficalarmfin.Activities.FavoritesActivity;
import com.finnis.trafficalarmfin.Activities.SettingsActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bruh);

        View locationButton = findViewById(R.id.locationButton);
        View toSettings = findViewById(R.id.settings_button);
        View toFavorites = findViewById(R.id.favorites_button);
        View toAboutUs = findViewById(R.id.about_us_button);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Stations.class);
                startActivity(intent);

            }
        });
        toSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        toFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });
        toAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
    }
}
