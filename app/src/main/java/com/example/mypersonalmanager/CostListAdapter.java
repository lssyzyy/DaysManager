package com.example.mypersonalmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CostListAdapter extends ArrayAdapter<BeanCost> {
    private int resourceid;
    public CostListAdapter(Context context, int id, List<BeanCost> objects) {
        super(context, id,objects);
        resourceid=id;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        BeanCost data=getItem(position);
        View view;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceid,null);
        }
        else{
            view=convertView;
        }
        TextView title=view.findViewById(R.id.tv_title);
        TextView date=view.findViewById(R.id.tv_date);
        TextView money=view.findViewById(R.id.tv_cost);
        title.setText(data.getCostTitle());
        date.setText(data.getCostDate());
        money.setText(data.getCostMoney());
        return view;
    }
}
