package com.fmp;

import android.text.format.DateFormat;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.fmp.core.HelperNative;
import com.fmp.util.HashUtil;

import java.io.File;
import java.util.Date;


public class UploadHelper {

    //与个人的存储区域有关
    private static final String ENDPOINT = "helper-files.gaozaici.cn";
    //上传仓库名
    private static final String BUCKET_NAME = "fmp-helper";

    private static OSS getOSSClient() {
        OSSCredentialProvider credentialProvider =
                new OSSPlainTextAKSKCredentialProvider("",
                        "");
        return new OSSClient(HelperNative.getApplication(), ENDPOINT, credentialProvider);
    }

    /**
     * 上传方法
     *
     * @param objectKey 标识
     * @param path      需上传文件的路径
     * @return 外网访问的路径
     */
    private static String upload(String objectKey, String path) throws Exception {
        // 构造上传请求
        PutObjectRequest request =
                new PutObjectRequest(BUCKET_NAME,
                        objectKey, path);
        //得到client
        OSS client = getOSSClient();
        //上传获取结果
        PutObjectResult result = client.putObject(request);
        //获取可访问的url
        return client.presignPublicObjectURL(BUCKET_NAME, objectKey);
    }

    /**
     * 上传普通图片
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadImage(String path) throws Exception {
        String key = getObjectImageKey(path);
        return upload(key, path);
    }

    /**
     * 上传头像
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadPortrait(String path) throws Exception {
        String key = getObjectPortraitKey(path);
        return upload(key, path);
    }

    /**
     * 上传audio
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadAudio(String path) throws Exception {
        String key = getObjectAudioKey(path);
        return upload(key, path);
    }

    public static String uploadMod(String path) throws Exception {
        return upload(String.format("mod/%s/%s", getDateString(), new File(path).getName()), path);
    }

    public static String uploadIcon(String path) throws Exception {
        return upload(String.format("icon/%s/%s", getDateString(), HashUtil.getMD5String(new File(path))), path);
    }

    /**
     * 获取时间
     *
     * @return 时间戳 例如:201805
     */
    private static String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    /**
     * 返回key
     *
     * @param path 本地路径
     * @return key
     */
    //格式: image/201805/sfdsgfsdvsdfdsfs.jpg
    private static String getObjectImageKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("image/%s/%s.jpg", dateString, fileMd5);
    }

    //格式: portrait/201805/sfdsgfsdvsdfdsfs.jpg
    private static String getObjectPortraitKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("portrait/%s/%s.jpg", dateString, fileMd5);
    }

    //格式: audio/201805/sfdsgfsdvsdfdsfs.mp3
    private static String getObjectAudioKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format("audio/%s/%s.mp3", dateString, fileMd5);
    }

}
