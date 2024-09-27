package com.finnis.trafficalarmfin;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;


public class Stations extends AppCompatActivity {

    private LocationAdapter adapter;
    private List<LocationItem> locationItems;

    @Override
    protected void onCreate(Bundle sav) {
        super.onCreate(sav);

        setContentView(R.layout.list);
        RecyclerView rec = findViewById(R.id.recyclerView);
        rec.setLayoutManager(new LinearLayoutManager(this));
        SearchView sea = findViewById(R.id.searchView);

        String[] locationNames = getResources().getStringArray(R.array.location_names);
        String[] locationLatitudes = getResources().getStringArray(R.array.location_latitudes);
        String[] locationLongitudes = getResources().getStringArray(R.array.location_longitudes);

        locationItems = new ArrayList<>();
        for (int i = 0; i < locationNames.length; i++) {
            double latitude = Double.parseDouble(locationLatitudes[i]);
            double longitude = Double.parseDouble(locationLongitudes[i]);
            locationItems.add(new LocationItem(locationNames[i], latitude, longitude));
        }

        adapter = new LocationAdapter(locationItems, this);
        rec.setAdapter(adapter);

        sea.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return false;
            }
        });
    }
}