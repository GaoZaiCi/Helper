package com.fmp.helper.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fmp.core.HelperCore;
import com.fmp.helper.activity.SkinsActivity;
import com.fmp.skins.SkinItem;
import com.fmp.skins.SkinPackDialog;
import com.fmp.skins.SkinRenderer;
import com.fmp.skins.SkinUtil;
import com.fmp.util.FileSizeUtil;
import com.jian.explosion.animation.ExplosionField;

import net.fmp.helper.R;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SkinRecycleViewAdapter extends RecyclerView.Adapter<SkinRecycleViewAdapter.MyViewHolder> {
    private SkinsActivity activity;
    private List<SkinItem> list;

    public SkinRecycleViewAdapter(SkinsActivity activity, List<SkinItem> list) {
        this.activity = activity;
        this.list = list;
    }

    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_layout_skin_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull MyViewHolder holder, int position) {
        SkinItem item = list.get(position);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinPackDialog.getInstance().showEditDialog(activity, item);
                SkinPackDialog.getInstance().setRemoveListener(new onSkinItemListener() {
                    @Override
                    public void onRemoveItem(SkinItem item) {
                        File file = new File(item.getPath());
                        boolean bool = file.delete();
                        if (bool) {
                            list.remove(position);
                            new ExplosionField(activity).explode(holder.cardView,
                                    new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                        }
                                    });
                            activity.loadSkinPack();
                            notifyDataSetChanged();
                            Toasty.success(activity, "删除成功", Toast.LENGTH_SHORT, true).show();
                        } else {
                            Toasty.error(activity, "删除失败", Toast.LENGTH_SHORT, true).show();
                        }
                    }

                    @Override
                    public void onUpdateItem(SkinItem newItem) {
                        list.set(position, newItem);
                        notifyDataSetChanged();
                    }
                });
            }
        });
        if (item.getSteve() != null) {
            holder.skinSteve.setImageBitmap(SkinRenderer.getSkinPreview(item.getSteve(), SkinRenderer.Side.FRONT, 19, SkinRenderer.Model.STEVE));
            holder.skinSteve.setVisibility(View.VISIBLE);
        } else {
            holder.skinSteve.setImageBitmap(null);
            holder.skinSteve.setVisibility(View.INVISIBLE);
        }
        if (item.getAlex() != null) {
            holder.skinAlex.setImageBitmap(SkinRenderer.getSkinPreview(item.getAlex(), SkinRenderer.Side.FRONT, 19, SkinRenderer.Model.ALEX));
            holder.skinAlex.setVisibility(View.VISIBLE);
        } else {
            holder.skinAlex.setImageBitmap(null);
            holder.skinAlex.setVisibility(View.INVISIBLE);
        }

        holder.skinName.setText(item.getName().replaceAll(".mcskin", HelperCore.EMPTY_STRING));
        holder.skinSize.setText(FileSizeUtil.FormetFileSize(item.getSize()));
        if (SkinUtil.getCurSkinName().equals(item.getName())) {
            holder.skinUse.setText("恢复默认");
            holder.skinUse.setOnClickListener(v -> {
                SkinUtil.setCurSkinName(HelperCore.EMPTY_STRING);
                notifyDataSetChanged();
            });
        } else {
            holder.skinUse.setText("使用");
            holder.skinUse.setOnClickListener(v -> {
                SkinUtil.setCurSkinName(item.getName());
                Toasty.success(activity, "设置成功", Toast.LENGTH_SHORT, true).show();
                notifyDataSetChanged();
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface onSkinItemListener {
        void onRemoveItem(SkinItem item);

        void onUpdateItem(SkinItem item);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView skinSteve;
        private ImageView skinAlex;
        private TextView skinName;
        private TextView skinSize;
        private Button skinUse;

        MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.skin_cardview);
            skinSteve = itemView.findViewById(R.id.skin_image_steve);
            skinAlex = itemView.findViewById(R.id.skin_image_alex);
            skinName = itemView.findViewById(R.id.skin_name);
            skinSize = itemView.findViewById(R.id.skin_size);
            skinUse = itemView.findViewById(R.id.skin_set_use);
        }
    }
}
