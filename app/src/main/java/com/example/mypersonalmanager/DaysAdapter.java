package com.example.mypersonalmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class DaysAdapter extends ArrayAdapter<BeanDays> {
    private int resourceid;
    public DaysAdapter(Context context, int id, List<BeanDays> objects) {
        super(context, id,objects);
        resourceid=id;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        BeanDays data=getItem(position);
        View view;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceid,null);
        }
        else{
            view=convertView;
        }
        ImageView daysImage=view.findViewById(R.id.days_img);
        TextView daysContent=view.findViewById(R.id.days_con);
        TextView daysTime=view.findViewById(R.id.days_time);
        daysImage.setImageResource(data.getImageId());
        daysContent.setText(data.getContent());
        daysTime.setText(data.getTime());
        return view;
    }
}
