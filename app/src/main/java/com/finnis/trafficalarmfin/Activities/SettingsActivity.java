package com.finnis.trafficalarmfin.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.finnis.trafficalarmfin.R;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;


public class SettingsActivity extends AppCompatActivity {

    private SeekBar radiusSlider;
    private TextView radiusValueText;
    private Button soundButton;
    private Uri selectedSoundUri;
    private SharedPreferences sharedPreferences;

    //Можно добавить 3000 если добавишь станции электричек
    private final int[] radiusValues = {100, 200, 300, 500, 1000, 2000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        radiusSlider = findViewById(R.id.radiusSlider);
        radiusValueText = findViewById(R.id.radiusValueText);
        soundButton = findViewById(R.id.soundButton);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

        // Инициализация значений слайдера        ^ исправь если добавишь ещё значений
        radiusSlider.setMax(radiusValues.length - 1);
        int savedRadius = sharedPreferences.getInt("radiusIndex", 0);
        radiusSlider.setProgress(savedRadius);
        radiusValueText.setText(String.valueOf(radiusValues[savedRadius]));

        // Загрузка сохранённого звука, обязательно ставить раньше меню!!!
        String sound = sharedPreferences.getString("alarmSound", null);
        if (sound != null) {
            selectedSoundUri = Uri.parse(sound);

        }

        radiusSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radiusValueText.setText(String.valueOf(radiusValues[progress]));
            }


            //Можно потом сделать в лейауте
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Сохранение значения радиуса, потом перенос
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("radiusIndex", seekBar.getProgress());
                editor.apply();
            }
        });

        soundButton.setOnClickListener(v -> {
            // Запуск выбора звука (нужно будет сделать свое меню с тем же звуками, пока как есть)
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Выберите звук");
            Uri currentTone = selectedSoundUri != null ? selectedSoundUri : RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);

            //НЕ УДАЛЯТЬ X2 !!!
            startActivityForResult(intent, 123);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == RESULT_OK) {
            selectedSoundUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (selectedSoundUri != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("alarmSound", selectedSoundUri.toString());
                editor.apply();

            }
        }
        //Исправить при переходе на новое меню звуов
        super.onActivityResult(requestCode, resultCode, data);
    }
}