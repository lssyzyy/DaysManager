package com.example.mypersonalmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class ClockActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaPlayer = MediaPlayer.create(this,R.raw.test);
        mMediaPlayer.start();
        Intent intent = this.getIntent();
        String a=intent.getStringExtra("INFO_DAYS_CON");
        String b=intent.getStringExtra("INFO_DAYS_TIM");
        String c=intent.getStringExtra("INFO_DAYS_CON2");
        String d=intent.getStringExtra("INFO_DAYS_TIM2");
        String clo=a;
        String clo2=b;
        if(a==null){
            clo=c;clo2=d;
        }
        new AlertDialog.Builder(ClockActivity.this).setTitle(clo2+"的闹钟").setMessage(clo)
                .setPositiveButton("关闭闹铃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMediaPlayer.stop();
                        ClockActivity.this.finish();
                    }
                }).show();
    }
}
