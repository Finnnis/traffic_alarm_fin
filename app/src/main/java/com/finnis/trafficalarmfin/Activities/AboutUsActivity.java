package com.finnis.trafficalarmfin.Activities;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.finnis.trafficalarmfin.R;
import android.widget.TextView;
public class AboutUsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle sS) {
        super.onCreate(sS);
        setContentView(R.layout.about_us);
        TextView av = findViewById(R.id.about);
        av.setText("About Us");
    }
}