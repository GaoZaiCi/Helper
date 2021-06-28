package com.fmp.skins;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.fmp.Logger;
import com.fmp.helper.adapter.SkinRecycleViewAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.lwkandroid.imagepicker.ImagePicker;
import com.lwkandroid.imagepicker.data.ImageBean;
import com.lwkandroid.imagepicker.data.ImagePickType;
import com.lwkandroid.imagepicker.utils.GlideImagePickerDisplayer;

import net.fmp.helper.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import per.goweii.anylayer.AnimatorHelper;
import per.goweii.anylayer.AnyLayer;
import per.goweii.anylayer.DialogLayer;
import per.goweii.anylayer.Layer;

import static android.app.Activity.RESULT_OK;

public class SkinPackDialog {
    private static final int SELECT_STEVE = 1000;
    private static final int SELECT_ALEX = 1001;
    private static final int SELECT_STEVE_CAPE = 1002;
    private static final int SELECT_ALEX_CAPE = 1003;

    private static final int SKIN_WIDTH = 64;
    private static final int SKIN_HEIGHT = 64;
    private static final int SKIN_CAPE_WIDTH = 64;
    private static final int SKIN_CAPE_HEIGHT = 32;

    private static SkinPackDialog skinPackDialog;
    private SkinRecycleViewAdapter.onSkinItemListener skinItemListener;
    private onImageCallBack imageCallBack;

    public static SkinPackDialog getInstance() {
        if (skinPackDialog == null)
            skinPackDialog = new SkinPackDialog();
        return skinPackDialog;
    }

    public void setRemoveListener(SkinRecycleViewAdapter.onSkinItemListener listener) {
        this.skinItemListener = listener;
    }

    public void showEditDialog(Activity activity, SkinItem item) {
        AnyLayer.dialog(activity)
                .contentView(R.layout.dialog_edit_skin)
                .backgroundDimDefault()
                .contentAnimator(new DialogLayer.AnimatorCreator() {
                    @Override
                    public Animator createInAnimator(View content) {
                        return AnimatorHelper.createTopAlphaInAnim(content);
                    }

                    @Override
                    public Animator createOutAnimator(View content) {
                        return AnimatorHelper.createTopAlphaOutAnim(content);
                    }
                })
                .onVisibleChangeListener(new Layer.OnVisibleChangeListener() {
                    @Override
                    public void onShow(Layer layer) {
                        ((TextView) layer.getView(R.id.tv_dialog_title)).setText(String.format("编辑皮肤包-%s", item.getName()));
                        ((TextView) layer.getView(R.id.skin_item_edit_steve_tip)).setText("Steve模型");
                        ((TextView) layer.getView(R.id.skin_item_edit_steve_image_add)).setText("添加皮肤+");
                        ((TextView) layer.getView(R.id.skin_item_edit_steve_cape_add)).setText("添加披风+");
                        ((Button) layer.getView(R.id.skin_item_edit_steve_data)).setText("编辑JSON");
                        ((TextView) layer.getView(R.id.skin_item_edit_alex_tip)).setText("Alex模型");
                        ((TextView) layer.getView(R.id.skin_item_edit_alex_image_add)).setText("添加皮肤+");
                        ((TextView) layer.getView(R.id.skin_item_edit_alex_cape_add)).setText("添加披风+");
                        ((Button) layer.getView(R.id.skin_item_edit_alex_data)).setText("编辑JSON");
                        ((TextView) layer.getView(R.id.tv_dialog_no)).setText("退出编辑");
                        ((TextView) layer.getView(R.id.tv_dialog_yes)).setText("保存数据");

                        if (item.getSteve() != null) {
                            ((ImageView) layer.getView(R.id.skin_item_edit_steve_image)).setImageBitmap(SkinRenderer.getSkinPreview(item.getSteve(), SkinRenderer.Side.FRONT, 19, SkinRenderer.Model.STEVE));
                            layer.getView(R.id.skin_item_edit_steve_image_add).setVisibility(View.GONE);
                        }
                        if (item.getCape() != null) {
                            ((ImageView) layer.getView(R.id.skin_item_edit_steve_cape)).setImageBitmap(item.getCape());
                            layer.getView(R.id.skin_item_edit_steve_cape_add).setVisibility(View.GONE);
                        }
                        if (item.getAlex() != null) {
                            ((ImageView) layer.getView(R.id.skin_item_edit_alex_image)).setImageBitmap(SkinRenderer.getSkinPreview(item.getAlex(), SkinRenderer.Side.FRONT, 19, SkinRenderer.Model.ALEX));
                            layer.getView(R.id.skin_item_edit_alex_image_add).setVisibility(View.GONE);
                        }
                        if (item.getCapeTwo() != null) {
                            ((ImageView) layer.getView(R.id.skin_item_edit_alex_cape)).setImageBitmap(item.getCapeTwo());
                            layer.getView(R.id.skin_item_edit_alex_cape_add).setVisibility(View.GONE);
                        }

                        View.OnLongClickListener onLongClickListener = v -> {
                            ((ImageView) v).setImageBitmap(null);
                            switch (v.getId()) {
                                case R.id.skin_item_edit_steve_image:
                                    item.setSteve(null);
                                    layer.getView(R.id.skin_item_edit_steve_image_add).setVisibility(View.VISIBLE);
                                    break;
                                case R.id.skin_item_edit_steve_cape:
                                    item.setCape(null);
                                    layer.getView(R.id.skin_item_edit_steve_cape_add).setVisibility(View.VISIBLE);
                                    break;
                                case R.id.skin_item_edit_alex_image:
                                    item.setAlex(null);
                                    layer.getView(R.id.skin_item_edit_alex_image_add).setVisibility(View.VISIBLE);
                                    break;
                                case R.id.skin_item_edit_alex_cape:
                                    item.setCapeTwo(null);
                                    layer.getView(R.id.skin_item_edit_alex_cape_add).setVisibility(View.VISIBLE);
                                    break;
                            }
                            return true;
                        };
                        layer.getView(R.id.skin_item_edit_steve_image).setOnLongClickListener(onLongClickListener);
                        layer.getView(R.id.skin_item_edit_steve_cape).setOnLongClickListener(onLongClickListener);
                        layer.getView(R.id.skin_item_edit_alex_image).setOnLongClickListener(onLongClickListener);
                        layer.getView(R.id.skin_item_edit_alex_cape).setOnLongClickListener(onLongClickListener);
                    }

                    @Override
                    public void onDismiss(Layer layer) {
                    }
                })
                .onClick((layer, v) -> removeItem(activity, item, layer), R.id.skin_item_edit_remove)
                .onClick((layer, v) -> showHelpDialog(activity), R.id.skin_item_edit_help)
                .onClick((layer, v) -> showEditJsonDialog(activity, item, SELECT_STEVE), R.id.skin_item_edit_steve_data)
                .onClick((layer, v) -> showEditJsonDialog(activity, item, SELECT_ALEX), R.id.skin_item_edit_alex_data)
                .onClick((layer, v) -> addItemImage(activity, item, SELECT_STEVE, (ImageView) v, layer.getView(R.id.skin_item_edit_steve_image_add)), R.id.skin_item_edit_steve_image)
                .onClick((layer, v) -> addItemImage(activity, item, SELECT_STEVE_CAPE, (ImageView) v, layer.getView(R.id.skin_item_edit_steve_cape_add)), R.id.skin_item_edit_steve_cape)
                .onClick((layer, v) -> addItemImage(activity, item, SELECT_ALEX, (ImageView) v, layer.getView(R.id.skin_item_edit_alex_image_add)), R.id.skin_item_edit_alex_image)
                .onClick((layer, v) -> addItemImage(activity, item, SELECT_ALEX_CAPE, (ImageView) v, layer.getView(R.id.skin_item_edit_alex_cape_add)), R.id.skin_item_edit_alex_cape)
                .onClick((layer, v) -> {
                    try {
                        SkinUtil.saveSkinPack(item.getPath(), item);
                        skinItemListener.onUpdateItem(item);
                        toast(activity, "保存成功");
                        layer.dismiss();
                    } catch (IOException e) {
                        toast(activity, "保存失败");
                    }
                }, R.id.fl_dialog_yes)
                .onClickToDismiss(R.id.fl_dialog_no)
                .show();
    }

    private void showHelpDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.skin_course);
        builder.setView(imageView);
        AlertDialog dialog = builder.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imageView.setOnClickListener(v -> dialog.dismiss());
    }

    private String formatName(String str) {
        char[] chars = str.toCharArray();
        for (int i = 0; chars.length > i; i++) {
            if (chars[i] >= 'A' && chars[i] <= 'Z') {
                chars[i] += 32;
            }
        }
        return new String(chars);
    }

    private Skins.SkinsBean getSkinBean(Skins skins, int type) {
        for (Skins.SkinsBean skinsBean : skins.getSkins()) {
            if (type == SELECT_STEVE && formatName(skinsBean.getLocalization_name()).equals("steve")) {
                return skinsBean;
            }
            if (type == SELECT_ALEX && formatName(skinsBean.getLocalization_name()).equals("alex")) {
                return skinsBean;
            }
        }
        return null;
    }

    private void showEditJsonDialog(Context context, SkinItem item, int type) {
        Skins skins = SkinJsonTools.getSkinsClass(item.getSkins());
        if (TextUtils.isEmpty(item.getManifest())) {
            item.setManifest(SkinUtil.getManifestJson(context));
        }
        Manifest manifest = SkinJsonTools.getManifestClass(item.getManifest());
        AnyLayer.dialog(context)
                .contentView(R.layout.dialog_edit_skin_json)
                .backgroundDimDefault()
                .contentAnimator(new DialogLayer.AnimatorCreator() {
                    @Override
                    public Animator createInAnimator(View content) {
                        return AnimatorHelper.createTopAlphaInAnim(content);
                    }

                    @Override
                    public Animator createOutAnimator(View content) {
                        return AnimatorHelper.createTopAlphaOutAnim(content);
                    }
                })
                .onVisibleChangeListener(new Layer.OnVisibleChangeListener() {
                    @Override
                    public void onShow(Layer layer) {
                        try {
                            Skins.SkinsBean bean = getSkinBean(skins, type);
                            ((TextView) layer.getView(R.id.tv_dialog_title)).setText(String.format("编辑JSON-%s", bean.getLocalization_name()));
                            ((TextInputEditText) layer.getView(R.id.skin_item_edit_json_version)).setText(String.valueOf(manifest.getFormat_version()));
                            ((TextInputEditText) layer.getView(R.id.skin_item_edit_json_name)).setText(bean.getLocalization_name());
                            ((TextInputEditText) layer.getView(R.id.skin_item_edit_json_geometry)).setText(bean.getGeometry());
                            ((TextView) layer.getView(R.id.tv_dialog_no)).setText("退出编辑");
                            ((TextView) layer.getView(R.id.tv_dialog_yes)).setText("保存数据");
                        } catch (NullPointerException e) {
                            toast(context, "当前JSON无法编辑");
                            layer.dismiss();
                        }
                    }

                    @Override
                    public void onDismiss(Layer layer) {
                    }
                })
                .onClick((layer, v) -> {
                    try {
                        Skins.SkinsBean bean = getSkinBean(skins, type);
                        manifest.setFormat_version(Integer.parseInt(((TextInputEditText) layer.getView(R.id.skin_item_edit_json_version)).getText().toString()));
                        bean.setLocalization_name(((TextInputEditText) layer.getView(R.id.skin_item_edit_json_name)).getText().toString());
                        bean.setGeometry(((TextInputEditText) layer.getView(R.id.skin_item_edit_json_geometry)).getText().toString());
                        item.setManifest(SkinJsonTools.getManifestJson(manifest));
                        item.setSkins(SkinJsonTools.getSkinsJson(skins));
                        toast(context, "保存成功");
                        layer.dismiss();
                    } catch (NullPointerException e) {
                        toast(context, "当前数据无法保存");
                    }
                }, R.id.fl_dialog_yes)
                .onClickToDismiss(R.id.fl_dialog_no)
                .show();
    }

    private void removeItem(Context context, SkinItem item, Layer layer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage("您确定要删除这个皮肤包?");
        builder.setNegativeButton("确定", (dialog, which) -> {
            if (skinItemListener != null) {
                layer.dismiss();
                skinItemListener.onRemoveItem(item);
            }
        });
        builder.setPositiveButton("取消", null);
        builder.show();
    }

    private void addItemImage(Activity activity, SkinItem item, int requestCode, ImageView imageView, View addTip) {
        if (TextUtils.isEmpty(item.getSkins())) {
            item.setSkins(SkinUtil.getSkinsJson(activity));
        }
        Skins skins = SkinJsonTools.getSkinsClass(item.getSkins());
        for (Skins.SkinsBean bean : skins.getSkins()) {
            if (requestCode == SELECT_STEVE_CAPE && !bean.getGeometry().equals("geometry.humanoid.custom")) {
                toast(activity, "Steve正在使用自定义模型，披风可能显示不正常");
                break;
            }
            if (requestCode == SELECT_ALEX_CAPE && !bean.getGeometry().equals("geometry.humanoid.customSlim")) {
                toast(activity, "Alex正在使用自定义模型，披风可能显示不正常");
                break;
            }
        }
        this.imageCallBack = bitmap -> {
            try {
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();
                if ((height != SKIN_HEIGHT && height != SKIN_CAPE_HEIGHT) || (width != SKIN_WIDTH && width != SKIN_CAPE_WIDTH)) {
                    throw new Exception();
                }
            } catch (OutOfMemoryError e) {
                Logger.e("Bitmap", e.getMessage());
                toast(activity, "您选择的图片太大了");
                return;
            } catch (Exception e) {
                toast(activity, "您选择的图片不适用于我的世界");
                return;
            }
            try {
                switch (requestCode) {
                    case SELECT_STEVE: {
                        Skins.SkinsBean bean = getSkinBean(skins, SELECT_STEVE);
                        bean.setTexture("skin_steve.png");
                        item.setSteve(bitmap);
                        break;
                    }
                    case SELECT_STEVE_CAPE: {
                        Skins.SkinsBean bean = getSkinBean(skins, SELECT_STEVE);
                        bean.setCape("cape.png");
                        item.setCape(bitmap);
                        break;
                    }
                    case SELECT_ALEX: {
                        Skins.SkinsBean bean = getSkinBean(skins, SELECT_ALEX);
                        bean.setTexture("skin_alex.png");
                        item.setAlex(bitmap);
                        break;
                    }
                    case SELECT_ALEX_CAPE: {
                        Skins.SkinsBean bean = getSkinBean(skins, SELECT_ALEX);
                        bean.setCape("capeTwo.png");
                        item.setCapeTwo(bitmap);
                        break;
                    }
                }
                item.setSkins(SkinJsonTools.getSkinsJson(skins));
                addTip.setVisibility(View.GONE);
                imageView.setImageBitmap(SkinRenderer.getSkinPreview(bitmap, SkinRenderer.Side.FRONT, 19, requestCode == SELECT_STEVE ? SkinRenderer.Model.STEVE : SkinRenderer.Model.ALEX));
            } catch (NullPointerException e) {
                toast(activity, "添加失败");
            }
        };
        //发起图片选择
        new ImagePicker()
                .pickType(ImagePickType.SINGLE) //设置选取类型(拍照ONLY_CAMERA、单选SINGLE、多选MUTIL)
                .maxNum(1) //设置最大选择数量(此选项只对多选生效，拍照和单选都是1，修改后也无效)
                .needCamera(false) //是否需要在界面中显示相机入口(类似微信那样)
                .cachePath(activity.getCacheDir().getAbsolutePath()) //自定义缓存路径(拍照和裁剪都需要用到缓存)
                .displayer(new GlideImagePickerDisplayer()) //自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                .start(activity, requestCode); //自定义RequestCode
    }

    private void toast(Context context, String str) {
        Toasty.normal(context, str, R.drawable.ic_photo_white_24dp).show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == SELECT_STEVE || requestCode == SELECT_ALEX || requestCode == SELECT_STEVE_CAPE || requestCode == SELECT_ALEX_CAPE) && resultCode == RESULT_OK && null != data && imageCallBack != null) {
            //获取选择的图片数据
            List<ImageBean> resultList = data.getParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
            if (resultList != null)
                for (ImageBean bean : resultList) {
                    File file = new File(bean.getImagePath());
                    if (file.exists()) {
                        imageCallBack.onImage(BitmapFactory.decodeFile(bean.getImagePath()));
                    }
                }
        }
    }

    private interface onImageCallBack {
        void onImage(Bitmap bitmap);
    }
}
