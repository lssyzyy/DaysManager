package com.example.mypersonalmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private List<BeanDays> dayslist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initdays();//初始数据
        DaysAdapter adapter = new DaysAdapter(MainActivity.this,R.layout.days_list, dayslist);
        ListView listView = findViewById(R.id.hom_days_list);
        listView.setAdapter(adapter);

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
        // return super.onPrepareOptionsMenu(item);
    }

    private void initdays() {
        for (int i = 0; i < 2; i++) {//循环两遍，只添加一遍的话不足以充满屏幕
            SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd HH:mm");
            Date curDate = new Date(System.currentTimeMillis());
            BeanDays apple = new BeanDays(R.drawable.launcher_icon, formatter.format(curDate), "apple");//这里懒得找图片了，直接用系统图片
            dayslist.add(apple);
            BeanDays banana = new BeanDays(R.drawable.launcher_icon, formatter.format(curDate), "banana");
            dayslist.add(banana);
            BeanDays orange = new BeanDays(R.drawable.launcher_icon, formatter.format(curDate), "orange");
            dayslist.add(orange);
            BeanDays watermelon = new BeanDays(R.drawable.launcher_icon, formatter.format(curDate), "watermelon");
            dayslist.add(watermelon);
            BeanDays pear = new BeanDays(R.drawable.launcher_icon, formatter.format(curDate), "pear");
            dayslist.add(pear);
            BeanDays grape = new BeanDays(R.drawable.launcher_icon, formatter.format(curDate), "grape");
            dayslist.add(grape);
        }
    }
}