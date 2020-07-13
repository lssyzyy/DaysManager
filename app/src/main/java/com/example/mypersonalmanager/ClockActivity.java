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
        mMediaPlayer = MediaPlayer.create(this, getSystemDefultRingtoneUri());
        mMediaPlayer.setLooping(true);
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        Intent intent = this.getIntent();
        String a=intent.getStringExtra("INFO_DAYS_CON");
        String b=intent.getStringExtra("INFO_DAYS_TIM");
        String c=intent.getStringExtra(DaysEdit.INFO_DAYS_CON5);
        String d=intent.getStringExtra(DaysEdit.INFO_DAYS_TIM5);
        new AlertDialog.Builder(ClockActivity.this).setTitle(b+"的闹钟").setMessage(a)
                .setPositiveButton("关闭闹铃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMediaPlayer.stop();
                        ClockActivity.this.finish();
                    }
                }).show();
    }
    private Uri getSystemDefultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this,
                RingtoneManager.TYPE_RINGTONE);
    }
}
