package com.fmp.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.fmp.view.SmartScrollView;

import net.fmp.helper.R;

public class HelperDialog implements View.OnClickListener {
    private Context mContext;
    private View view;
    private TextView Title;
    private TextView Message;
    private LinearLayout MessageContent;
    private SmartScrollView ScrollView;
    private Button Button1;
    private Button Button2;
    private Button Button3;
    private AlertDialog dialog;

    public HelperDialog(Context context) {
        mContext = context;
        initLayoutView();
    }

    private void initLayoutView() {
        view = LayoutInflater.from(mContext).inflate(R.layout.fmp_alert_dialog, null);
        Title = view.findViewById(R.id.tv_alert_title);
        Message = view.findViewById(R.id.tv_alert_message);
        MessageContent = view.findViewById(R.id.tv_alert_message_content);
        ScrollView = view.findViewById(R.id.sv_alert_view);
        Button1 = view.findViewById(R.id.btn_alert_button1);
        Button2 = view.findViewById(R.id.btn_alert_button2);
        Button3 = view.findViewById(R.id.btn_alert_button3);

        Title.setVisibility(View.GONE);
        Message.setVisibility(View.GONE);
        Button1.setVisibility(View.GONE);
        Button2.setVisibility(View.GONE);
        Button3.setVisibility(View.GONE);

        initDialog();
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        dialog = builder.create();
    }

    public View getLayoutView() {
        return view;
    }

    public void setTitle(CharSequence text) {
        Title.setVisibility(View.VISIBLE);
        Title.setText(text);
    }

    public TextView getTitleView() {
        return Title;
    }

    public void setMessage(CharSequence text) {
        Message.setVisibility(View.VISIBLE);
        Message.setText(text);
    }

    public TextView getMessageView() {
        return Message;
    }

    public void addView(View view) {
        MessageContent.addView(view);
    }

    public SmartScrollView getScrollView() {
        return ScrollView;
    }

    public void setButton1(CharSequence text, View.OnClickListener clickListener) {
        Button1.setVisibility(View.VISIBLE);
        Button1.setText(text);
        if (clickListener == null)
            clickListener = this::onClick;
        Button1.setOnClickListener(clickListener);
    }

    public Button getButton1View() {
        return Button1;
    }

    public void setButton2(CharSequence text, View.OnClickListener clickListener) {
        Button2.setVisibility(View.VISIBLE);
        Button2.setText(text);
        if (clickListener == null)
            clickListener = this::onClick;
        Button2.setOnClickListener(clickListener);
    }

    public Button getButton2View() {
        return Button2;
    }

    public void setButton3(CharSequence text, View.OnClickListener clickListener) {
        Button3.setVisibility(View.VISIBLE);
        Button3.setText(text);
        if (clickListener == null)
            clickListener = this::onClick;
        Button3.setOnClickListener(clickListener);
    }

    public Button getButton3View() {
        return Button3;
    }

    public void setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
    }

    public void dismiss() {
        dialog.dismiss();
    }

    public AlertDialog show() {
        dialog.show();
        return dialog;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
