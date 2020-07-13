package com.example.mypersonalmanager;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class EditCostData extends AppCompatActivity implements View.OnClickListener{
    private SQLiteDatabase db;
    private EditText editText1,editText2;
    private TextView showDate;
    private FloatingActionButton button1,button2,button3;
    private Calendar cal;
    private int year,month,day,hour,minute;
    MyCostDatabaseHelper helper;
    private RadioGroup gp;
    private RadioButton checked;
    private RadioButton radioButton1,radioButton2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cost_edit);
        editText1=findViewById(R.id.et_cost_title1);
        editText2=findViewById(R.id.et_cost_money1);
        showDate=findViewById(R.id.dp_cost_data1);
        button1=findViewById(R.id.bt_comf);
        button2=findViewById(R.id.bt_delete);
        button3=findViewById(R.id.bt_cancel);
        radioButton1=findViewById(R.id.shouru1);
        radioButton2=findViewById(R.id.zhichu1);
        gp=findViewById(R.id.gp1);
        Intent intent=this.getIntent();
        String infotitle=intent.getStringExtra(CostActivity.INFO_COST_TITLE);
        String infodate=intent.getStringExtra(CostActivity.INFO_COST_DATE);
        String infomoney=intent.getStringExtra(CostActivity.INFO_COST_MON);
        String type=infomoney.substring(0,1);
        if (type.equals("+")==true){
            radioButton1.setChecked(true);
        }
        else {
            radioButton2.setChecked(true);
        }
        String infomoneyr=removeChartAt(infomoney,0);
        infomoneyr=infomoneyr.substring(0,infomoneyr.length()-1);
        editText1.setText(infotitle);
        editText2.setText(infomoneyr);
        showDate.setText(infodate);
        gp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button=group.findViewById(checkedId);
            }
        });

        getDate();
        showDate.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dp_cost_data1:
                DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        showDate.setText((++month)+"-"+day+","+year);
                    }
                };
                DatePickerDialog dialog=new DatePickerDialog(EditCostData.this,DatePickerDialog.THEME_HOLO_LIGHT,listener,year,month,day);
                dialog.show();
                break;
            case R.id.bt_comf:
                checked=(RadioButton)gp.findViewById(gp.getCheckedRadioButtonId());
                if(checked.getText().equals("收入")==true){
                    editText2.setText("+"+editText2.getText().toString()+"元");
                }
                else {
                    editText2.setText("-"+editText2.getText().toString()+"元");
                }
                EditData();
                Intent intent=new Intent(EditCostData.this,CostActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_delete:
                DeleteData();
                Intent intent1=new Intent(EditCostData.this,CostActivity.class);
                startActivity(intent1);
                break;
            case R.id.bt_cancel:
                Intent intent2=new Intent(EditCostData.this,CostActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
    private void getDate() {
        cal= Calendar.getInstance();
        year=cal.get(Calendar.YEAR);       //获取年月日时分秒
        month=cal.get(Calendar.MONTH);   //获取到的月份是从0开始计数
        day=cal.get(Calendar.DAY_OF_MONTH);
    }
    private void EditData(){
        MyCostDatabaseHelper helper=new MyCostDatabaseHelper(this);
        SQLiteDatabase db=helper.getReadableDatabase();
        Intent intent=this.getIntent();
        ContentValues values=new ContentValues();
        values.put("cost_title",editText1.getText().toString());
        values.put("cost_date",showDate.getText().toString());
        values.put("cost_money",editText2.getText().toString());
        int editCount=db.update("IMOOC_COST",values,"cost_title="+"'"+intent.getStringExtra(CostActivity.INFO_COST_TITLE)+"'",null);
        db.close();
    }
    private void DeleteData(){
        MyCostDatabaseHelper helper=new MyCostDatabaseHelper(this);
        SQLiteDatabase db=helper.getReadableDatabase();
        Intent intent=this.getIntent();
        String where="cost_title=?";
        String[] value=new String[]{intent.getStringExtra(CostActivity.INFO_COST_TITLE)};
        int deleteCount=db.delete("IMOOC_COST",where,value);
        db.close();
    }
    public static String removeChartAt(String s,int pos){
        return s.substring(0, pos) + s.substring(pos + 1);
    }
}
