package com.example.mypersonalmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CostListAdapter extends BaseAdapter {
    private List<BeanCost>clist;
    private Context mcontext;
    private LayoutInflater clayoutInflater;
    public CostListAdapter(Context context,List<BeanCost>list){
        mcontext=context;
        clist=list;
        clayoutInflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return clist.size();
    }

    @Override
    public Object getItem(int position) {
        return clist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=clayoutInflater.inflate(R.layout.cost_list,null);
            viewHolder.mTvCostTitle=convertView.findViewById(R.id.tv_title);
            viewHolder.mTvCostDate=convertView.findViewById(R.id.tv_date);
            viewHolder.mTvCostMoney=convertView.findViewById(R.id.tv_cost);
            convertView.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        BeanCost b=clist.get(position);
        viewHolder.mTvCostTitle.setText(b.getCostTitle());
        viewHolder.mTvCostDate.setText(b.getCostDate());
        viewHolder.mTvCostMoney.setText(b.getCostMoney());
        return convertView;
    }
    private static class ViewHolder{
        public TextView mTvCostTitle;
        public TextView mTvCostDate;
        public TextView mTvCostMoney;
    }
}
