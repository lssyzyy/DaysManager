package com.example.mypersonalmanager;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DaysManagerAdd extends AppCompatActivity implements View.OnClickListener {
    private TextView showDate,showTime;
    private Calendar cal;
    private EditText editText1;
    private SQLiteDatabase db;
    private int year,month,day,hour,minute;
    MyDatabaseHelper helper;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    Calendar c;
    Intent intent2;
    Switch switch_btn;
    FloatingActionButton mday_add,mday_cancel;
    int flag,flag2,flag3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daymanager_add);

        //获取闹钟服务
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        getDate();
        getTime();
        showDate=findViewById(R.id.day_showdate);
        showDate.setOnClickListener(this);
        showDate.setText(new SimpleDateFormat("MM-dd,yyyy").format(new Date(System.currentTimeMillis())));

        showTime=findViewById(R.id.day_showtime);
        showTime.setOnClickListener(this);
        showTime.setText(new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis())));

        helper=new MyDatabaseHelper(this,"daydata",null,1);

        mday_add=findViewById(R.id.daymanager_ok);
        editText1=findViewById(R.id.daymanager_content);
        switch_btn=findViewById(R.id.switch_btn);
        c = Calendar.getInstance();
        //设置日期
        flag2=0;
        showDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag2=1;
                Calendar currentTime = Calendar.getInstance();
                new DatePickerDialog(DaysManagerAdd.this, 0,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // 根据用户选择的时间来设置Calendar对象
                                c.set(Calendar.YEAR,year);
                                c.set(Calendar.MONTH,month);
                                c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                showDate.setText((++month)+"-"+dayOfMonth+","+year);
                            }
                        }, currentTime.get(Calendar.YEAR), currentTime.get(Calendar.MONTH),currentTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //设置时分秒
        flag=0;
        showTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                Calendar currentTime = Calendar.getInstance();
                new TimePickerDialog(DaysManagerAdd.this, 0,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // 根据用户选择的时间来设置Calendar对象
                                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                c.set(Calendar.MINUTE,minute);
                                c.set(Calendar.SECOND,0);
                                showTime.setText(hourOfDay+":"+minute);
                            }
                        }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), false).show();
            }
        });
        //switch开关
        flag3=0;
        switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    mday_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //保存switch状态
                            SharedPreferences mPref=getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mPref.edit();
                            editor.putBoolean("flag", true);
                            editor.commit();
                            db=helper.getWritableDatabase();
                            if(editText1.getText().toString().length()!=0){
                                Insertdata();
                                Toast.makeText(DaysManagerAdd.this,"添加成功",Toast.LENGTH_SHORT).show();
                                intent2 = new Intent(DaysManagerAdd.this, ClockActivity.class);
                                intent2.putExtra("INFO_DAYS_CON",editText1.getText().toString());
                                intent2.putExtra("INFO_DAYS_TIM",showTime.getText().toString());
                                pendingIntent = PendingIntent.getActivity(DaysManagerAdd.this, 0, intent2, 0);
                                if(flag==0&&flag2==0){
                                    switch_btn.setChecked(false);
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                                }else{
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                                }
                                Toast.makeText(DaysManagerAdd.this, "开启闹钟", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(DaysManagerAdd.this,MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(DaysManagerAdd.this,"日程为空",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    mday_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //保存switch状态
                            SharedPreferences mPref=getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mPref.edit();
                            editor.putBoolean("flag", false);
                            editor.commit();

                            db=helper.getWritableDatabase();
                            if(editText1.getText().toString().length()!=0){
                                Insertdata();
                                intent2 = new Intent(DaysManagerAdd.this, ClockActivity.class);
                                intent2.putExtra("INFO_DAYS_CON",editText1.getText().toString());
                                intent2.putExtra("INFO_DAYS_TIM",showTime.getText().toString());
                                pendingIntent = PendingIntent.getActivity(DaysManagerAdd.this, 0, intent2, 0);
                                alarmManager.cancel(pendingIntent);
                                Toast.makeText(DaysManagerAdd.this,"添加成功",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(DaysManagerAdd.this,MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(DaysManagerAdd.this,"日程为空",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(DaysManagerAdd.this, "取消闹钟", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //不按闹铃添加日程
        mday_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mPref=getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mPref.edit();
                editor.putBoolean("flag", false);
                editor.commit();
                db=helper.getWritableDatabase();
                if(editText1.getText().toString().length()!=0){
                    Insertdata();
                    intent2 = new Intent(DaysManagerAdd.this, ClockActivity.class);
                    intent2.putExtra("INFO_DAYS_CON",editText1.getText().toString());
                    intent2.putExtra("INFO_DAYS_TIM",showTime.getText().toString());
                    pendingIntent = PendingIntent.getActivity(DaysManagerAdd.this, 0, intent2, 0);
                    Toast.makeText(DaysManagerAdd.this,"添加成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(DaysManagerAdd.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(DaysManagerAdd.this,"日程为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mday_cancel=findViewById(R.id.daymanager_cancel);
        mday_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaysManagerAdd.this.finish();
            }
        });
    }

    private void getDate() {
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
    }
    private void getTime(){
        cal=Calendar.getInstance();
        hour=cal.get(Calendar.HOUR_OF_DAY);
        minute=cal.get(Calendar.MINUTE);
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.day_showdate:
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        showDate.setText((++month)+"-"+day+","+year);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(DaysManagerAdd.this, DatePickerDialog.THEME_HOLO_LIGHT,listener,year,month,day);//主题在这里！后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();
                break;
            default:
                break;
        }
    }
    private void Insertdata(){
        ContentValues contentValues=new ContentValues();
        contentValues.put("dayid",showDate.getText().toString());
        contentValues.put("time",showTime.getText().toString());
        contentValues.put("content",editText1.getText().toString());
        db.insert("daysdb",null,contentValues);
    }
}
