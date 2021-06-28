package com.fmp.helper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.billy.android.swipe.SmartSwipe;
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer;
import com.zzhoujay.richtext.RichText;

import net.fmp.helper.R;


public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_notification);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = findViewById(R.id.toolbar_notification);
        toolbar.setTitle("消息通知");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //activity侧滑返回
        SmartSwipe.wrap(this)
                .addConsumer(new ActivitySlidingBackConsumer(this))
                //设置联动系数
                .setRelativeMoveFactor(0.5F)
                //指定可侧滑返回的方向，如：enableLeft() 仅左侧可侧滑返回
                .enableLeft()
                .enableRight();

        String title = getIntent().getStringExtra("USE0");
        String timex = getIntent().getStringExtra("USE1");
        String infox = getIntent().getStringExtra("USE2");

        ((Button) findViewById(R.id.activitylayoutnotificationButton1)).setText(title.intern());
        ((TextView) findViewById(R.id.activitylayoutnotificationTextView1)).setText(timex.intern());
        TextView Message = findViewById(R.id.activitylayoutnotificationTextView2);
        //Message.setText(infox.intern());
        RichText.fromHtml(infox).into(Message);
    }


}
