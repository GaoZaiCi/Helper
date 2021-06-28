package com.fmp.helper.adapter.ListViewMyInfoData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.fmp.helper.R;

import java.util.LinkedList;

public class MyInfoAdapter extends BaseAdapter {

    private Context con;
    private LinkedList<MyInfoData> Data;
    private ViewHolder holder;

    public MyInfoAdapter(Context con, LinkedList<MyInfoData> data) {
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
        view = LayoutInflater.from(con).inflate(R.layout.main_frame_tab4_item, p3, false);
        holder = new ViewHolder();
        view.setTag(holder);

        int width = ((AppCompatActivity) con).getWindow().getWindowManager().getDefaultDisplay().getWidth();

        holder.text = view.findViewById(R.id.mainframetab4itemTextView1);
        holder.text.setText(Data.get(p).getStr().intern());
        holder.imv = view.findViewById(R.id.mainframetab4itemImageView1);
        holder.imv.setImageResource(Data.get(p).getRes());
        Animation Left = new TranslateAnimation(-width, 0, 0, 0);
        Left.setInterpolator(con, android.R.anim.anticipate_overshoot_interpolator);
        Left.setFillAfter(false);
        Left.setDuration(600);
        Animation Right = new TranslateAnimation(width, 0, 0, 0);
        Right.setInterpolator(con, android.R.anim.anticipate_overshoot_interpolator);
        Right.setFillAfter(false);
        Right.setDuration(600);
        if (i == 0) {
            i = 1;
            //view.startAnimation(Left);
        } else if (i == 1) {
            i = 0;
            //view.startAnimation(Right);
        }
        return view;
    }

    public class ViewHolder {
        TextView text;
        ImageView imv;
    }

    public void add(MyInfoData data) {
        if (Data == null) {
            Data = new LinkedList<MyInfoData>();
        }
        //添加一条数据
        Data.add(data);
        //刷新
        notifyDataSetChanged();
    }

    public void refresh() {
        if (Data != null) {
            //清除List的全部数据
            Data.clear();
            //刷新
            notifyDataSetChanged();
        }
    }


}
