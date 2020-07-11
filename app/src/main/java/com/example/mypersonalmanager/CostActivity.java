package com.example.mypersonalmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CostActivity extends AppCompatActivity {
    private List<BeanCost> mlist;
    private MyCostDatabaseHelper myCostDatabaseHelper;
    private CostListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost);

        myCostDatabaseHelper=new MyCostDatabaseHelper(this);
        mlist=new ArrayList<>();
        ListView costList=(ListView)findViewById(R.id.lv_main);
        initCostData();
        mAdapter=new CostListAdapter(this,mlist);
        costList.setAdapter(mAdapter);

        FloatingActionButton fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(CostActivity.this);

                LayoutInflater inflater=LayoutInflater.from(CostActivity.this);
                View view=inflater.inflate(R.layout.cost_add,null);
                final EditText title=(EditText)view.findViewById(R.id.et_cost_title);
                final EditText money=(EditText)view.findViewById(R.id.et_cost_money);
                final DatePicker date=(DatePicker)view.findViewById(R.id.dp_cost_data);
                builder.setView(view);
                builder.setTitle("新的记录");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BeanCost c=new BeanCost();
                        c.setCostTitle(title.getText().toString());
                        c.setCostMoney(money.getText().toString());
                        c.setCostDate(date.getYear()+"-"+(date.getMonth()+1)+"-"+date.getDayOfMonth());
                        if(c.getCostTitle().length()==0&&c.getCostMoney().length()==0){
                            Toast.makeText(CostActivity.this,"标题或钱款不能为空",Toast.LENGTH_SHORT).show();
                        }else {
                            myCostDatabaseHelper.insertCost(c);
                            mlist.add(c);
                            mAdapter.notifyDataSetChanged();
                            Toast.makeText(CostActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.create().show();
            }
        });
    }
    private void initCostData(){
        Cursor cursor=myCostDatabaseHelper.getAllCostData();
        if (cursor!=null){
            while (cursor.moveToNext()){
                BeanCost b=new BeanCost();
                b.setCostTitle(cursor.getString(cursor.getColumnIndex("cost_title")));
                b.setCostDate(cursor.getString(cursor.getColumnIndex("cost_date")));
                b.setCostMoney(cursor.getString(cursor.getColumnIndex("cost_money")));
                mlist.add(b);
            }
            cursor.close();
        }
    }
}