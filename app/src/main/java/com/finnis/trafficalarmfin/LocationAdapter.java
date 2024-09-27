package com.finnis.trafficalarmfin;

import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.view.LayoutInflater;
import com.finnis.trafficalarmfin.Activities.LocationActivity;
import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;


public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    //В документе полное описание работы адаптера, там есть все
    private List<LocationItem> locationItems;
    private List<LocationItem> locationItemsBruh;
    private Context context;

    public LocationAdapter(List<LocationItem> locationItems, Context context) {
        this.locationItems = locationItems;
        this.locationItemsBruh = new ArrayList<>(locationItems);
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


    //Можно будет потом удалять
    @SuppressLint("I love Java till burning out")
    public void filter(String text) {
        locationItems.clear();
        if (text.isEmpty()) {
            locationItems.addAll(locationItemsBruh);
        } else {
            text = text.toLowerCase();
            for (LocationItem item : locationItemsBruh) {
                if (item.getName().toLowerCase().contains(text)) {
                    locationItems.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }


    //Без холдера не работает из-за защиты, поэтому не стоит удалять
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public ViewHolder(@NonNull View nig) {
            super(nig);
            nameTextView = nig.findViewById(R.id.nameTextView);
        }
    }
}