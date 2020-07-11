package com.example.mypersonalmanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DaysManagerAdd extends AppCompatActivity implements View.OnClickListener {
    private TextView showDate,showTime;
    private Calendar cal;
    private EditText editText1;
    private TextView textView1,textView2;
    private SQLiteDatabase db;
    private int year,month,day,hour,minute;
    MyDatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daymanager_add);
        getDate();
        getTime();
        showDate=findViewById(R.id.day_showdate);
        showDate.setOnClickListener(this);
        showDate.setText(new SimpleDateFormat("MM-dd,yy").format(new Date(System.currentTimeMillis())));

        showTime=findViewById(R.id.day_showtime);
        showTime.setOnClickListener(this);
        showTime.setText(new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis())));

        helper=new MyDatabaseHelper(this,"daydata",null,1);

        FloatingActionButton mday_add=findViewById(R.id.daymanager_ok);
        editText1=findViewById(R.id.daymanager_content);
        textView2=findViewById(R.id.day_showdate);
        textView1=findViewById(R.id.day_showtime);
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
    }

    private void getDate() {
        cal=Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        Log.i("zyy","year"+year);
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
            case R.id.day_showtime:
                TimePickerDialog.OnTimeSetListener listener1=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker arg0, int hourOfDay, int minute) {
                        showTime.setText(hourOfDay+":"+minute);
                    }
                };
                TimePickerDialog dialog1=new TimePickerDialog(DaysManagerAdd.this, TimePickerDialog.THEME_HOLO_LIGHT, listener1, hour, minute,true);
                dialog1.show();
                break;
            default:
                break;
        }
    }
    private void Insertdata(){
        ContentValues contentValues=new ContentValues();
        contentValues.put("dayid",textView2.getText().toString());
        contentValues.put("time",textView1.getText().toString());
        contentValues.put("content",editText1.getText().toString());
        db.insert("daysdb",null,contentValues);
    }
}
