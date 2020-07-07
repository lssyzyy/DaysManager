package com.example.mypersonalmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    private List<BeanDays> dayslist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaysAdapter adapter = new DaysAdapter(MainActivity.this,R.layout.days_list, dayslist);
        ListView listView = findViewById(R.id.hom_days_list);
        listView.setAdapter(adapter);
        adddays();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BeanDays fruit = dayslist.get(position);
                Toast.makeText(MainActivity.this,fruit.getContent(),Toast.LENGTH_LONG).show();
            }
        });

        FloatingActionButton button_add=findViewById(R.id.days_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"添加日程");
                Intent intent=new Intent(MainActivity.this,DaysManagerAdd.class);
                startActivity(intent);
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

    //表面的ListView做个测试垫一垫
    private void adddays() {
        for (int i = 0; i < 2; i++) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date curDate = new Date(System.currentTimeMillis());
            BeanDays apple = new BeanDays(R.drawable.launcher_icon, formatter.format(curDate), "apple");
            dayslist.add(apple);
            BeanDays banana = new BeanDays(R.drawable.launcher_icon, formatter.format(curDate), "banana");
            dayslist.add(banana);
            BeanDays orange = new BeanDays(R.drawable.launcher_icon, formatter.format(curDate), "orange");
            dayslist.add(orange);
        }
    }
}