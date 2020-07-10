package com.example.mypersonalmanager;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class DaysEdit extends AppCompatActivity implements View.OnClickListener{
    private Calendar cal;
    private SQLiteDatabase db;
    private EditText editText1;
    private TextView textView2,textView3;
    private TextView showDate,showTime;
    private int year,month,day,hour,minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.days_edit);

        editText1=findViewById(R.id.day_edit_content);
        textView2=findViewById(R.id.day_edit_showdate);
        textView3=findViewById(R.id.day_edit_showtime);
        Intent intent = this.getIntent();
        String infocontent = intent.getStringExtra(DaysActivity.INFO_DAYS_CON2);
        String infodate = intent.getStringExtra(DaysActivity.INFO_DAYS_DATE2);
        String infotime = intent.getStringExtra(DaysActivity.INFO_DAYS_TIME2);
        editText1.setText(infocontent);
        textView2.setText(infodate);
        textView3.setText(infotime);

        getDate();
        getTime();
        showTime=findViewById(R.id.day_edit_showtime);
        showTime.setOnClickListener(this);
        showDate=findViewById(R.id.day_edit_showdate);
        showDate.setOnClickListener(this);

        FloatingActionButton button_delete=findViewById(R.id.day_edit_ok);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"删除日程");
                EditDate(v);
                Intent intent=new Intent(DaysEdit.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getDate() {
        cal= Calendar.getInstance();
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
            case R.id.day_edit_showdate:
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        showDate.setText((++month)+"-"+day+","+year);      //将选择的日期显示到TextView中,因为之前获取month直接使用，所以不需要+1，这个地方需要显示，所以+1
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(DaysEdit.this, DatePickerDialog.THEME_HOLO_LIGHT,listener,year,month,day);//主题在这里！后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();
                break;
            case R.id.day_edit_showtime:
                TimePickerDialog.OnTimeSetListener listener1=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker arg0, int hourOfDay, int minute) {
                        showTime.setText(hourOfDay+":"+minute);
                    }
                };
                TimePickerDialog dialog1=new TimePickerDialog(DaysEdit.this, TimePickerDialog.THEME_HOLO_LIGHT, listener1, hour, minute,true);
                dialog1.show();
                break;
            default:
                break;
        }
    }
    private void EditDate(View v) {
        MyDatabaseHelper helper=new MyDatabaseHelper(this,"daydata",null,1);
        db=helper.getReadableDatabase();
        Intent intent = this.getIntent();
        ContentValues values = new ContentValues();
        values.put("content",editText1.getText().toString());
        values.put("dayid",textView2.getText().toString());
        values.put("time",textView3.getText().toString());
        int editCount = db.update("daysdb",values,"content = "+intent.getStringExtra(DaysActivity.INFO_DAYS_CON2),null);
        db.close();
        Toast.makeText(this,"editCount = "+editCount,Toast.LENGTH_SHORT).show();
    }
}
