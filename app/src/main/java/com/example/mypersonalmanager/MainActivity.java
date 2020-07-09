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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private ListView listView;
    private DaysAdapter adapter;
    MyDatabaseHelper helper;
    private List<BeanDays> dayslist=new ArrayList<>();

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

        adapter = new DaysAdapter(MainActivity.this,R.layout.days_list, dayslist);
        listView = findViewById(R.id.hom_days_list);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BeanDays fruit = dayslist.get(position);
                Toast.makeText(MainActivity.this,fruit.getContent(),Toast.LENGTH_LONG).show();
            }
        });



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
                break;
            case R.id.mnu_days:
                Log.i(TAG,"个人日程管理被点击");
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

}