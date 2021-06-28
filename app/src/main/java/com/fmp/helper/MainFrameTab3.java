package com.fmp.helper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.fmp.Logger;
import com.fmp.cloud.FMP_USER_NOTICE;
import com.fmp.helper.activity.NotificationActivity;
import com.fmp.helper.adapter.ListViewNotificationData.MyNotificationAdapter;
import com.fmp.helper.adapter.ListViewNotificationData.MyNotificationData;

import net.fmp.helper.R;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import es.dmoral.toasty.Toasty;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class MainFrameTab3 extends Fragment {
    private View view;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private MyNotificationAdapter MyAdapter;
    private boolean isRefresh;
    private long refreshTime = 0;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.main_frame_tab3, null, false);

            LinkedList<MyNotificationData> myData = new LinkedList<>();
            MyAdapter = new MyNotificationAdapter(getContext(), myData);

            mWaveSwipeRefreshLayout = view.findViewById(R.id.main_swipe);
            mWaveSwipeRefreshLayout.setWaveColor(getResources().getColor(R.color.colorPrimary));
            mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    isRefresh = true;
                    refreshData();
                }
            });

            ListView list = view.findViewById(R.id.mainframetab3ListView1);
            //list.setDivider(null);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        TextView Title = view.findViewById(R.id.mainframetab3itemTextView1);
                        TextView Time = view.findViewById(R.id.mainframetab3itemTextView2);
                        TextView Info = view.findViewById(R.id.mainframetab3itemTextView3);

                        //RichText.fromHtml(myData.get(position).getInfo()).into(Info);

                        Intent intent = new Intent();
                        intent.setClass(getContext(), NotificationActivity.class);
                        intent.putExtra("USE0", Title.getText().toString());
                        intent.putExtra("USE1", Time.getText().toString());
                        intent.putExtra("USE2", Info.getText().toString());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getActivity().startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            list.setAdapter(MyAdapter);
            //刷新数据
            refreshData();

        }
        return view;
    }

    private void refreshData() {
        if (refreshTime != 0 && System.currentTimeMillis() - refreshTime < 5000) {
            mWaveSwipeRefreshLayout.setRefreshing(false);
            isRefresh = false;
            Toasty.warning(getContext(), "您刷新的太快啦，休息一会儿吧~", Toast.LENGTH_SHORT, true).show();
            refreshTime = System.currentTimeMillis();
            return;
        } else {
            refreshTime = System.currentTimeMillis();
        }
        BmobQuery<FMP_USER_NOTICE> bmobQuery = new BmobQuery<>();
        bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);   // 先从网络读取数据，如果没有，再从缓存中获取。
        bmobQuery.findObjects(new FindListener<FMP_USER_NOTICE>() {
            @Override
            public void done(List<FMP_USER_NOTICE> list, BmobException e) {
                if (e == null) {
                    MyAdapter.removeAll();
                    for (FMP_USER_NOTICE notice : list) {
                        if (notice.getShow()) {
                            MyNotificationData data = new MyNotificationData();
                            data.setTitle(notice.getTitle());
                            data.setInfo(notice.getMessage());
                            data.setTime(notice.getUpdatedAt());
                            MyAdapter.add(data);
                        }
                    }
                    if (isRefresh) {
                        isRefresh = false;
                        Toasty.success(getContext(), "刷新成功", Toast.LENGTH_SHORT, true).show();
                    }
                } else {
                    Logger.LogInfo(this.getClass().getName(), e);
                    if (isRefresh) {
                        isRefresh = false;
                        Toasty.error(getContext(), "刷新失败", Toast.LENGTH_SHORT, true).show();
                    }
                }
                mWaveSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

}
