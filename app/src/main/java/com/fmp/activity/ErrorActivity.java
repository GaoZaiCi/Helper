package com.fmp.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fmp.FMP_Toast;
import com.fmp.Logger;
import com.fmp.util.SystemUtil;
import com.fmp.util.VeDate;

import net.fmp.helper.R;


public class ErrorActivity extends AppCompatActivity implements OnClickListener {
    private Context Context = this;
    private LinearLayout LinearLayout;
    private String ClipboardMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClipboardMsg = VeDate.getStringDate() + getIntent().getStringExtra("ERROR_CAUSE") + getIntent().getStringExtra("ERROR_MSG");
        try {
            //XmlPullParser paser = FMP_Util.getLayoutXmlPullParser(this, "assets/FMP_Resources/Xml/fmp_error_helper.xml");
            LinearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.fmp_error_helper, null);

            TextView ERROR_DATE = LinearLayout.findViewWithTag("ERROR_DATE");
            ERROR_DATE.setText(VeDate.getStringDate());

            TextView SystemVersion = LinearLayout.findViewWithTag("SystemVersion");
            SystemVersion.setText(SystemUtil.getSystemVersion());

            TextView DeviceBrand = LinearLayout.findViewWithTag("DeviceBrand");
            DeviceBrand.setText(SystemUtil.getDeviceBrand());

            TextView SystemModel = LinearLayout.findViewWithTag("SystemModel");
            SystemModel.setText(SystemUtil.getSystemModel());

            TextView ERROR_CAUSE = LinearLayout.findViewWithTag("ERROR_CAUSE");
            ERROR_CAUSE.setText(getIntent().getStringExtra("ERROR_CAUSE"));

            TextView ERROR_MSG = LinearLayout.findViewWithTag("ERROR_MSG");
            ERROR_MSG.setText(getIntent().getStringExtra("ERROR_MSG"));
            ERROR_MSG.setOnClickListener(this::onClick);
        } catch (Throwable e) {
            e.printStackTrace();
            LinearLayout = new LinearLayout(Context);
        }
        setContentView(LinearLayout);
    }

    @Override
    public void onClick(View view) {
        try {
            //获取剪贴板管理器：
            ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", ClipboardMsg);
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
            FMP_Toast.Show_Toast(view.getContext(), "复制日志信息成功");
        } catch (NullPointerException e) {
            Logger.toString(e);
        }

    }

}
