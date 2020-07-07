package com.example.mypersonalmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MyDatabaseManager {
    private MyDatabaseHelper helper;
    private SQLiteDatabase db;

    public MyDatabaseManager(Context context) {
        helper=new MyDatabaseHelper(context);
        db=helper.getWritableDatabase();
    }
    public void addData(BeanDays data){
        addContent(data);
    }
    private void addContent(BeanDays data){
        ContentValues values=new ContentValues();
        values.put("imageid",data.getImageId());
        values.put("content",data.getContent());
        values.put("time",data.getTime());
        db.insert("daysdb",null,values);
    }
    public ArrayList<BeanDays> queryAllContent(){
        ArrayList<BeanDays> datas=new ArrayList<>();
        Cursor c=db.query("daysdb",null,null,null,null,null,null);
        while (c.moveToNext()){
            BeanDays data=null;
            int imageid=c.getInt(c.getColumnIndex("imageid"));
            String content=c.getString(c.getColumnIndex("content"));
            String time=c.getString(c.getColumnIndex("time"));
            data=new BeanDays(imageid,content,time);
            datas.add(data);
        }
        c.close();
        return datas;
    }
}

