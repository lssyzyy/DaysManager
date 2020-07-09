package com.example.mypersonalmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResActivity extends AppCompatActivity {
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private Button button;
    private SQLiteDatabase db;
    MyUserdataHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);

        editText1 = findViewById(R.id.Ed1);
        editText2 = findViewById(R.id.ED2);
        editText3 = findViewById(R.id.ED3);
        button = findViewById(R.id.BT1);
        helper=new MyUserdataHelper(this,"userdata",null,1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db=helper.getWritableDatabase();
                Instert();
                if(editText2.getText().toString().equals(editText3.getText().toString())&&editText1.getText().toString().length()!=0&&editText2.getText().toString().length()!=0&&editText3.getText().toString().length()!=0){
                    Toast.makeText(ResActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ResActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else if(editText1.getText().toString().length()==0||editText2.getText().toString().length()==0||editText3.getText().toString().length()==0){
                    Toast.makeText(ResActivity.this,"不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ResActivity.this,"密码不一致",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void Instert(){
        ContentValues contentValues=new ContentValues();
        contentValues.put("username",editText1.getText().toString());
        contentValues.put("pwd",editText2.getText().toString());
        db.insert("user",null,contentValues);
    }
}