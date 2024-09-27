package com.finnis.trafficalarmfin.Activities;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.finnis.trafficalarmfin.R;
import android.os.Bundle;
public class FavoritesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorites);
        TextView fv = findViewById(R.id.favor);
        fv.setText("Favourites");
    }
}