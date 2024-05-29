package com.finnis.trafficalarmfin;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import com.finnis.trafficalarmfin.Activities.LocationActivity;
import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {
    private List<LocationItem> locationItems;
    private List<LocationItem> locationItemsFull;
    private Context context;

    public LocationAdapter(List<LocationItem> locationItems, Context context) {
        this.locationItems = locationItems;
        this.locationItemsFull = new ArrayList<>(locationItems);
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationItem item = locationItems.get(position);
        holder.nameTextView.setText(item.getName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, LocationActivity.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("latitude", item.getLatitude());
            intent.putExtra("longitude", item.getLongitude());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return locationItems.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String text) {
        locationItems.clear();
        if (text.isEmpty()) {
            locationItems.addAll(locationItemsFull);
        } else {
            text = text.toLowerCase();
            for (LocationItem item : locationItemsFull) {
                if (item.getName().toLowerCase().contains(text)) {
                    locationItems.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }
    }
}