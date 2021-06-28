package com.fmp.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

public class WebUtil {
    //第一种

    /**
     * 获取参数(ArrayList<NameValuePair> nameValuePairs,String url)后post给远程服务器
     * 将获得的返回结果(String)返回给调用者
     * 本函数适用于查询数量较少的时候
     * Chen.Zhidong
     * 2011-02-15
     */
    public static String PostUrl(ArrayList<NameValuePair> nameValuePairs, String url) {
        String result = "";
        String tmp = "";
        InputStream is = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            return "Fail to establish http connection!";
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            tmp = sb.toString();
        } catch (Exception e) {
            return "Fail to convert net stream!";
        }

        try {
            JSONArray jArray = new JSONArray(tmp);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);
                Iterator<?> keys = json_data.keys();
                while (keys.hasNext()) {
                    result += json_data.getString(keys.next().toString());
                }
            }
        } catch (JSONException e) {
            return "The URL you post is wrong!";
        }

        return result;
    }

//第二种   

    /**
     * 获取参数指定的网页代码，将其返回给调用者，由调用者对其解析
     * 返回String
     * Chen.Zhidong
     * 2011-02-15
     */
    public static String UrlCode(String url) {
        InputStream is = null;
        String result = "";

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            return "未能建立http连接！" + e.toString();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();

            result = sb.toString();
        } catch (Exception e) {
            return "未能转换网流！" + e;
        }

        return result;
    }

//第三种   

    /**
     * 获取指定地址的网页数据
     * 返回数据流
     * Chen.Zhidong
     * 2011-02-18
     */
    public static InputStream streampost(String remote_addr) {
        URL infoUrl = null;
        InputStream inStream = null;
        try {
            infoUrl = new URL(remote_addr);
            URLConnection connection = infoUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inStream;
    }

    /**
     * 获取网页HTML源代码
     *
     * @param path 网页路径
     */
    public static String getHtml(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200) {
            InputStream inStream = conn.getInputStream();
            byte[] data = read(inStream);
            String html = new String(data, StandardCharsets.UTF_8);
            return html;
        }
        return null;
    }

    /**
     * 读取流中的数据
     */
    public static byte[] read(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(b)) != -1) {
            outputStream.write(b);
        }
        inputStream.close();
        return outputStream.toByteArray();
    }

    public static String downLoad(String url) {
        String response = "";
        //第一步：创建HttpClient对象
        HttpClient httpClient = new DefaultHttpClient();
        //第二步：创建代表请求的对象,参数是访问的服务器地址
        HttpGet httpGet = new HttpGet(url);
        //第三步：执行请求，获取服务器发还的相应对象
        HttpResponse httpResponse = null;
        try {
            httpResponse = httpClient.execute(httpGet);
            //第四步：检查相应的状态是否正常：检查状态码的值是200表示正常
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                //第五步：从相应对象当中取出数据，放到entity当中
                HttpEntity entity = httpResponse.getEntity();
                response = EntityUtils.toString(entity, "utf-8");//将entity当中的数据转换为字符串
            } else {
                response = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            response = "";
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return response;
    }
}
