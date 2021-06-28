package com.fmp.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.fmp.Logger;
import com.fmp.util.FileSizeUtil;

import net.fmp.helper.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.fmp.util.FileSizeUtil.FormetFileSize;

public class UpdateManager implements View.OnClickListener {
    //获取文件大小
    private static final int FLAG_GET_FILE_SIZE = 1;
    //更新下载进度条
    private static final int FLAG_UPDATE_PROGRESS = 2;
    //下载成功
    private static final int FLAG_DOWNLOAD_SUCCESS = 3;
    //下载停止
    private static final int FLAG_DOWNLOAD_STOP = 4;
    //下载失败
    private static final int FLAG_DOWNLOAD_ERROR = -1;
    //重新下载
    private static final String TAG_REDOWNLOAD = "REDOWNLOAD";
    //取消下载
    private static final String TAG_CEDOWNLOAD = "CEDOWNLOAD";
    //退出下载
    private static final String TAG_ETDOWNLOAD = "ETDOWNLOAD";
    //安装apk
    private static final String TAG_INSTALLAPK = "INSTALLAPK";
    private Context mContext;
    private AlertDialog MainDialog;
    private String fileName;
    private String fileUrl;
    //文件大小
    private int FileSize;
    //标题
    private TextView tv_Title;
    //副标题
    private TextView tv_SubTitle;
    //下载进度条
    private ProgressBar pb_Loading;
    private Button btn_Cancel;
    private Button btn_Exit;
    private boolean isDownLoadFile = true;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //super.handleMessage(msg);
            switch (msg.what) {
                case FLAG_GET_FILE_SIZE:
                    //保存大小
                    FileSize = (int) msg.obj;
                    //设置最大进度条
                    pb_Loading.setMax(FileSize);
                    pb_Loading.setVisibility(View.VISIBLE);
                    //设置标题
                    tv_Title.setText("开始下载");
                    //设置副标题
                    tv_SubTitle.setText(String.format("总大小%s", FormetFileSize(FileSize)));
                    tv_SubTitle.setVisibility(View.VISIBLE);
                    isDownLoadFile = true;
                    break;
                case FLAG_UPDATE_PROGRESS:
                    //设置进度条
                    pb_Loading.setProgress((int) msg.obj);
                    String Percentage = (((long) pb_Loading.getProgress() * 100) / (long) pb_Loading.getMax()) + "%";
                    tv_Title.setText(String.format("下载中...%s", Percentage));
                    //设置显示信息
                    String FileSizes = FileSizeUtil.FormetFileSize(pb_Loading.getProgress()) + "/" + FileSizeUtil.FormetFileSize(FileSize);
                    tv_SubTitle.setText(String.format("已下载%s", FileSizes));
                    break;
                case FLAG_DOWNLOAD_ERROR:
                    tv_Title.setText("下载出现错误");
                    tv_SubTitle.setVisibility(View.GONE);
                    pb_Loading.setProgress(0);
                    pb_Loading.setVisibility(View.GONE);
                    btn_Cancel.setText("重新下载");
                    btn_Cancel.setTag(TAG_REDOWNLOAD);
                    break;
                case FLAG_DOWNLOAD_SUCCESS:
                    tv_Title.setText("下载完成");
                    tv_SubTitle.setVisibility(View.GONE);
                    btn_Cancel.setText("安装");
                    btn_Exit.setText("退出");
                    pb_Loading.setProgress(0);
                    pb_Loading.setVisibility(View.GONE);
                    btn_Cancel.setTag(TAG_INSTALLAPK);
                    installApk(mContext, fileName);
                    break;
                case FLAG_DOWNLOAD_STOP:
                    tv_Title.setText("已取消下载");
                    tv_SubTitle.setVisibility(View.GONE);
                    pb_Loading.setProgress(0);
                    pb_Loading.setVisibility(View.GONE);
                    btn_Cancel.setText("重新下载");
                    btn_Cancel.setTag(TAG_REDOWNLOAD);
                    break;
            }
        }
    };

    public UpdateManager(Context ctx, String fileUrl) {
        this.mContext = ctx;
        this.fileUrl = fileUrl;
        if (ctx == null || fileUrl == null || fileUrl.isEmpty()) return;
        this.fileName = getDefaultFileName();
        initDialog();
        downLoad();
    }

    @Override
    public void onClick(View v) {
        switch ((String) v.getTag()) {
            case TAG_REDOWNLOAD:
                tv_Title.setText("准备下载");
                btn_Cancel.setText("取消下载");
                btn_Cancel.setTag(TAG_CEDOWNLOAD);
                downLoad();
                break;
            case TAG_CEDOWNLOAD:
                btn_Cancel.setTag(TAG_REDOWNLOAD);
                isDownLoadFile = false;
                sendMessage(FLAG_DOWNLOAD_STOP, null);
                break;
            case TAG_ETDOWNLOAD:
                isDownLoadFile = false;
                MainDialog.dismiss();
                break;
            case TAG_INSTALLAPK:
                installApk(mContext, fileName);
                break;
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        return super.equals(obj);
    }

    private void initDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fmp_download_dialog, null);

        tv_Title = view.findViewById(R.id.tv_download_title);
        tv_SubTitle = view.findViewById(R.id.tv_download_sub_title);

        pb_Loading = view.findViewById(R.id.pb_download_progressBar);
        btn_Cancel = view.findViewById(R.id.btn_download_cancel);
        btn_Exit = view.findViewById(R.id.btn_download_exit);

        btn_Cancel.setOnClickListener(this);
        btn_Exit.setOnClickListener(this);

        btn_Cancel.setTag(TAG_CEDOWNLOAD);
        btn_Exit.setTag(TAG_ETDOWNLOAD);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setCancelable(false);
        MainDialog = builder.show();
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public void setUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    private String getDefaultFileName() {
        File folder = mContext.getExternalFilesDir("Download");
        File file = new File(folder, "update.apk");
        if (folder != null && !folder.exists()) {
            folder.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        return file.getAbsolutePath();
    }

    /**
     * 从服务器下载文件
     */
    private void downLoad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(fileUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        //获取文件大小
                        sendMessage(FLAG_GET_FILE_SIZE, con.getContentLength());
                        InputStream is = con.getInputStream();//获取输入流
                        FileOutputStream fileOutputStream = null;//文件输出流
                        if (is != null) {
                            File file = new File(fileName);
                            if (file.exists()) {
                                file.delete();
                            }
                            try {
                                file.createNewFile();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            fileOutputStream = new FileOutputStream(file);//指定文件保存路径，代码看下一步
                            byte[] buf = new byte[1024];
                            int ch;
                            int count = 0;
                            while ((ch = is.read(buf)) != -1) {
                                fileOutputStream.write(buf, 0, ch);//将获取到的流写入文件中
                                count += ch;
                                sendMessage(FLAG_UPDATE_PROGRESS, count);
                                if (!isDownLoadFile) {
                                    break;
                                }
                            }
                            is.close();
                            if (isDownLoadFile) {
                                sendMessage(FLAG_DOWNLOAD_SUCCESS, null);
                            } else {
                                sendMessage(FLAG_DOWNLOAD_STOP, null);
                            }

                        } else {
                            sendMessage(FLAG_DOWNLOAD_ERROR, "");
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                    } else {
                        sendMessage(FLAG_DOWNLOAD_ERROR, "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessage(FLAG_DOWNLOAD_ERROR, e.toString());
                    Logger.e(this.getClass().getName(), e.toString());
                }
            }
        }).start();
    }


    //发送消息
    private void sendMessage(int what, Object object) {
        Message message = handler.obtainMessage();
        message.what = what;
        message.obj = object;
        handler.sendMessage(message);

    }

    /* 安装apk */
    private void installApk(Context context, String fileName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是Android N以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context, "net.fmp.helper.fileprovider", new File(fileName));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + fileName), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

}
