package com.fmp.helper.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileService {

    static public String getLastName(File list) {
        //取得文件后缀名
        String FileName = list.getName();
        return FileName.substring(FileName.lastIndexOf(".") + 1);
    }

    static public void CopyBind(String Path, String ToPath) {
        //String ToPath=com.fmp.core.HelperCore.getHelperDirectory().getAbsolutePath() + "/--Tralside--/iDSystem/";
        File file = new File(Path);
        //判断
        if (file.isFile()) {
            //是文件则直接复制文件
            copyFile(file.getAbsolutePath(), ToPath + File.separator + file.getName());
        } else if (file.isDirectory()) {
            //是文件夹就遍历再复制
            copyFolder(file.getAbsolutePath(), ToPath);
        }
    }


    static private void copyFile(String 地址, String 目标地址) {
        try {
            // 新建文件输入流并对它进行缓冲
            FileInputStream input = new FileInputStream(地址);
            BufferedInputStream inBuff = new BufferedInputStream(input);
            // 新建文件输出流并对它进行缓冲
            FileOutputStream output = new FileOutputStream(目标地址);
            BufferedOutputStream outBuff = new BufferedOutputStream(output);
            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
            //关闭流
            inBuff.close();
            outBuff.close();
            output.close();
            input.close();
        } catch (Exception e) {
        }
    }

    static private void copyFolder(String Path, String TPath) {
        //MainActivity.DEBUG(Path);
        File main = new File(TPath + "/" + new File(Path).getName());
        main.mkdirs();
        //遍历文件
        File[] files = new File(Path).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                //MainActivity.DEBUG(main.getAbsolutePath());
                copyFile(file.getAbsolutePath(), main.getAbsolutePath() + "/" + file.getName());
            } else if (file.isDirectory()) {
                //MainActivity.DEBUG(main.getAbsolutePath());
                copyFolder(file.getAbsolutePath(), main.getAbsolutePath());
            }
        }
    }

    //删除文件夹或文件的操作
    static public void DeleteBind(String Path) {
        File file = new File(Path);
        if (file.exists()) {
            if (file.isFile()) {
                //删除文件
                file.delete();
            } else if (file.isDirectory()) {
                //遍历文件夹
                File[] files = file.listFiles();
                for (File list : files) {
                    if (list.isFile()) {
                        //删除文件
                        list.delete();
                    } else if (list.isDirectory()) {
                        //递归调用
                        DeleteBind(list.getAbsolutePath());
                    }
                }
                //删除文件夹中的文件夹和文件时，删除该文件夹/文件
                file.delete();
            }
        }
    }
}

