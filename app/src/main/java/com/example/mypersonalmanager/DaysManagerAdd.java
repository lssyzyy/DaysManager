package com.example.mypersonalmanager;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daymanager_add);
        getDate();
        getTime();
        showDate=findViewById(R.id.day_showdate);
        showDate.setOnClickListener(this);
        showDate.setText(new SimpleDateFormat("MM-dd,yyyy").format(new Date(System.currentTimeMillis())));

        showTime=findViewById(R.id.day_showtime);
        showTime.setOnClickListener(this);
        showTime.setText(new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis())));

        helper=new MyDatabaseHelper(this,"daydata",null,1);

        FloatingActionButton mday_add=findViewById(R.id.daymanager_ok);
        editText1=findViewById(R.id.daymanager_content);
        mday_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=helper.getWritableDatabase();
                if(editText1.getText().toString().length()!=0){
                    Insertdata();
                    Toast.makeText(DaysManagerAdd.this,"添加成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(DaysManagerAdd.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(DaysManagerAdd.this,"日程为空",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //获取闹钟服务
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, ClockActivity.class);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        showTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                new TimePickerDialog(DaysManagerAdd.this, 0,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //设置当前时间
                                Calendar c = Calendar.getInstance();
                                c.setTimeInMillis(System.currentTimeMillis());
                                // 根据用户选择的时间来设置Calendar对象
                                c.set(Calendar.HOUR_OF_DAY,hourOfDay);//设置闹钟的小时数，API>23可以使用getHour()
                                c.set(Calendar.MINUTE,minute);//设置闹钟的分钟数，API>23可以使用getMinute()
                                c.set(Calendar.SECOND,0);
                                showTime.setText(hourOfDay+":"+minute);
                                // ②设置AlarmManager在Calendar对应的时间启动Activity
                                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                                Toast.makeText(DaysManagerAdd.this, "闹钟已开启！", Toast.LENGTH_SHORT).show();
                            }
                        }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), false).show();
            }
        });

        //取消闹钟
        Button btn2=findViewById(R.id.day_alarm_cancel);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alarmManager.cancel(pendingIntent);
                Toast.makeText(DaysManagerAdd.this, "闹钟已取消！", Toast.LENGTH_SHORT).show();
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
