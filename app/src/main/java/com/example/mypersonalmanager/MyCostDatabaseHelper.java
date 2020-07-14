package com.example.mypersonalmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;

import androidx.annotation.Nullable;

public class MyCostDatabaseHelper extends SQLiteOpenHelper {
    public static final String COST_TITLE = "cost_title";
    public static final String COST_DATE = "cost_date";
    public static final String COST_MONEY = "cost_money";
    public static final String IMOOC_COST = "imooc_cost";
    private Context context;
    public MyCostDatabaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "imooc_daily",null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists IMOOC_COST("+
                "id integer primary key, "+
                "cost_title varchar, "+
                "cost_date varchar, "+
                "cost_money varchar)");
    }
    public void insertCost(BeanCost b){
        SQLiteDatabase database=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(COST_TITLE,b.getCostTitle());
        cv.put(COST_DATE,b.getCostDate());
        cv.put(COST_MONEY,b.getCostMoney());
        database.insert(IMOOC_COST,null,cv);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists IMOOC_COST");
    }
}
