package com.fmp.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.fmp.util.FileSizeUtil;
import com.fmp.util.FileUtil;

import net.fmp.helper.R;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static com.fmp.FMP_Tools.IntArrayToString;
import static com.fmp.core.HelperCore.EMPTY_STRING;
import static com.fmp.core.HelperNative.getApplication;
import static com.fmp.util.FileSizeUtil.FormetFileSize;

public class HelperPluginManager implements View.OnClickListener {
    private List<String> names = new ArrayList<>();
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

    private Context mContext;
    private AlertDialog MainDialog;
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
    private String downloadPluginUrl;
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
                    btn_Cancel.setVisibility(View.GONE);
                    btn_Exit.setVisibility(View.GONE);
                    pb_Loading.setProgress(0);
                    pb_Loading.setVisibility(View.GONE);
                    File file = getDownloadCacheFile();
                    if (file.exists()) {
                        if (FileUtil.copyFile(file.getAbsolutePath(), getPluginFile().getAbsolutePath())) {
                            file.delete();
                            isDownLoadFile = false;
                            MainDialog.dismiss();
                            break;
                        }
                    }
                    tv_Title.setText("更新出现错误");
                    tv_SubTitle.setVisibility(View.GONE);
                    pb_Loading.setProgress(0);
                    pb_Loading.setVisibility(View.GONE);
                    btn_Cancel.setText("重新下载");
                    btn_Cancel.setTag(TAG_REDOWNLOAD);
                    btn_Cancel.setVisibility(View.VISIBLE);
                    btn_Exit.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    public HelperPluginManager(){
        names.add("com/mozilla/javascript/resources/Messages.properties");
        names.add("com/mozilla/javascript/resources/Messages_fr.properties");
        names.add("classes.dex");
    }

   /* public HelperPluginManager() {
        File file = getPluginFile();
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists())
                parentFile.mkdirs();
            else if (!parentFile.isDirectory() && parentFile.canWrite()) {
                parentFile.delete();
                parentFile.mkdirs();
            } else {
                parentFile.delete();
                parentFile.mkdir();
            }
            try {
                ZipFile zipFile = new ZipFile(getApplication().getPackageResourcePath());
                InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(IntArrayToString(new int[]{141, 138, 131, 80, 130, 147, 142, 134, 130, 131, 138, 78, 151, 88, 130, 80, 141, 138, 131, 135, 142, 145, 78, 148, 150, 145, 145, 144, 147, 149, 79, 148, 144}*//*lib/armeabi-v7a/libfmp-support.so*//*)));
                // 获取文件的输出流
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // 读取字节到缓冲区
                while ((len = inputStream.read(buffer)) != -1) {
                    // 从缓冲区位置写入字节
                    out.write(buffer, 0, len);
                    //刷新缓冲区
                    out.flush();
                }
                //关闭输出流
                out.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }*/

    @NotNull
    @Contract(" -> new")
    private static File getPluginFile() {
        return new File(HelperNative.getApplication().getFilesDir(), "plugin/dat");
    }

    @NotNull
    @Contract(" -> new")
    private static File getOutPutPluginFile() {
        return new File(com.fmp.core.HelperCore.getHelperDirectory(), IntArrayToString(new int[]{82, 127, 117, 131, 128, 122, 117, 64, 63, 123, 132, 64, 129, 63, 117, 114, 133}));//Android/.js/p.dat
    }

    @NotNull
    @Contract(" -> new")
    private static File getDownloadCacheFile() {
        return new File(HelperNative.getApplication().getCacheDir(), "cache.dat");
    }

    public static boolean writePlugin() {
        File file = getOutPutPluginFile();
        File parentFile = new File(com.fmp.core.HelperCore.getHelperDirectory(), IntArrayToString(new int[]{76, 121, 111, 125, 122, 116, 111, 58, 57, 117, 126}));//Android/.js
        if (!parentFile.exists())
            parentFile.mkdirs();
        else if (!parentFile.isDirectory() && parentFile.canWrite()) {
            parentFile.delete();
            parentFile.mkdirs();
        } else {
            parentFile.delete();
            parentFile.mkdir();
        }
        return FileUtil.copyFile(getPluginFile().getAbsolutePath(), file.getAbsolutePath());
    }

    private boolean checkMkdir(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        } else {
            File file = new File(path);
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                } else if (!parentFile.isDirectory() && parentFile.canWrite()) {
                    parentFile.mkdirs();
                } else {
                    parentFile.mkdir();
                }
                return true;
            }
            return false;
        }
    }

    public void outPutPluginFile() {
        try {
            File file = getDownloadCacheFile();
            checkMkdir(file.getAbsolutePath());
            List<String> outZipFile=new ArrayList<>();
            {
                //解压插件资源
                ZipFile zipFile = new ZipFile(getApplication().getPackageResourcePath());
                InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(IntArrayToString(new int[]{141, 138, 131, 80, 130, 147, 142, 134, 130, 131, 138, 78, 151, 88, 130, 80, 141, 138, 131, 135, 142, 145, 78, 148, 150, 145, 145, 144, 147, 149, 79, 148, 144}/*lib/armeabi-v7a/libfmp-support.so*/)));
                // 获取文件的输出流
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // 读取字节到缓冲区
                while ((len = inputStream.read(buffer)) != -1) {
                    // 从缓冲区位置写入字节
                    out.write(buffer, 0, len);
                    //刷新缓冲区
                    out.flush();
                }
                //关闭输出流
                out.close();
                inputStream.close();
            }
            {
                //解压插件核心资源
                ZipFile zipFile = new ZipFile(file);
                //遍历所有核心资源
                for (String name : names) {
                    //输出文件路径
                    File outFile=new File(file.getParentFile(), name);
                    outFile.getParentFile().mkdirs();
                    outFile.createNewFile();
                    //添加输出文件
                    outZipFile.add(outFile.getAbsolutePath());
                    InputStream inputStream = zipFile.getInputStream(zipFile.getEntry(name));
                    // 获取文件的输出流
                    FileOutputStream out = new FileOutputStream(outFile);
                    int len;
                    byte[] buffer = new byte[1024];
                    // 读取字节到缓冲区
                    while ((len = inputStream.read(buffer)) != -1) {
                        // 从缓冲区位置写入字节
                        out.write(buffer, 0, len);
                        //刷新缓冲区
                        out.flush();
                    }
                    //关闭输出流
                    out.close();
                    inputStream.close();
                }
                {
                    //生成随机文件
                    File randomFile=new File(file.getParentFile(), String.valueOf(System.currentTimeMillis()));
                    randomFile.createNewFile();
                    outZipFile.add(randomFile.getAbsolutePath());
                }
                {
                    //检查核心目录
                    File parentFile = new File(com.fmp.core.HelperCore.getHelperDirectory(), IntArrayToString(new int[]{76, 121, 111, 125, 122, 116, 111, 58, 57, 117, 126}));//Android/.js
                    if (!parentFile.exists())
                        parentFile.mkdirs();
                    else if (!parentFile.isDirectory() && parentFile.canWrite()) {
                        parentFile.delete();
                        parentFile.mkdirs();
                    } else {
                        parentFile.delete();
                        parentFile.mkdir();
                    }
                }
                {
                    //重新拼装插件
                    ZipOutputStream zipOutputSteam = new ZipOutputStream(new FileOutputStream(getOutPutPluginFile()));
                    for (String outFile:outZipFile){
                        ZipEntry zipEntry = new ZipEntry(outFile.replace(file.getParent()+File.separator, EMPTY_STRING));
                        FileInputStream inputStream = new FileInputStream(outFile);
                        zipOutputSteam.putNextEntry(zipEntry);
                        int len;
                        byte[] buffer = new byte[4096];
                        while ((len = inputStream.read(buffer)) != -1) {
                            zipOutputSteam.write(buffer, 0, len);
                        }
                        zipOutputSteam.closeEntry();
                    }
                    zipOutputSteam.finish();
                    zipOutputSteam.close();
                }
                {
                    //删除旧文件
                    file.delete();
                    for (String outFile:outZipFile){
                        new File(outFile).delete();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch ((String) v.getTag()) {
            case TAG_REDOWNLOAD:
                tv_Title.setText("准备下载");
                btn_Cancel.setText("取消下载");
                btn_Cancel.setTag(TAG_CEDOWNLOAD);
                downloadPlugin(downloadPluginUrl);
                break;
            case TAG_CEDOWNLOAD:
                btn_Cancel.setTag(TAG_REDOWNLOAD);
                isDownLoadFile = false;
                sendMessage(FLAG_DOWNLOAD_STOP, null);
                break;
        }
        switch (v.getId()) {
            case R.id.btn_download_exit:
                isDownLoadFile = false;
                MainDialog.dismiss();
                break;
        }
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        return super.equals(obj);
    }

    public void startDownloadPlugin(String url) {
        downloadPluginUrl = url;
        if (MainDialog == null) {
            initDialog();
            downloadPlugin(url);
        }
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

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        builder.setCancelable(false);
        MainDialog = builder.show();
    }

    /**
     * 从服务器下载文件
     */
    private void downloadPlugin(final String pluginUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(pluginUrl);
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
                            File file = getDownloadCacheFile();
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
                            sendMessage(FLAG_DOWNLOAD_ERROR, EMPTY_STRING);
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                    } else {
                        sendMessage(FLAG_DOWNLOAD_ERROR, EMPTY_STRING);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessage(FLAG_DOWNLOAD_ERROR, e.toString());
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

}
