package com.example.mypersonalmanager;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static android.content.ContentValues.TAG;

public class DaysEdit extends AppCompatActivity implements View.OnClickListener{
    private Calendar cal;
    private SQLiteDatabase db;
    private EditText editText1;
    private TextView textView2,textView3;
    private TextView showDate,showTime;
    private int year,month,day,hour,minute;
    AlarmManager alarmManager;
    Switch switch_btn;
    Intent intent2;
    private PendingIntent pendingIntent;
    public static final String INFO_DAYS_CON4 = "INFO_DAYS_CON4";
    public static final String INFO_DAYS_TIM4 = "INFO_DAYS_TIM4";
    int flag;
    Calendar c;
    FloatingActionButton button_edit;
    MyDatabaseHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.days_edit);
        //获取闹钟服务
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);

        helper=new MyDatabaseHelper(this,"daydata",null,1);
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


        flag=0;
        showTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                Calendar currentTime = Calendar.getInstance();
                new TimePickerDialog(DaysEdit.this, 0,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //设置当前时间
                                c = Calendar.getInstance();
                                c.setTimeInMillis(System.currentTimeMillis());
                                // 根据用户选择的时间来设置Calendar对象
                                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                c.set(Calendar.MINUTE,minute);
                                c.set(Calendar.SECOND,0);
                                showTime.setText(hourOfDay+":"+minute);
                            }
                        }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), false).show();
            }
        });
        //switch和闹钟服务
        button_edit=findViewById(R.id.day_edit_ok);
        switch_btn=findViewById(R.id.switch_btn2);
        switch_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    button_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            db=helper.getWritableDatabase();
                            if(editText1.getText().toString().length()!=0){
                                EditDate();
                                Toast.makeText(DaysEdit.this,"修改成功",Toast.LENGTH_SHORT).show();

                                intent2 = new Intent(DaysEdit.this, ClockActivity.class);
                                pendingIntent = PendingIntent.getActivity(DaysEdit.this, 0, intent2, 0);
                                if(flag==0){
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                                }else{
                                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
                                }
                                Toast.makeText(DaysEdit.this, "开启闹钟"+showTime.getText().toString()+editText1.getText().toString(), Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(DaysEdit.this,MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(DaysEdit.this,"日程为空",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    button_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            db=helper.getWritableDatabase();
                            if(editText1.getText().toString().length()!=0){
                                EditDate();
                                intent2 = new Intent(DaysEdit.this, ClockActivity.class);
                                intent2.putExtra(INFO_DAYS_CON4,editText1.getText().toString());
                                intent2.putExtra(INFO_DAYS_TIM4,showTime.getText().toString());
                                pendingIntent = PendingIntent.getActivity(DaysEdit.this, 0, intent2, 0);
                                alarmManager.cancel(pendingIntent);

                                Toast.makeText(DaysEdit.this,"修改成功",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(DaysEdit.this,MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(DaysEdit.this,"日程为空",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(DaysEdit.this, "取消闹钟", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //不按闹铃添加日程
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=helper.getWritableDatabase();
                if(editText1.getText().toString().length()!=0){
                    EditDate();
                    intent2 = new Intent(DaysEdit.this, ClockActivity.class);
                    intent2.putExtra(INFO_DAYS_CON4,editText1.getText().toString());
                    intent2.putExtra(INFO_DAYS_TIM4,showTime.getText().toString());
                    pendingIntent = PendingIntent.getActivity(DaysEdit.this, 0, intent2, 0);
                    Toast.makeText(DaysEdit.this,"修改成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(DaysEdit.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(DaysEdit.this,"日程为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getDate() {
        cal= Calendar.getInstance();
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
            default:
                break;
        }
    }
    private void EditDate() {
        MyDatabaseHelper helper=new MyDatabaseHelper(this,"daydata",null,1);
        db=helper.getReadableDatabase();
        Intent intent = this.getIntent();
        ContentValues values = new ContentValues();
        values.put("content",editText1.getText().toString());
        values.put("dayid",textView2.getText().toString());
        values.put("time",textView3.getText().toString());
        int editCount = db.update("daysdb",values,"content="+"'"+intent.getStringExtra(DaysActivity.INFO_DAYS_CON2)+"'",null);
        db.close();
        Toast.makeText(this,"更新了"+editCount+"条数据",Toast.LENGTH_SHORT).show();
    }
}
