package com.example.mypersonalmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public MyDatabaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "daydata", null, 1);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS daysdb" +
                "(dayid TEXT,"+
                "time TEXT," +
                "content TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists daysdb");
    }
}
