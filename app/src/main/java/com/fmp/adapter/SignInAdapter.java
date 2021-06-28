package com.fmp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fmp.util.DensityUtil;

import java.util.List;

public class SignInAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> SigninUsers;

    public SignInAdapter(Context context, List<String> users) {
        mContext = context;
        SigninUsers = users;
    }

    @Override
    public int getCount() {
        return SigninUsers.size();
    }

    @Override
    public Object getItem(int i) {
        return SigninUsers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView = new TextView(mContext);
        textView.setText("第" + (i + 1) + "名 " + SigninUsers.get(i));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getColor(i));
        textView.setTextSize(getTextSize(i));
        return textView;
    }

    private int getColor(int i) {
        switch (i) {
            case 0:
                return Color.RED;
            case 1:
                return Color.YELLOW;
            case 2:
                return Color.BLUE;
            default:
                return Color.MAGENTA;
        }
    }

    private int getTextSize(int i) {
        switch (i) {
            case 0:
                return DensityUtil.dip2px(mContext, 13);
            case 1:
                return DensityUtil.dip2px(mContext, 11);
            case 2:
                return DensityUtil.dip2px(mContext, 8);
            default:
                return DensityUtil.dip2px(mContext, 5);
        }
    }
}
