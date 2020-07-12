package com.example.mypersonalmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ClockActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = mediaPlayer.create(this,R.raw.test);
        mediaPlayer.start();
        Intent intent = this.getIntent();
        String a=intent.getStringExtra(DaysManagerAdd.INFO_DAYS_CON4);
        String b=intent.getStringExtra(DaysManagerAdd.INFO_DAYS_TIM4);
        new AlertDialog.Builder(ClockActivity.this).setTitle(b+"的闹钟").setMessage(a)
                .setPositiveButton("关闭闹铃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mediaPlayer.stop();
                        ClockActivity.this.finish();
                    }
                }).show();
    }
}
