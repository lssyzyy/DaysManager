package com.example.mypersonalmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyCostDatabaseHelper extends SQLiteOpenHelper {
    public static final String COST_TITLE = "cost_title";
    public static final String COST_DATE = "cost_date";
    public static final String COST_MONEY = "cost_money";
    public static final String IMOOC_COST = "imooc_cost";
    public MyCostDatabaseHelper(@Nullable Context context) {
        super(context, "imooc_daily",null, 1);
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

    public Cursor getAllCostData(){
        SQLiteDatabase database=getWritableDatabase();
        return database.query("IMOOC_COST",null,null,null,null,null,"COST_DATE "+" ASC");
    }
    public void deleteAllData(){
        SQLiteDatabase database=getWritableDatabase();
        database.delete("IMOOC_COST",null,null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}