package com.fmp.helper.adapter;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fmp.core.GamePluginManager;
import com.fmp.core.HelperCore;
import com.fmp.core.http.bean.HelperAccount;
import com.fmp.core.http.bean.HelperPlugin;
import com.fmp.util.FileSizeUtil;
import com.fmp.view.CircleImageView;
import com.squareup.picasso.Picasso;

import net.fmp.helper.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import es.dmoral.toasty.Toasty;
import per.goweii.anylayer.AnimatorHelper;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.Layer;


public class ModMallRecycleViewAdapter extends RecyclerView.Adapter<ModMallRecycleViewAdapter.MyViewHolder> {
    private Context mContext;

    public ModMallRecycleViewAdapter(Context context) {
        mContext = context;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fmp_mod_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        HelperPlugin plugin = GamePluginManager.getInstance().getAllMods().get(position);
        //设置按钮的点击
        setOperation(holder.Operation, plugin);
        setModCount(holder.Count, plugin.count);
        //设置MOD大小
        setModSize(holder.Size, plugin.size);

        if (!TextUtils.isEmpty(plugin.icon))
            Picasso.get()
                    .load(plugin.icon)
                    .into(holder.Icon);
        //设置MOD名称
        holder.Name.setText(plugin.name);
        //设置MOD的类型
        setType(holder.Type, plugin.type);
        holder.Code.setText(plugin.code);
        holder.Info.setText(plugin.info);

        holder.Name.setOnClickListener(v -> toast(((TextView) v).getText().toString()));
        holder.Info.setOnClickListener(v -> toast(((TextView) v).getText().toString()));
    }

    @Override
    public int getItemCount() {
        return GamePluginManager.getInstance().getAllMods().size();
    }

    private void updateModCount(HelperPlugin curMod) {
        curMod.updateCount(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e==null){
                    curMod.count++;
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void setOperation(Button btn, HelperPlugin plugin) {

        List<Integer> userMods = new ArrayList<>();
        HelperAccount userData = HelperCore.getInstance().getUserData();
        if (userData != null) {
            for (int id:userData.gameMods){
                userMods.add(new Integer(id));
            }
        }

        File modDir = GamePluginManager.getInstance().getModFilesDir();
        File modFile = new File(modDir, plugin.name);

        if (modFile.exists() && (userMods.contains(plugin.id) || userMods.contains(-1))) {
            if (!TextUtils.isEmpty(plugin.size) && Long.parseLong(plugin.size) != modFile.length()) {
                btn.setText("下载更新");
            } else {
                btn.setText("已下载");
            }
        } else {
            if (plugin.type == 2) {
                btn.setText("锁定");
            } else if (plugin.type == 0 || userMods.contains(plugin.id) || userMods.contains(-1)) {
                btn.setText("下载");
            } else if (plugin.type == 1) {
                btn.setText("购买");
            } else {
                btn.setText("禁用");
            }
        }

        btn.setOnClickListener(v -> {
            if (modFile.exists() && (userMods.contains(plugin.id) || userMods.contains(-1))) {
                if (!TextUtils.isEmpty(plugin.size) && Long.parseLong(plugin.size) != modFile.length()) {
                    btn.setText("下载更新");
                    new newThread(plugin.url, plugin.name, new onDownLoadListener() {
                        @Override
                        public void onCallBack(int flag, String message) {
                            switch (flag) {
                                case newThread.FLAG_DOWNLOAD_SUC:
                                    updateModCount(plugin);
                                    toast("下载成功");
                                    btn.setText("已下载");
                                    break;
                                case newThread.FLAG_DOWNLOAD_ERR:
                                    toast("下载失败" + message);
                                    btn.setText("重新下载");
                                    break;
                            }
                        }

                        @Override
                        public void onLoad(String message) {
                            btn.setText(message);
                        }
                    });
                } else {
                    toast("您已经拥有这个插件啦");
                }
            } else {
                if (plugin.type == 2) {
                    toast("当前插件已锁定，不可下载");
                } else if (plugin.type == 0 || userMods.contains(plugin.id) || userMods.contains(-1)) {
                    new newThread(plugin.url, plugin.name, new onDownLoadListener() {
                        @Override
                        public void onCallBack(int flag, String message) {
                            switch (flag) {
                                case newThread.FLAG_DOWNLOAD_SUC:
                                    updateModCount(plugin);
                                    toast("下载成功");
                                    btn.setText("已下载");
                                    break;
                                case newThread.FLAG_DOWNLOAD_ERR:
                                    toast("下载失败" + message);
                                    btn.setText("重新下载");
                                    break;
                            }
                        }

                        @Override
                        public void onLoad(String message) {
                            btn.setText(message);
                        }
                    });
                } else if (plugin.type == 1) {
                    joinQQGroup(plugin.groupKey);
                } else {
                    toast("当前插件无法使用");
                }
            }
        });
    }

    private void toast(String msg) {
        Random mRandom = new Random();
        AnyLayer.toast()
                .duration(3000)
                .icon(R.drawable.ic_extension_white_24dp)
                .message(msg)
                .alpha(mRandom.nextFloat())
                .backgroundColorInt(Color.argb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)))
                .gravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL)
                .animator(new Layer.AnimatorCreator() {
                    @Override
                    public Animator createInAnimator(View target) {
                        return AnimatorHelper.createZoomAlphaInAnim(target);
                    }

                    @Override
                    public Animator createOutAnimator(View target) {
                        return AnimatorHelper.createZoomAlphaOutAnim(target);
                    }
                })
                .show();
    }


    private void joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            Toasty.normal(mContext, "跳转中...").show();
            mContext.startActivity(intent);
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            Toasty.warning(mContext, "跳转失败", Toast.LENGTH_SHORT, true).show();
        }
    }

    @SuppressLint("DefaultLocale")
    private void setModCount(TextView textView, long count) {
            double value=count;
            if (count == 0) {
                textView.setText("无热度");
            } else if (count >= 1000 && count <= 10000) {
                textView.setText(String.format("热度%.2fk", value / 1000));
            } else if (count >= 10000) {
                textView.setText(String.format("热度%.2fw", value / 10000));
            } else {
                textView.setText(String.format("热度%d", count));
            }
    }

    private void setModSize(TextView tv, String size) {
        if (size != null && !size.isEmpty()) {
            tv.setText(FileSizeUtil.FormetFileSize(Long.parseLong(size)));
        }
    }

    private void setType(TextView tv, Integer type) {
        if (type != null) {
            if (type == 0) {
                tv.setText("免费");
                tv.setTextColor(Color.GREEN);
            } else if (type == 1) {
                tv.setText("付费");
                tv.setTextColor(Color.RED);
            } else if (type == 2) {
                tv.setText("锁定");
                tv.setTextColor(Color.MAGENTA);
            } else {
                tv.setText("禁用");
                tv.setTextColor(Color.BLUE);
            }
            return;
        }
        tv.setText("未知");
    }

    public interface onDownLoadListener {
        void onCallBack(int flag, String message);

        void onLoad(String message);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private Button Operation;
        private CircleImageView Icon;
        private TextView Name;
        private TextView Count;
        private TextView Size;
        private TextView Type;
        private TextView Code;
        private TextView Info;

        MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.mod_cardview);
            Operation = itemView.findViewById(R.id.mod_operation);
            Icon = itemView.findViewById(R.id.mod_icon);
            Name = itemView.findViewById(R.id.mod_name);
            Count = itemView.findViewById(R.id.mod_count);
            Size = itemView.findViewById(R.id.mod_size);
            Type = itemView.findViewById(R.id.mod_type);
            Code = itemView.findViewById(R.id.mod_code);
            Info = itemView.findViewById(R.id.mod_info);
        }
    }

    public class newThread {
        public static final int FLAG_DOWNLOAD_ING = 0;
        public static final int FLAG_DOWNLOAD_SUC = 1;
        public static final int FLAG_DOWNLOAD_ERR = 2;
        private onDownLoadListener downLoadListener;
        @SuppressLint("HandlerLeak")
        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FLAG_DOWNLOAD_ING:
                        downLoadListener.onLoad(FileSizeUtil.FormetFileSize((Integer) msg.obj));
                        break;
                    case FLAG_DOWNLOAD_SUC:
                        downLoadListener.onCallBack(msg.what, null);
                        break;
                    case FLAG_DOWNLOAD_ERR:
                        downLoadListener.onCallBack(msg.what, (String) msg.obj);
                        break;
                }
            }
        };

        newThread(String fileUrl, String fileName, onDownLoadListener downLoadListener) {
            this.downLoadListener = downLoadListener;
            downLoadFile(fileUrl, fileName);
        }

        private void sendMessage(int what, Object object) {
            Message message = handler.obtainMessage();
            message.what = what;
            message.obj = object;
            handler.sendMessage(message);
        }

        private void downLoadFile(String fileUrl, String fileName) {
            new Thread(() -> {
                try {
                    File file = new File(GamePluginManager.getInstance().getModFilesDir(), fileName);
                    File file1 = file.getParentFile();
                    if (!file1.exists()) {
                        file1.mkdirs();
                    }
                    URL url = new URL(fileUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestProperty("Charset", "UTF-8");
                    con.setRequestMethod("GET");
                    if (con.getResponseCode() == 200) {
                        InputStream is = con.getInputStream();//获取输入流
                        FileOutputStream fileOutputStream = null;//文件输出流
                        if (is != null) {
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
                                sendMessage(FLAG_DOWNLOAD_ING, count);
                            }
                            is.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                        /*//判断是否为fmod
                        if (fileName.endsWith(".fmod")) {
                            //获取全部mod数据
                            ModsData data = ModUtil.getModData();
                            if (data != null) {
                                boolean isDataExist = false;
                                //数据不为空
                                List<ModItem> items = data.getItemData();
                                //遍历数据
                                for (ModItem item : items) {
                                    if (fileName.equals(item.getName())) {
                                        //有这个记录
                                        item.setName(fileName);
                                        item.setPath(new File(ModsManager.getInstance().getModFilesDir(), fileName).getAbsolutePath());
                                        item.setSize(file.length());
                                        item.setKey(key);
                                        item.setEnable(false);
                                        isDataExist = true;
                                        //结束循环
                                        break;
                                    }
                                }
                                ModUtil.setModData(data);
                                if (!isDataExist) {
                                    ModItem item = new ModItem();
                                    item.setName(fileName);
                                    item.setPath(new File(ModsManager.getInstance().getModFilesDir(), fileName).getAbsolutePath());
                                    item.setSize(file.length());
                                    item.setKey(key);
                                    item.setEnable(false);
                                    item.setPosition(0);
                                    //添加这个数据
                                    ModUtil.addModItem(item);
                                }
                            } else {
                                //创建新数据
                                ModItem item = new ModItem();
                                item.setName(fileName);
                                item.setPath(new File(ModsManager.getInstance().getModFilesDir(), fileName).getAbsolutePath());
                                item.setSize(file.length());
                                item.setKey(key);
                                item.setEnable(false);
                                item.setPosition(0);
                                //添加这个数据
                                ModUtil.addModItem(item);
                            }
                        }*/
                        sendMessage(FLAG_DOWNLOAD_SUC, null);
                    }
                    con.disconnect();
                } catch (Exception e) {
                    sendMessage(FLAG_DOWNLOAD_ERR, e.getMessage());
                }
            }).start();
        }
    }
}
