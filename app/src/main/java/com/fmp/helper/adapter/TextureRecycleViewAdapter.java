package com.fmp.helper.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fmp.helper.activity.TexturesActivity;
import com.fmp.textures.TextureManager;
import com.fmp.util.FileSizeUtil;
import com.jian.explosion.animation.ExplosionField;

import net.fmp.helper.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class TextureRecycleViewAdapter extends RecyclerView.Adapter<TextureRecycleViewAdapter.MyViewHolder> {
    private TexturesActivity activity;
    private List<TextureManager.Item> list;

    public TextureRecycleViewAdapter(TexturesActivity activity, List<TextureManager.Item> list) {
        this.activity = activity;
        this.list = list;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_layout_texture_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        TextureManager.Item item = list.get(position);
        holder.TexturesName.setText(item.getName());
        holder.TexturesExplain.setText(item.getExplain());
        holder.TexturesVersion.setText(item.getVersion());
        holder.TexturesSize.setText(FileSizeUtil.FormetFileSize(item.getSize()));
        holder.TexturesSwitch.setChecked(item.isEnable());

        holder.TexturesCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("提示");
                builder.setMessage("是否要移除这个材质/光影");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (TextureManager.getInstance().removeItem(item)) {
                            dialog.dismiss();
                            Toasty.success(activity, "移除成功", Toast.LENGTH_SHORT, true).show();
                            new ExplosionField(activity).explode(holder.TexturesCardView,
                                    new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                        }
                                    });
                            notifyDataSetChanged();
                        } else {
                            Toasty.warning(activity, "操作失败", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
                builder.setPositiveButton("取消", null);
                builder.show();
            }
        });
        holder.TexturesSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!TextureManager.getInstance().setEnable(item, isChecked)) {
                buttonView.setChecked(!isChecked);
                Toasty.warning(activity, "操作失败!", Toast.LENGTH_SHORT, true).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView TexturesCardView;
        private TextView TexturesName;
        private TextView TexturesExplain;
        private TextView TexturesVersion;
        private TextView TexturesSize;
        private Switch TexturesSwitch;

        MyViewHolder(View itemView) {
            super(itemView);
            TexturesCardView = itemView.findViewById(R.id.textures_item_cardview);
            TexturesName = itemView.findViewById(R.id.textures_item_name);
            TexturesExplain = itemView.findViewById(R.id.textures_item_explain);
            TexturesVersion = itemView.findViewById(R.id.textures_item_version);
            TexturesSize = itemView.findViewById(R.id.textures_item_size);
            TexturesSwitch = itemView.findViewById(R.id.textures_item_switch);
        }
    }
}
