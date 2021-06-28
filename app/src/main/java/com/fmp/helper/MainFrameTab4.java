package com.fmp.helper;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fmp.FMP_Tools;
import com.fmp.activity.LogInActivity;
import com.fmp.core.DeviceIdUtil;
import com.fmp.core.HelperCore;
import com.fmp.core.http.bean.HelperAccount;
import com.fmp.core.push.ClientPush;
import com.fmp.helper.activity.AboutActivity;
import com.fmp.helper.activity.MeInfoActivity;
import com.fmp.helper.activity.PluginActivity;
import com.fmp.helper.activity.SettingActivity;
import com.fmp.helper.adapter.ListViewMyInfoData.MyInfoAdapter;
import com.fmp.helper.adapter.ListViewMyInfoData.MyInfoData;
import com.fmp.view.CircleImageView;
import com.squareup.picasso.Picasso;

import net.fmp.helper.R;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import es.dmoral.toasty.Toasty;
import per.goweii.anylayer.AnimatorHelper;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.DialogLayer;
import per.goweii.anylayer.Layer;

import static android.app.Activity.RESULT_OK;

public class MainFrameTab4 extends Fragment implements AbsListView.OnScrollListener, OnItemClickListener {
    private View view;
    private ListView MyListView;
    private MyInfoAdapter MyAdapter;
    private CircleImageView HeadImage;
    private TextView HeadNickName, HeadSex, HeadAuthorize;
    private ImageView SexIcon;
    //private Button Login;
    private long refreshTime = 0;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.main_frame_tab4, null, false);

            //View layout = view.findViewById(R.id.main_tab4_layout);


            MyListView = view.findViewById(R.id.main_tab4_listview);

            //刷新
            //SmartSwipeRefresh.behindMode(MyListView, false).setDataLoader(this).setNoMoreData(true);

            View headerView = inflater.inflate(R.layout.main_frame_tab4_head, null, false);
            // 添加头布局
            MyListView.addHeaderView(headerView);

            LinkedList<MyInfoData> myData = new LinkedList<MyInfoData>();
            MyAdapter = new MyInfoAdapter(getContext(), myData);
            MyListView.setAdapter(MyAdapter);
            MyListView.setDivider(null);

            // 设置滚动监听
            MyListView.setOnScrollListener(this);
            //MyListView.setPullRefreshEnable(false);

            HeadImage = view.findViewById(R.id.main_tab4_head_image);
            HeadNickName = view.findViewById(R.id.main_tab4_head_nickname);
            HeadSex = view.findViewById(R.id.main_tab4_head_sex);
            SexIcon = view.findViewById(R.id.main_tab4_head_sex_icon);
            HeadAuthorize = view.findViewById(R.id.main_tab4_head_authorization);
            //Login = view.findViewById(R.id.main_tab4_login_button);
        }

        MyAdapter.refresh();
        MyAdapter.add(new MyInfoData(R.drawable.ic_person_white_24dp, "个人信息"));
        //MyAdapter.add(new MyInfoData(R.drawable.ic_item_verified, "授权信息"));
        MyAdapter.add(new MyInfoData(R.drawable.ic_item_status, "设备信息"));
        MyAdapter.add(new MyInfoData(R.drawable.ic_item_networks, "网络状态"));
        MyAdapter.add(new MyInfoData(R.drawable.ic_item_settings, "应用设置"));
        MyAdapter.add(new MyInfoData(R.drawable.ic_item_disclaimer, "免责声明"));
        MyAdapter.add(new MyInfoData(R.drawable.ic_item_about, "关于我们"));
        if (HelperCore.getInstance().getUserAbility() >= 2) {
            MyAdapter.add(new MyInfoData(R.drawable.ic_extension_white_24dp, "插件管理"));
        }

        MyListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View mView, int position, long id) {
        switch (position) {
            case 0: {
                HelperCore helperCore=HelperCore.getInstance();
                HelperAccount account=helperCore.getUserData();
                if (account!=null && !TextUtils.isEmpty(helperCore.getObjectId())) {
                    onRefreshData();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LogInActivity.class);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, 100);
                }
                break;
            }
            case 1: {
                HelperCore helperCore=HelperCore.getInstance();
                HelperAccount account=helperCore.getUserData();
                if (account!=null && !TextUtils.isEmpty(helperCore.getObjectId())) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), MeInfoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toasty.normal(getContext(), "请先点击头像登录").show();
                }
                break;
            }
            case 2:
                AnyLayer.dialog(getActivity())
                        .contentView(R.layout.dialog_normal_device_info)
                        .backgroundDimDefault()
                        .contentAnimator(new DialogLayer.AnimatorCreator() {
                            @Override
                            public Animator createInAnimator(View content) {
                                return AnimatorHelper.createLeftInAnim(content);
                            }

                            @Override
                            public Animator createOutAnimator(View content) {
                                return AnimatorHelper.createRightOutAnim(content);
                            }
                        })
                        .onVisibleChangeListener(new Layer.OnVisibleChangeListener() {
                            @Override
                            public void onShow(Layer layer) {
                                ((TextView) layer.getView(R.id.tv_dialog_title)).setText("设备信息");
                                ((TextView) layer.getView(R.id.tv_dialog_content)).setText(String.format(
                                        "手机厂商：%s\n手机型号：%s\n安卓版本：%s\n系统语言：%s\nABI：%s\nABI-32：%s\nABI-64 ：%s\n设备标识：%s",
                                        android.os.Build.BRAND,
                                        android.os.Build.MODEL,
                                        android.os.Build.VERSION.RELEASE,
                                        Locale.getDefault().getLanguage(),
                                        Arrays.toString(Build.SUPPORTED_ABIS),
                                        Arrays.toString(Build.SUPPORTED_32_BIT_ABIS),
                                        Arrays.toString(Build.SUPPORTED_64_BIT_ABIS),
                                        DeviceIdUtil.getDeviceId()));
                                ((TextView) layer.getView(R.id.tv_dialog_yes)).setText("知道了");
                            }

                            @Override
                            public void onDismiss(Layer layer) {

                            }
                        })
                        .onClickToDismiss(R.id.fl_dialog_yes)
                        .show();
                break;
            case 3:
                Bmob.getServerTime(new QueryListener<Long>() {
                    @Override
                    public void done(Long aLong, BmobException e) {
                        if (e == null) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String times = formatter.format(new Date(aLong * 1000L));
                            Toasty.warning(getContext(), "连接服务器成功，当前服务器时间为" + times, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Toasty.normal(getContext(), "连接服务器失败").show();
                        }
                    }
                });
                break;
            case 4: {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            }
            case 5:
                try {
                    HelperCore.getInstance().showAgreementDialog(getActivity(), true);
                } catch (IOException e) {
                    Toasty.info(getContext(), e.getMessage(), Toast.LENGTH_SHORT, true).show();
                }
                break;
            case 6: {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AboutActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            }
            case 7: {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PluginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            }
        }
    }

    private void onRefreshData() {
        if (refreshTime != 0 && System.currentTimeMillis() - refreshTime < 1000) {
            Toasty.warning(getContext(), "您刷新的太快啦，休息一会儿吧~", Toast.LENGTH_SHORT, true).show();
            return;
        } else {
            refreshTime = System.currentTimeMillis();
        }
        HelperCore.getInstance().checkUserVerify((e, userData) -> {
            if (e == null) {
                setData(userData);
                Toasty.normal(getContext(), "刷新成功").show();
            } else {
                Toasty.error(getContext(), "刷新失败！" + e.getMessage(), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    private void setData(HelperAccount userData) {
        if (userData != null) {
            HeadNickName.setText(userData.userName);
            Integer sex = userData.userSex;
            HeadSex.setText(String.format("%s%s", FMP_Tools.getSexString(sex), sex == 0 ? "一枚~" : "一只~"));
            SexIcon.setImageDrawable(getResources().getDrawable(sex == 0 ? R.drawable.user_female : R.drawable.user_male, null));
            HeadAuthorize.setText(userData.userType);
            if (!TextUtils.isEmpty(userData.headUrl)) {
                Picasso.get()
                        .load(userData.headUrl)
                        .into(HeadImage);
            }else {
                HeadImage.setImageResource(R.drawable.emp_logo);
            }
        } else {
            HeadNickName.setText("点击登录");
            HeadSex.setText("");
            SexIcon.setImageDrawable(null);
            HeadAuthorize.setText("");
            HeadImage.setImageResource(R.drawable.back_headicon);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setData(HelperCore.getInstance().getUserData());
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    // 最重要的方法，标题栏的透明度变化在这个方法实现
    @Override
    public void onScroll(AbsListView listView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        /*// 判断当前最上面显示的是不是头布局，因为Xlistview有刷新控件，所以头布局的位置是1，即第二个
        if (firstVisibleItem == 0) {
            // 获取头布局
            View view = listView.getChildAt(0);
            if (view != null) {
                // 获取头布局现在的最上部的位置的相反数
                int top = -view.getTop();
                // 获取头布局的高度
                int headerHeight = view.getHeight();
                // 满足这个条件的时候，是头布局在XListview的最上面第一个控件的时候，只有这个时候，我们才调整透明度
                if (top <= headerHeight && top >= 0) {
                    // 获取当前位置占头布局高度的百分比
                    double f = ((double) top / (double) headerHeight) * 1;
                    MainTab4Head.setAlpha((float) f);
                    // 通知标题栏刷新显示
                    MainTab4Head.invalidate();
                }
            }
        } else if (firstVisibleItem > 0) {
            MainTab4Head.setAlpha(1);
        } else {
            MainTab4Head.setAlpha(0);
        }*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            if (data.getBooleanExtra("login", false)) {
                Toasty.success(getContext(), "登录成功！", Toast.LENGTH_SHORT, true).show();
                //onRefreshData();
                HelperCore.getInstance().showBanDeviceDialog(getActivity());
                ClientPush.getInstance().checkAllPush(getContext(), ClientPush.TYPE_ALL, false);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
		

