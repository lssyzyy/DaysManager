package com.example.mypersonalmanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.content.ContentValues.TAG;

public class DaysActivity extends AppCompatActivity {
    private TextView textView1,textView2,textView3;
    private SQLiteDatabase db;
    public static final String INFO_DAYS_CON2 = "INFO_DAYS_CON2";
    public static final String INFO_DAYS_DATE2 = "INFO_DAYS_DATE2";
    public static final String INFO_DAYS_TIME2 = "INFO_DAYS_TIME2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.days_main);

        textView1=findViewById(R.id.day_main_content);
        textView2=findViewById(R.id.day_main_showdate);
        textView3=findViewById(R.id.day_main_showtime);
        Intent intent = this.getIntent();
        String infocontent = intent.getStringExtra(MainActivity.INFO_DAYS_CON);
        String infodate = intent.getStringExtra(MainActivity.INFO_DAYS_DATE);
        String infotime = intent.getStringExtra(MainActivity.INFO_DAYS_TIME);
        textView1.setText(infocontent);
        textView2.setText(infodate);
        textView3.setText(infotime);

        FloatingActionButton button_delete=findViewById(R.id.daymanager_delete);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"删除日程");
                DeleteDate(v);
                Intent intent=new Intent(DaysActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton button_edit=findViewById(R.id.daymanager_edit);
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"编辑日程");
                Intent intent2=new Intent(DaysActivity.this,DaysEdit.class);
                String infocontent2 = textView1.getText().toString();
                String infodate2 = textView2.getText().toString();
                String infotime2 = textView3.getText().toString();
                intent2.putExtra(INFO_DAYS_CON2, infocontent2);
                intent2.putExtra(INFO_DAYS_DATE2, infodate2);
                intent2.putExtra(INFO_DAYS_TIME2, infotime2);
                startActivity(intent2);
            }
        });
    }
    //删除数据
    public void DeleteDate(View view) {
        MyDatabaseHelper helper=new MyDatabaseHelper(this,"daydata",null,1);
        db=helper.getReadableDatabase();
        String where="content=?";
        String[] value=new String[]{textView1.getText().toString()};
        int deleteCount = db.delete("daysdb",where,value);
        db.close();
        Toast.makeText(this,"删除了"+deleteCount+"条数据",Toast.LENGTH_SHORT).show();
    }
}
