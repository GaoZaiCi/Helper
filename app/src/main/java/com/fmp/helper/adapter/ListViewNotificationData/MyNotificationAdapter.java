package com.fmp.helper.adapter.ListViewNotificationData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.fmp.helper.R;

import java.util.LinkedList;

public class MyNotificationAdapter extends BaseAdapter {

    private Context con;
    private LinkedList<MyNotificationData> Data;
    private ViewHolder holder;

    public MyNotificationAdapter(Context con, LinkedList<MyNotificationData> data) {
        this.con = con;
        this.Data = data;
    }

    @Override
    public int getCount() {
        // TODO: Implement this method
        return Data.size();
    }

    @Override
    public Object getItem(int p1) {
        // TODO: Implement this method
        return null;
    }

    @Override
    public long getItemId(int p1) {
        // TODO: Implement this method
        return p1;
    }

    int i = 0;

    @Override
    public View getView(int p, View view, ViewGroup p3) {
        view = LayoutInflater.from(con).inflate(R.layout.main_frame_tab3_item, p3, false);
        holder = new ViewHolder();
        view.setTag(holder);

        int width = ((AppCompatActivity) con).getWindow().getWindowManager().getDefaultDisplay().getWidth();

        holder.title = view.findViewById(R.id.mainframetab3itemTextView1);
        holder.time = view.findViewById(R.id.mainframetab3itemTextView2);
        holder.info = view.findViewById(R.id.mainframetab3itemTextView3);
        holder.title.setText(Data.get(p).getTitle());
        holder.time.setText(Data.get(p).getTime());
        holder.info.setText(Data.get(p).getInfo());
		/*Animation Left= new TranslateAnimation(-width , 0 , 0 , 0);
		 Left.setInterpolator(con, android.R.anim.anticipate_overshoot_interpolator);
		 Left.setFillAfter(false);
		 Left.setDuration(600);
		 Animation Right= new TranslateAnimation(width , 0 , 0 , 0);
		 Right.setInterpolator(con, android.R.anim.anticipate_overshoot_interpolator);
		 Right.setFillAfter(false);
		 Right.setDuration(600);
		 if(i==0){
		 i=1;
		 view.startAnimation(Left);
		 }else if(i==1){
		 i=0;
		 view.startAnimation(Right);
		 }*/
        return view;
    }

    public class ViewHolder {
        TextView title, time, info;
    }

    public void add(MyNotificationData data) {
        if (Data == null) {
            Data = new LinkedList<MyNotificationData>();
        }
        //添加一条数据
        Data.add(data);
        //刷新
        notifyDataSetChanged();
    }

    public void removeAll() {
        if (Data != null) {
            //清除List的全部数据
            Data.clear();
            //刷新
            notifyDataSetChanged();
        }
    }


}
