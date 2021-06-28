package com.fmp;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FMP_ZipKillerDialog extends AlertDialog implements View.OnClickListener {
    private Context mContext;
    //默认爆破等级
    private int Level = 30000;

    private static AlertDialog LoadingDialog;
    private TextView tv_ZipFile;
    private Button btn_Start;
    private ProgressBar pb_Loading;

    private AlertDialog FileSelectorDialog;
    private TextView curPathView;

    public FMP_ZipKillerDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout Layout = new LinearLayout(mContext);
        Layout.setOrientation(LinearLayout.VERTICAL);

        TextView tv_Tip = new TextView(mContext);
        tv_Tip.setText("压缩文件爆破");
        tv_Tip.setGravity(Gravity.CENTER);
        Layout.addView(tv_Tip);

        tv_ZipFile = new TextView(mContext);
        tv_ZipFile.setGravity(Gravity.CENTER);
        tv_ZipFile.setText("当前未选择文件");
        tv_ZipFile.setTextColor(Color.GREEN);
        Layout.addView(tv_ZipFile);

        Button btn_SelectorFile = new Button(mContext);
        btn_SelectorFile.setText("选择压缩文件");
        btn_SelectorFile.setTag("SelectorFile");
        btn_SelectorFile.setOnClickListener(this);
        Layout.addView(btn_SelectorFile);

        btn_Start = new Button(mContext);
        btn_Start.setGravity(Gravity.CENTER);
        btn_Start.setText("开始爆破");
        btn_Start.setTag("Start");
        btn_Start.setVisibility(View.GONE);
        btn_Start.setOnClickListener(this);
        Layout.addView(btn_Start);

        pb_Loading = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
        pb_Loading.setVisibility(View.GONE);
        Layout.addView(pb_Loading);


        setContentView(Layout);
    }


    private class start extends Thread {
        private String filePath;

        public start(String Path) {
            filePath = Path;
            AlertDialog.Builder Loading_AlertDialog = new AlertDialog.Builder(mContext);
            //LinearLayout布局
            LinearLayout MainLayout = new LinearLayout(mContext);
            MainLayout.setOrientation(LinearLayout.VERTICAL);
            MainLayout.setGravity(Gravity.CENTER);
            //加载条
            ProgressBar ProgressBar = new ProgressBar(mContext);
            MainLayout.addView(ProgressBar);
            //提示消息
            TextView TextView_Tip = new TextView(mContext);
            TextView_Tip.setGravity(Gravity.CENTER);
            TextView_Tip.setText("处理中...");
            TextView_Tip.setTextSize(20);
            MainLayout.addView(TextView_Tip);
            //设置布局
            Loading_AlertDialog.setView(MainLayout);
            //设置不可关闭窗口
            Loading_AlertDialog.setCancelable(false);
            LoadingDialog = Loading_AlertDialog.show();
            //获取窗口并设置背景透明
            Objects.requireNonNull(LoadingDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @Override
        public void run() {
            File zipFile = new File(filePath);
            if (!zipFile.exists()) return;
            String tempPath = zipFile.getParent() + File.separator + "temp";
            String tempOut = zipFile.getParent() + File.separator + "temp.zip";
            try {
                //FMP_Toast.Show_Toast(mContext, "正在操作中...请勿离开本界面");
                deleteFile(new File(tempPath));
                UnZipFolder(filePath, tempPath);
                ZipFolder(tempPath, tempOut);
                File file = new File(tempPath);
                deleteFile(file);
                zipFile.delete();
                File oldFile = new File(tempOut);
                File newFile = new File(filePath);
                oldFile.renameTo(newFile);
                //FMP_Toast.Show_Toast(mContext, "操作成功");
            } catch (Exception e) {
                Logger.LogInfo("ZipKiller", e);
                //FMP_Toast.Show_Toast(mContext, "操作失败");
                deleteFile(new File(tempPath));
            }
            LoadingDialog.dismiss();
            //pb_Loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch ((String) v.getTag()) {
            case "SelectorFile":
                break;
            case "Start":
                if (tv_ZipFile == null) {
                    FMP_Toast.Show_Toast(mContext, "内部错误");
                    return;
                }
                if (tv_ZipFile.getText().toString().isEmpty()) {
                    FMP_Toast.Show_Toast(mContext, "文件路径无效");
                    return;
                }
                File zipFile = new File(tv_ZipFile.getText().toString());
                if (!zipFile.exists()) {
                    FMP_Toast.Show_Toast(mContext, "文件不存在");
                    return;
                }
                FMP_Toast.Show_Toast(mContext, "开始爆破");
                pb_Loading.setVisibility(View.VISIBLE);
                new start(tv_ZipFile.getText().toString()).start();
                break;
            case "upOrder":
                break;
            case "downOrder":
                break;
            case "reset":

                break;
            case "exit":
                FileSelectorDialog.dismiss();
                break;
            default:
                break;
        }
    }


    /**
     * 解压zip到指定的路径
     *
     * @param zipFileString ZIP的名称
     * @param outPathString 要解压缩路径
     * @throws Exception
     */
    private void UnZipFolder(String zipFileString, String outPathString) throws Exception {
        File zipFile = new File(zipFileString);
        if (!zipFile.exists()) return;
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        while ((zipEntry = inZip.getNextEntry()) != null) {
            //获取文件名称
            String szName = zipEntry.getName();
            //如果名称包含非法字符-跳转到下一循环
            if (szName.contains("../")) continue;
            //是否为目录--名称不包含非法字符
            if (zipEntry.isDirectory()) {
                //获取部件的文件夹名
                szName = szName.substring(0, szName.length() - 1);
                //new 文件夹对象
                File folder = new File(outPathString + File.separator + szName);
                //创建目录
                folder.mkdirs();
            } else {
                //new 文件对象
                File file = new File(outPathString + File.separator + szName);
                //文件不存在
                if (!file.exists()) {
                    //获取父目录--创建目录
                    file.getParentFile().mkdirs();
                    //创建新文件
                    file.createNewFile();
                }
                // 获取文件的输出流
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                // 读取字节到缓冲区
                while ((len = inZip.read(buffer)) != -1) {
                    // 从缓冲区位置写入字节
                    out.write(buffer, 0, len);
                    //刷新缓冲区
                    out.flush();
                }
                //关闭输出流
                out.close();
            }
        }
        //关闭zip流
        inZip.close();
    }

    /**
     * 压缩文件和文件夹
     *
     * @param srcFileString 要压缩的文件或文件夹
     * @param zipFileString 解压完成的Zip路径
     * @throws Exception
     */
    private void ZipFolder(String srcFileString, String zipFileString) throws Exception {
        //创建ZIP
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
        //创建文件
        File file = new File(srcFileString);
        //压缩
        ZipFiles(file.getParent() + File.separator, file.getName(), file.getName(), outZip);
        //==添加爆破文件==
        StringBuffer sb = new StringBuffer();
        for (int i = 0; Level > i; i++) {
            sb.append("./");
        }
        File newFile = new File(file.getParent() + File.separator + "*");
        //创建临时文件
        newFile.createNewFile();
        ZipEntry zipEntry = new ZipEntry(sb.toString() + newFile.getName());
        FileInputStream inputStream = new FileInputStream(newFile);
        outZip.putNextEntry(zipEntry);
        int len;
        byte[] buffer = new byte[4096];
        while ((len = inputStream.read(buffer)) != -1) {
            outZip.write(buffer, 0, len);
        }
        outZip.closeEntry();
        //删除临时文件
        newFile.delete();
        //==添加完成==
        //完成和关闭
        outZip.finish();
        outZip.close();
    }

    /**
     * 压缩文件
     *
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @throws Exception
     */
    private void ZipFiles(String folderString, String fileString, String putFileString, ZipOutputStream zipOutputSteam) throws Exception {
        if (zipOutputSteam == null) return;
        File file = new File(folderString + fileString);
        if (file.isFile()) {
            //文件
            ZipEntry zipEntry = new ZipEntry(putFileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }
            zipOutputSteam.closeEntry();
        } else {
            //文件夹
            String[] fileList = file.list();
            if (!putFileString.equals("temp")) {
                //没有子文件和压缩
                if (fileList.length <= 0) {
                    ZipEntry zipEntry = new ZipEntry(putFileString + File.separator);
                    zipOutputSteam.putNextEntry(zipEntry);
                    zipOutputSteam.closeEntry();
                }
                //子文件和递归
                for (int i = 0; i < fileList.length; i++) {
                    ZipFiles(folderString, fileString + File.separator + fileList[i], putFileString + File.separator + fileList[i], zipOutputSteam);
                }
            } else {
                //子文件和递归
                for (int i = 0; i < fileList.length; i++) {
                    ZipFiles(folderString, fileString + File.separator + fileList[i], fileList[i], zipOutputSteam);
                }
            }
        }
    }

    private void deleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }


}
