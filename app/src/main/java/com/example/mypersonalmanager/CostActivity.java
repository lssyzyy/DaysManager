package com.example.mypersonalmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CostActivity extends AppCompatActivity {
    private List<BeanCost> mlist=new ArrayList<>();;
    private MyCostDatabaseHelper myCostDatabaseHelper;
    private CostListAdapter mAdapter;
    private int year,month,day;
    private EditText title;
    private EditText money;
    private TextView date;
    private ListView costList;
    public static final String INFO_COST_TITLE = "INFO_COST_TITLE";
    public static final String INFO_COST_DATE = "INFO_COST_DATE";
    public static final String INFO_COST_MON = "INFO_COST_MON";
    ArrayList<String> costtitle=new ArrayList<String>();
    ArrayList<String> costdate=new ArrayList<String>();
    ArrayList<String> costmoney=new ArrayList<String>();
    private int flag;
    private RadioButton checked;
    private TextView total;
    SearchView msearchView;
    Handler myhandler=new Handler();
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cost);

        myCostDatabaseHelper=new MyCostDatabaseHelper(this,"imooc_daily",null,1);
        db=myCostDatabaseHelper.getWritableDatabase();
        initCostData();
        mAdapter=new CostListAdapter(this,R.layout.cost_list,mlist);
        costList=(ListView)findViewById(R.id.lv_main);
        costList.setAdapter(mAdapter);
        costList.setTextFilterEnabled(true);
        costList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                final BeanCost c=mlist.get(position);
                Intent intent=new Intent(CostActivity.this,EditCostData.class);
                String infotitle=c.getCostTitle();
                String infoDate=c.getCostDate();
                String infoMoney=c.getCostMoney();
                intent.putExtra(INFO_COST_TITLE,infotitle);
                intent.putExtra(INFO_COST_DATE,infoDate);
                intent.putExtra(INFO_COST_MON,infoMoney);
                startActivity(intent);
            }
        });
        //搜索功能
        msearchView=findViewById(R.id.day_search1);
        msearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                myhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String data = newText;
                        mlist.clear();
                        DataSearch(mlist, data);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                return false;
            }
        });
        //总额计算
        total=findViewById(R.id.total);
        int totalcost=0;
        String type;
        String money1;
        int flag=0;
        for(int i=0;i<mlist.size();i++){
            type=mlist.get(i).getCostMoney().substring(0,1);
            if(type.equals("+")==true){
                flag=1;
            }
            else if(type.equals("-")==true){
                flag=0;
            }
            money1=removeChartAt(mlist.get(i).getCostMoney(),0);
            money1=money1.substring(0,money1.length()-1);
            int b=Integer.parseInt(money1);
            if(flag==1){
                totalcost=totalcost+b;
            }
            else if(flag==0){
                totalcost=totalcost-b;
            }
        }
        String totalmoney=String.valueOf(totalcost);
        total.setText(totalmoney+"元");

        //添加收支
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
                final RadioGroup rg=(RadioGroup)view.findViewById(R.id.gp);
                builder.setView(view);
                builder.setTitle("新的记录");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BeanCost c=new BeanCost();
                        money.setInputType(EditorInfo.TYPE_CLASS_PHONE);
                        c.setCostTitle(title.getText().toString());
                        c.setCostDate((date.getMonth()+1)+"月"+date.getDayOfMonth()+"日,"+date.getYear());
                        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                RadioButton radioButton=group.findViewById(checkedId);
                            }
                        });
                        checked=(RadioButton)rg.findViewById(rg.getCheckedRadioButtonId());
                        if(checked.getText().equals("收入")==true){
                            c.setCostMoney("+"+money.getText().toString()+"元");
                        }
                        else {
                            c.setCostMoney("-"+money.getText().toString()+"元");
                        }
                        if(money.getText().toString().length()==0||title.getText().toString().length()==0){
                            Toast.makeText(CostActivity.this,"标题或钱款不能为空",Toast.LENGTH_SHORT).show();
                        }else if(money.getText().toString().length()!=0&&title.getText().toString().length()!=0){
                            myCostDatabaseHelper.insertCost(c);
                            mlist.add(c);
                            mAdapter.notifyDataSetChanged();
                            Intent intent2=new Intent(CostActivity.this,CostActivity.class);
                            startActivity(intent2);
                        }
                        int totalcost=0;
                        String type;
                        String money1;
                        int flag=0;
                        for(int i=0;i<mlist.size();i++){
                            type=mlist.get(i).getCostMoney().substring(0,1);
                            if(type.equals("+")==true){
                                flag=1;
                            }
                            else if(type.equals("-")==true){
                                flag=0;
                            }
                            money1=removeChartAt(mlist.get(i).getCostMoney(),0);
                            money1=money1.substring(0,money1.length()-1);
                            int b=Integer.parseInt(money1);
                            if(flag==1){
                                totalcost=totalcost+b;
                            }
                            else if(flag==0){
                                totalcost=totalcost-b;
                            }
                        }
                        String totalmoney=String.valueOf(totalcost);
                        total.setText(totalmoney+"元");
                    }
                });
                builder.setNegativeButton("Cancel",null);
                builder.create().show();
            }
        });
    }
    public static String removeChartAt(String s,int pos){
        return s.substring(0, pos) + s.substring(pos + 1);
    }
    private void initCostData(){
        mlist=queryAllContent();
        for(BeanCost d:mlist){
            if(d!=null){
                costtitle.add(d.getCostTitle());
                costdate.add(d.getCostDate());
                costmoney.add(d.getCostMoney());
            }
        }
    }
    public ArrayList<BeanCost> queryAllContent(){
        ArrayList<BeanCost> datas=new ArrayList<>();
        Cursor cursor=db.query("IMOOC_COST",null,null,null,null,null,null);
        while (cursor.moveToNext()){
            BeanCost data=null;
            String title = cursor.getString(cursor.getColumnIndex("cost_title"));
            String date = cursor.getString(cursor.getColumnIndex("cost_date"));
            String money = cursor.getString(cursor.getColumnIndex("cost_money"));
            data=new BeanCost(title,date,money);
            datas.add(data);
        }
        cursor.close();
        return datas;
    }
    //搜索功能
    private void DataSearch(List<BeanCost> datas,String data){
        int length=costtitle.size();
        for(int i=0;i<length;i++){
            if(costtitle.get(i).contains(data)||costdate.get(i).contains(data)||costmoney.get(i).contains(data)){
                BeanCost item=new BeanCost();
                item.setCostTitle(costtitle.get(i));
                item.setCostDate(costdate.get(i));
                item.setCostMoney(costmoney.get(i));
                datas.add(item);
            }
        }
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
            case R.id.mnu_day:
                Log.i(TAG,"日程搜索被点击");
                Intent intent2=new Intent(CostActivity.this,MainActivity.class);
                startActivity(intent2);
                break;
            case R.id.mnu_exit:
                break;
        }
        return true;
    }

}