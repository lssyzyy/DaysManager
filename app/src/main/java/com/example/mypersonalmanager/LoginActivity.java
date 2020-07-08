package com.example.mypersonalmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText pwd;
    private Button login;
    private Button res;
    private SQLiteDatabase db;
    private CheckBox rem_pwd;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    MyUserdataHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
        username=findViewById(R.id.Ed1);
        pwd=findViewById(R.id.ED2);
        login=findViewById(R.id.BT1);
        res=findViewById(R.id.BT2);
        rem_pwd=(CheckBox)findViewById(R.id.remember_pass);
        helper=new MyUserdataHelper(this,"userdata",null,1);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=username.getText().toString();
                String pass=pwd.getText().toString();
                editor=pref.edit();
                if (login(name,pass)){
                    Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    Intent user=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(user);
                    if(rem_pwd.isChecked()){
                        editor.putBoolean("remember_pwd",true);
                        editor.putString("username",name);
                        editor.putString("password",pass);
                    }else {
                        editor.clear();
                    }
                    editor.commit();
                }
                else {
                    Toast.makeText(LoginActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,ResActivity.class);
                startActivity(intent);
            }
        });
        boolean isRemeber=pref.getBoolean("remember_pwd",false);
        if(isRemeber){
            String name=pref.getString("username","");
            String password=pref.getString("password","");
            username.setText(name);
            pwd.setText(password);
            rem_pwd.setChecked(true);
        }
    }
    public boolean login(String username,String pwd){
        db=helper.getReadableDatabase();
        String sql="select * from user where username=? and pwd=?";
        Cursor cursor=db.rawQuery(sql,new String[]{username,pwd});
        if(cursor.moveToFirst()){
            cursor.close();
            return true;
        }
        return false;
    }
}