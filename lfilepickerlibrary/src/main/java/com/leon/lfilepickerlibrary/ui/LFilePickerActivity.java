package com.leon.lfilepickerlibrary.ui;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.transition.Explode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.leon.lfilepickerlibrary.R;
import com.leon.lfilepickerlibrary.adapter.PathAdapter;
import com.leon.lfilepickerlibrary.filter.LFileFilter;
import com.leon.lfilepickerlibrary.model.ParamEntity;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.leon.lfilepickerlibrary.utils.FileUtils;
import com.leon.lfilepickerlibrary.utils.StringUtils;
import com.leon.lfilepickerlibrary.widget.EmptyRecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LFilePickerActivity extends AppCompatActivity {

    private final String TAG = "FilePickerLeon";
    private EmptyRecyclerView mRecylerView;
    private View mEmptyView;
    private TextView mTvPath;
    private TextView mTvBack;
    private Button mBtnAddBook;
    private Button mBtnOpenDir;
    private String mPath;
    private List<File> mListFiles;
    private ArrayList<String> mListNumbers = new ArrayList<String>();//存放选中条目的数据地址
    private PathAdapter mPathAdapter;
    private Toolbar mToolbar;
    private ParamEntity mParamEntity;
    private LFileFilter mFilter;
    private boolean mIsAllSelected = false;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            mParamEntity = (ParamEntity) getIntent().getExtras().getSerializable("param");
            setTheme(mParamEntity.getTheme());
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lfile_picker);
            initView();
            setSupportActionBar(mToolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            initToolbar();
            updateAddButton();
            if (!checkSDState()) {
                Toast.makeText(this, R.string.lfile_NotFoundPath, Toast.LENGTH_SHORT).show();
                return;
            }
            mPath = mParamEntity.getStartPath();
            if (StringUtils.isEmpty(mPath)) {
                //如果没有指定路径，则使用默认路径
                mPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
            mTvPath.setText(mPath);
            mFilter = new LFileFilter(mParamEntity.getFileTypes());
            mListFiles = FileUtils.getFileList(mPath, mFilter, mParamEntity.isGreater(), mParamEntity.getFileSize());
            mPathAdapter = new PathAdapter(mListFiles, this, mFilter, mParamEntity.isMutilyMode(), mParamEntity.isGreater(), mParamEntity.getFileSize());
            mRecylerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mPathAdapter.setmIconStyle(mParamEntity.getIconStyle());
            mRecylerView.setAdapter(mPathAdapter);
            mRecylerView.setmEmptyView(mEmptyView);
            initListener();
        } catch (NullPointerException e) {
            e.printStackTrace();
            finish();
        }
    }

    /**
     * 更新Toolbar展示
     */
    private void initToolbar() {
        if (mParamEntity.getTitle() != null) {
            mToolbar.setTitle(mParamEntity.getTitle());
        }
        if (mParamEntity.getTitleStyle() != 0) {
            mToolbar.setTitleTextAppearance(this, mParamEntity.getTitleStyle());
        }
        if (mParamEntity.getTitleColor() != null) {
            mToolbar.setTitleTextColor(Color.parseColor(mParamEntity.getTitleColor())); //设置标题颜色
        }
        if (mParamEntity.getBackgroundColor() != null) {
            mToolbar.setBackgroundColor(Color.parseColor(mParamEntity.getBackgroundColor()));
        }
        switch (mParamEntity.getBackIcon()) {
            case Constant.BACKICON_STYLEONE:
                mToolbar.setNavigationIcon(R.mipmap.lfile_back1);
                break;
            case Constant.BACKICON_STYLETWO:
                mToolbar.setNavigationIcon(R.mipmap.lfile_back2);
                break;
            case Constant.BACKICON_STYLETHREE:
                //默认风格
                break;
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void updateAddButton() {
        if (!mParamEntity.isMutilyMode()) {
            mBtnAddBook.setVisibility(View.GONE);
        }
        if (!mParamEntity.isChooseMode()) {
            mBtnAddBook.setVisibility(View.VISIBLE);
            mBtnAddBook.setText(getString(R.string.lfile_OK));
            //文件夹模式默认为单选模式
            mParamEntity.setMutilyMode(false);
        }
    }

    /**
     * 跳转到指定目录
     * @param path 路径
     * */
    private void openDir(String path){
        mPath = path;
        mListFiles = FileUtils.getFileList(mPath, mFilter, mParamEntity.isGreater(), mParamEntity.getFileSize());
        mPathAdapter.setmListData(mListFiles);
        mPathAdapter.updateAllSelelcted(false);
        mIsAllSelected = false;
        updateMenuTitle();
        mBtnAddBook.setText(getString(R.string.lfile_Selected));
        mRecylerView.scrollToPosition(0);
        setShowPath(mPath);
        //清除添加集合中数据
        mListNumbers.clear();
        if (mParamEntity.getAddText() != null) {
            mBtnAddBook.setText(mParamEntity.getAddText());
        } else {
            mBtnAddBook.setText(R.string.lfile_Selected);
        }
    }

    /**
     * 添加点击事件处理
     */
    private void initListener() {
        // 返回目录上一级
        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempPath = new File(mPath).getParent();
                if (tempPath == null) {
                    return;
                }
                if (!TextUtils.isEmpty(mParamEntity.getEndPath())) {
                    //判断是否到了不可返回的目录
                    if (mPath.equals(mParamEntity.getEndPath())) {
                        Toast.makeText(LFilePickerActivity.this, R.string.lfile_EndDir, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
               openDir(tempPath);
            }
        });
        mPathAdapter.setOnItemClickListener(new PathAdapter.OnItemClickListener() {
            @Override
            public void click(CheckBox checkBox, int position) {
                if (mParamEntity.isMutilyMode()) {
                    if (mListFiles.get(position).isDirectory()) {
                        //如果当前是目录，则进入继续查看目录
                        checkInDirectory(position);
                        mPathAdapter.updateAllSelelcted(false);
                        mIsAllSelected = false;
                        updateMenuTitle();
                        mBtnAddBook.setText(getString(R.string.lfile_Selected));
                    } else {
                        if (checkBox.isChecked()) {
                            //先判断是否达到最大数量，如果数量达到上限提示，否则继续添加
                            if (mParamEntity.getMaxNum() > 0 && mListNumbers.size() >= mParamEntity.getMaxNum()) {
                                checkBox.setChecked(false);
                                Toast.makeText(LFilePickerActivity.this, R.string.lfile_OutSize, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //如果已经选择则取消，否则添加进来
                            mListNumbers.add(mListFiles.get(position).getAbsolutePath());
                        } else {
                            mListNumbers.remove(mListFiles.get(position).getAbsolutePath());
                        }
                        if (mParamEntity.getAddText() != null) {
                            mBtnAddBook.setText(String.format("%s( %d )", mParamEntity.getAddText(), mListNumbers.size()));
                        } else {
                            mBtnAddBook.setText(String.format("%s( %d )", getString(R.string.lfile_Selected), mListNumbers.size()));
                        }
                    }
                } else {
                    //单选模式直接返回
                    if (mListFiles.get(position).isDirectory()) {
                        checkInDirectory(position);
                        return;
                    }
                    if (mParamEntity.isChooseMode()) {
                        //选择文件模式,需要添加文件路径，否则为文件夹模式，直接返回当前路径
                        mListNumbers.add(mListFiles.get(position).getAbsolutePath());
                        chooseDone();
                    } else {
                        Toast.makeText(LFilePickerActivity.this, R.string.lfile_ChooseTip, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        mBtnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mParamEntity.isChooseMode() && mListNumbers.size() < 1) {
                    String info = mParamEntity.getNotFoundFiles();
                    if (TextUtils.isEmpty(info)) {
                        Toast.makeText(LFilePickerActivity.this, R.string.lfile_NotFoundBooks, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LFilePickerActivity.this, info, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //返回
                    chooseDone();
                }
            }
        });

        mBtnOpenDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View layout = getLayoutInflater().inflate(R.layout.lfile_open_file_menu, null, false);
                final PopupWindow pox = new PopupWindow(LFilePickerActivity.this);
                pox.setFocusable(true);
                pox.setContentView(layout);
                pox.setWidth(getWindowManager().getDefaultDisplay().getWidth() / 2 + getWindowManager().getDefaultDisplay().getWidth() / 4);
                pox.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);//getWindowManager().getDefaultDisplay().getHeight() / 4);
                pox.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                pox.setElevation(10);
                Explode Explode = new Explode();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pox.setEnterTransition(Explode);
                    pox.setExitTransition(Explode);
                }
                pox.getBackground().setAlpha(0);
                pox.showAtLocation(getWindow().getDecorView(), Gravity.CENTER | Gravity.BOTTOM, 0, getWindowManager().getDefaultDisplay().getHeight() / 11);
                //旧QQ下载目录
                layout.findViewById(R.id.lfile_opendir_oldqq).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View p1) {
                        File folder = new File(Environment.getExternalStorageDirectory(), "tencent/QQfile_recv");
                        if (folder.exists()) {
                            openDir(folder.getAbsolutePath());
                            pox.dismiss();
                        } else {
                            pox.dismiss();
                            Toast.makeText(LFilePickerActivity.this, "旧QQ下载目录不存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //新QQ下载目录
                layout.findViewById(R.id.lfile_opendir_newqq).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View p1) {
                        File folder = new File(Environment.getExternalStorageDirectory(), "Android/data/com.tencent.mobileqq/tencent/QQfile_recv");
                        if (folder.exists()) {
                            openDir(folder.getAbsolutePath());
                            pox.dismiss();
                        } else {
                            pox.dismiss();
                            Toast.makeText(LFilePickerActivity.this, "新QQ下载目录不存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                layout.findViewById(R.id.lfile_opendir_tim).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View p1) {
                        File folder = new File(Environment.getExternalStorageDirectory(), "tencent/TIMfile_recv");
                        if (folder.exists()) {
                            openDir(folder.getAbsolutePath());
                            pox.dismiss();
                        } else {
                            pox.dismiss();
                            Toast.makeText(LFilePickerActivity.this, "TIM下载目录不存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                layout.findViewById(R.id.lfile_opendir_download).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View p1) {
                        File folder = new File(Environment.getExternalStorageDirectory(), "Download");
                        if (folder.exists()) {
                            openDir(folder.getAbsolutePath());
                            pox.dismiss();
                        } else {
                            pox.dismiss();
                            Toast.makeText(LFilePickerActivity.this, "系统下载目录不存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    /**
     * 点击进入目录
     *
     * @param position 位置
     */
    private void checkInDirectory(int position) {
        mPath = mListFiles.get(position).getAbsolutePath();
        setShowPath(mPath);
        //更新数据源
        mListFiles = FileUtils.getFileList(mPath, mFilter, mParamEntity.isGreater(), mParamEntity.getFileSize());
        mPathAdapter.setmListData(mListFiles);
        mPathAdapter.notifyDataSetChanged();
        mRecylerView.scrollToPosition(0);
    }

    /**
     * 完成提交
     */
    private void chooseDone() {
        //判断是否数量符合要求
        if (mParamEntity.isChooseMode()) {
            if (mParamEntity.getMaxNum() > 0 && mListNumbers.size() > mParamEntity.getMaxNum()) {
                Toast.makeText(LFilePickerActivity.this, R.string.lfile_OutSize, Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Intent intent = new Intent();
        if (mListNumbers.size()>0){
            intent.putStringArrayListExtra(Constant.RESULT_INFO, mListNumbers);
        }else {
            mListNumbers.add(mPath);
            intent.putStringArrayListExtra(Constant.RESULT_INFO, mListNumbers);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mRecylerView = findViewById(R.id.recylerview);
        mTvPath = findViewById(R.id.tv_path);
        mTvBack = findViewById(R.id.tv_back);
        mBtnAddBook = findViewById(R.id.btn_addbook);
        mBtnOpenDir = findViewById(R.id.btn_opendir);
        mEmptyView = findViewById(R.id.empty_view);
        mToolbar = findViewById(R.id.toolbar);
        if (mParamEntity.getAddText() != null) {
            mBtnAddBook.setText(mParamEntity.getAddText());
        }
    }

    /**
     * 检测SD卡是否可用
     */
    private boolean checkSDState() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 显示顶部地址
     *
     * @param path
     */
    private void setShowPath(String path) {
        mTvPath.setText(path);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);
        this.mMenu = menu;
        updateOptionsMenu(menu);
        return true;
    }

    /**
     * 更新选项菜单展示，如果是单选模式，不显示全选操作
     *
     * @param menu
     */
    private void updateOptionsMenu(Menu menu) {
        mMenu.findItem(R.id.action_selecteall_cancel).setVisible(mParamEntity.isMutilyMode());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_selecteall_cancel) {
            //将当前目录下所有文件选中或者取消
            mPathAdapter.updateAllSelelcted(!mIsAllSelected);
            mIsAllSelected = !mIsAllSelected;
            if (mIsAllSelected) {
                for (File mListFile : mListFiles) {
                    //不包含再添加，避免重复添加
                    if (!mListFile.isDirectory() && !mListNumbers.contains(mListFile.getAbsolutePath())) {
                        mListNumbers.add(mListFile.getAbsolutePath());
                    }
                    if (mParamEntity.getAddText() != null) {
                        mBtnAddBook.setText(String.format("%s( %d )", mParamEntity.getAddText(), mListNumbers.size()));
                    } else {
                        mBtnAddBook.setText(String.format("%s( %d )", getString(R.string.lfile_Selected), mListNumbers.size()));
                    }
                }
            } else {
                mListNumbers.clear();
                mBtnAddBook.setText(getString(R.string.lfile_Selected));
            }
            updateMenuTitle();
        }
        return true;
    }

    /**
     * 更新选项菜单文字
     */
    public void updateMenuTitle() {

        if (mIsAllSelected) {
            mMenu.getItem(0).setTitle(getString(R.string.lfile_Cancel));
        } else {
            mMenu.getItem(0).setTitle(getString(R.string.lfile_SelectAll));
        }
    }

}
