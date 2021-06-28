package com.fmp.helper.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fmp.core.HelperCore;
import com.fmp.core.LocalPluginManager;
import com.fmp.mods.PluginData;
import com.fmp.util.FileSizeUtil;
import com.jian.explosion.animation.ExplosionField;

import net.fmp.helper.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class ModFileRecycleViewAdapter extends RecyclerView.Adapter<ModFileRecycleViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<PluginData.Item> list;

    public ModFileRecycleViewAdapter(Context context, List<PluginData.Item> list) {
        this.mContext = context;
        this.list = list;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_mod_files_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.activity_mod_files_details, null, false);
                AlertDialog.Builder al = new AlertDialog.Builder(mContext);
                al.setView(view);
                final Dialog dialog = al.create();
                //dialog.setCancelable(false);
                //dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                TextView name = view.findViewById(R.id.mod_file_details_name);
                name.setText(list.get(position).getName());
                TextView size = view.findViewById(R.id.mod_file_details_size);
                size.setText(FileSizeUtil.FormetFileSize(list.get(position).getSize()));
                TextView path = view.findViewById(R.id.mod_file_details_path);
                path.setText(list.get(position).getPath().replaceAll(com.fmp.core.HelperCore.getHelperDirectory().getAbsolutePath() + File.separator, HelperCore.EMPTY_STRING));
                path.setOnClickListener(p1 -> {
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", list.get(position).getPath());
                    // 将ClipData内容放到系统剪贴板里。
                    if (cm != null) {
                        cm.setPrimaryClip(mClipData);
                        Toasty.normal(mContext, "复制路径成功").show();
                    }
                });
                //删除文件的操作
                view.findViewById(R.id.mod_file_details_delete).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View p1) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("警告");
                        builder.setMessage("删除文件将无法恢复，是否确认？");
                        builder.setPositiveButton("取消", null);
                        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface p1, int p2) {
                                if (LocalPluginManager.getInstance().removePlugin(list.get(position))) {
                                    dialog.dismiss();
                                    list.remove(position);
                                    new ExplosionField(mContext).explode(holder.cardView,
                                            new AnimatorListenerAdapter() {
                                                @Override
                                                public void onAnimationEnd(Animator animation) {
                                                    super.onAnimationEnd(animation);

                                                }
                                            });
                                    Toasty.success(mContext, "删除成功!", Toast.LENGTH_SHORT, true).show();
                                    notifyDataSetChanged();
                                } else {
                                    Toasty.warning(mContext, "操作失败!", Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        });
                        builder.show();
                    }
                });
                view.findViewById(R.id.mod_file_details_back).setOnClickListener(p1 -> dialog.dismiss());
            }
        });
        holder.modName.setText(list.get(position).getName());
        holder.modPath.setText(list.get(position).getPath().replaceAll(com.fmp.core.HelperCore.getHelperDirectory().getAbsolutePath() + File.separator, HelperCore.EMPTY_STRING));
        holder.modSize.setText(FileSizeUtil.FormetFileSize(list.get(position).getSize()));
        holder.modEnable.setChecked(list.get(position).isEnable());
        holder.modEnable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!LocalPluginManager.getInstance().setPluginEnable(list.get(position), isChecked)) {
                buttonView.setChecked(!isChecked);
                Toasty.warning(mContext, "操作失败!", Toast.LENGTH_SHORT, true).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView modName;
        private TextView modPath;
        private TextView modSize;
        private Switch modEnable;

        MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.mod_file_cardview);
            modName = itemView.findViewById(R.id.mod_file_name);
            modPath = itemView.findViewById(R.id.mod_file_path);
            modSize = itemView.findViewById(R.id.mod_file_size);
            modEnable = itemView.findViewById(R.id.mod_file_switch);
        }
    }
}
