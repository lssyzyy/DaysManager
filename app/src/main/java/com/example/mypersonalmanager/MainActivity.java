package com.example.mypersonalmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private ListView listView;
    private DaysAdapter adapter;
    private TextView navday,textView1,textView2,textView3;
    private TabHost tab;
    MyDatabaseHelper helper;
    private List<BeanDays> dayslist=new ArrayList<>();
    public static final String INFO_DAYS_CON = "INFO_DAYS_CON";
    public static final String INFO_DAYS_DATE = "INFO_DAYS_DATE";
    public static final String INFO_DAYS_TIME = "INFO_DAYS_TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper=new MyDatabaseHelper(this,"daydata",null,1);
        db=helper.getWritableDatabase();
        initDatas();
        FloatingActionButton button_add=findViewById(R.id.days_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"添加日程");
                Intent intent=new Intent(MainActivity.this,DaysManagerAdd.class);
                startActivity(intent);
            }
        });

        //显示每日日程
        adapter = new DaysAdapter(MainActivity.this,R.layout.days_list, dayslist);
        listView = findViewById(R.id.days_list1);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BeanDays fruit = dayslist.get(position);
                Toast.makeText(MainActivity.this,fruit.getContent(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this,DaysActivity.class);
                String infocontent = fruit.getContent();
                String infodate = fruit.getDayid();
                String infotime = fruit.getTime();
                intent.putExtra(INFO_DAYS_CON, infocontent);
                intent.putExtra(INFO_DAYS_DATE, infodate);
                intent.putExtra(INFO_DAYS_TIME, infotime);
                startActivity(intent);
            }
        });

        //实时获取日期(待修改)
        TextView navDay=findViewById(R.id.nav_days);
        navDay.setText("今天"+"("+new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()))+")");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mnu_pay:
                Log.i(TAG,"个人收支记账管理被点击");
                Intent intent=new Intent(MainActivity.this,CostActivity.class);
                startActivity(intent);
                break;
            case R.id.mnu_day:
                Log.i(TAG,"日程管理被点击");
                break;
            case R.id.mnu_exit:
                break;
        }
        return true;
    }

    private void initDatas() {
        Cursor cursor = db.rawQuery("select * from daysdb",null);
        while(cursor.moveToNext()){
            String dayid = cursor.getString(cursor.getColumnIndex("dayid"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String daycontent = cursor.getString(cursor.getColumnIndex("content"));
            BeanDays datas= new BeanDays(dayid,time,daycontent);
            dayslist.add(datas);
        }
        cursor.close();
    }

    //上个月字符串
    public void getLastMouth(Calendar calendar){
        DateFormat df = new SimpleDateFormat("YYYY/MM");
        calendar.add(Calendar.MONTH, -1);
        Date date = (Date) calendar.getTime();
        String format = df.format(date);
        System.out.println(format);
    }
    //下个月字符串
    public void getNextMouth(Calendar calendar){
        DateFormat df = new SimpleDateFormat("YYYY/MM");
        calendar.add(Calendar.MONTH, 1);
        Date date = (Date) calendar.getTime();
        String format = df.format(date);
        System.out.println(format);
    }
    //根据年月获取对应月份天数
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
    //获取当天周几数
    public static String getDaysOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "周日" ;
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
            default:
                return "";
        }
    }
}