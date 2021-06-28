package com.fmp.helper;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

//ViewPager使用的Adapter
class MyPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;

    MyPagerAdapter(@NonNull FragmentManager fm, int behavior,List<Fragment> fragments) {
        super(fm, behavior);
        mFragments=fragments;
    }


    @NotNull
    @Override
    public Fragment getItem(int position) {
        //该方法在滑到已经缓存的页面时，并不被调用。缓存外已经创建过并被销毁的页面，还会再调用该方法，重新创建。
        return mFragments.get(position);
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        //除非碰到 FragmentManager 刚好从 SavedState 中恢复了对应的 Fragment 的情况外(从页面缓存中恢复)，该函数将会调用 getItem() 函数，生成新的 Fragment 对象。新的对象将被 FragmentTransaction.add()。
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
        //超出缓存的页面，将调用该方法从视图中移除
        super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        //Log.i(TAG, NAME + "--getCount++");
        return mFragments.size();
    }
}
