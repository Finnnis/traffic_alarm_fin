package com.finnis.trafficalarmfin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.widget.Button;

public class AlarmActivity extends Activity {

    private Ringtone ringtone;
    private Uri alarmSoundUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // Выбранный звук из Intent
        Intent intent = getIntent();
        String alarmUri = intent.getStringExtra("ALARM_SOUND_URI");
        if (alarmUri != null) {
            alarmSoundUri = Uri.parse(alarmUri);
        } else {
            // Если звук не выбран, используй тот самый звук по умолчанию
            alarmSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        }

        playAlarmSound();

        View stopAlarmButton = findViewById(R.id.stop_alarm_button);
        stopAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAlarm();
            }
        });
    }

    private void playAlarmSound() {
        try {
            ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmSoundUri);
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopAlarm() {
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
        Intent alarm = new Intent(AlarmActivity.this, MainActivity.class);
        startActivity(alarm);
        finish();
    }

    //Остановка музыки
    @Override
    protected void onStop() {
        super.onStop();
        stopAlarm();
    }

    //Удалять сразу после рингтона, потом сразу обновляется
    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAlarm();
    }
}