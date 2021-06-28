package com.fmp.helper.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fmp.AppConfig;
import com.fmp.helper.item.ArchiveItem;
import com.fmp.helper.util.FileService;
import com.fmp.util.FileSizeUtil;
import com.jian.explosion.animation.ExplosionField;

import net.fmp.helper.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArchiveRecycleViewAdapter extends RecyclerView.Adapter<ArchiveRecycleViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<ArchiveItem> list;

    public ArchiveRecycleViewAdapter(Context context, List<ArchiveItem> list) {
        this.mContext = context;
        this.list = list;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_layout_archive_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.worldImage.setImageBitmap(list.get(position).getIcon());
        holder.worldName.setText(list.get(position).getName());
        holder.worldSize.setText(FileSizeUtil.FormetFileSize(list.get(position).getWorldSize()));
        holder.worldDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(holder.cardView, list.get(position), position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private void showDialog(View view, ArchiveItem item, int position) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.fmp_alert_dialog, null);
        TextView Title = layout.findViewById(R.id.tv_alert_title);
        TextView Message = layout.findViewById(R.id.tv_alert_message);
        Button Button1 = layout.findViewById(R.id.btn_alert_button1);
        Button Button2 = layout.findViewById(R.id.btn_alert_button2);
        Button Button3 = layout.findViewById(R.id.btn_alert_button3);

        Title.setText("世界信息");
        StringBuilder worldInfo = new StringBuilder();
        worldInfo.append("世界名称：");
        worldInfo.append(item.getName());
        worldInfo.append(AppConfig.Newline);
        worldInfo.append("世界路径：");
        worldInfo.append(item.getWorldPath());
        worldInfo.append(AppConfig.Newline);
        worldInfo.append("世界大小：");
        worldInfo.append(FileSizeUtil.FormetFileSize(item.getWorldSize()));
        worldInfo.append(AppConfig.Newline);
        worldInfo.append("行为包数量：");
        worldInfo.append(item.getBehaviorCount());
        worldInfo.append(AppConfig.Newline);
        worldInfo.append("行为包大小：");
        worldInfo.append(FileSizeUtil.FormetFileSize(item.getBehaviorSize()));
        worldInfo.append(AppConfig.Newline);
        worldInfo.append("资源包数量：");
        worldInfo.append(item.getResourceCount());
        worldInfo.append(AppConfig.Newline);
        worldInfo.append("资源包大小：");
        worldInfo.append(FileSizeUtil.FormetFileSize(item.getResourceSize()));
        Message.setText(worldInfo.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(layout);
        AlertDialog alertDialog = builder.show();

        Button1.setText("删除世界");
        Button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setTitle("警告");
                builder1.setMessage("这个世界将会消失很久很久！");
                builder1.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                        FileService.DeleteBind(item.getWorldPath());
                        list.remove(position);
                        new ExplosionField(mContext).explode(view,
                                new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);

                                    }
                                });
                        notifyDataSetChanged();

                    }
                });
                builder1.setPositiveButton("取消", null);
                builder1.show();
            }
        });
        Button2.setVisibility(View.GONE);
        Button3.setText("关闭");
        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView worldImage;
        private TextView worldName;
        private TextView worldSize;
        private Button worldDetails;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.archive_cardview);
            worldImage = itemView.findViewById(R.id.archive_world_image);
            worldName = itemView.findViewById(R.id.archive_world_name);
            worldSize = itemView.findViewById(R.id.archive_world_size);
            worldDetails = itemView.findViewById(R.id.archive_world_details);
        }
    }
}
