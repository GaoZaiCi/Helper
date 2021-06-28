package com.fmp.helper;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MyBehavior extends FloatingActionButton.Behavior {
    private static AccelerateDecelerateInterpolator LINEAR_INTERRPLATOR = new AccelerateDecelerateInterpolator();
    private boolean isAnimateIng = false;   // 是否正在动画
    private boolean isShow = true;  // 是否已经显示


    public MyBehavior(Context context, AttributeSet attrs) {
        super();
    }

    public void showFab(View view, AnimateListener... listener) {
        if (listener.length != 0) {
            view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(600)
                    .setInterpolator(LINEAR_INTERRPLATOR)
                    .setListener(listener[0])
                    .start();
        } else {
            view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(600)
                    .setInterpolator(LINEAR_INTERRPLATOR)
                    .start();
        }
        //view.setVisibility(View.VISIBLE);

    }

    public void hideFab(View view, AnimateListener listener) {
        view.animate()
                .scaleX(0f)
                .scaleY(0f)
                .alpha(0f)
                .setDuration(600)
                .setInterpolator(LINEAR_INTERRPLATOR)
                .setListener(listener)
                .start();
        //view.setVisibility(View.GONE);
    }

    //只有当返回值为true才会执行下面的方法,例如onNestedScroll
    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
                || axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    //滑动结束的位置
    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }


    //滑动时调用该方法
    //dxConsumed: 表示view消费了x方向的距离长度
    //dyConsumed: 表示view消费了y方向的距离长度
    //消费的距离是指实际滚动的距离
    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type, @NonNull int[] consumed) {
        if (dyConsumed > 0) { // 向下滑动
            animateOut(child);
        } else if (dyConsumed < 0) { // 向上滑动
            animateIn(child);
        }
        /*
        //界面向下滑动,fab动画结束,且正在显示
        //隐藏Fab
        if ((dyConsumed > 0 || dyUnconsumed > 0) && !isAnimateIng && isShow) {
            hideFab(child, new AnimateListener());
        }
        //界面向上滑动,fab动画结束,且隐藏
        //显示Fab
        else if ((dyConsumed < 0 || dyUnconsumed < 0) && !isAnimateIng && !isShow) {
            showFab(child, new AnimateListener());
        }
        */
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed);
    }

    // FAB移出屏幕动画（隐藏动画）
    private void animateOut(FloatingActionButton fab) {
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        int bottomMargin = layoutParams.bottomMargin;
        fab.animate().translationY(fab.getHeight() + bottomMargin + 200).setInterpolator(new LinearInterpolator()).start();
    }

    // FAB移入屏幕动画（显示动画）
    private void animateIn(FloatingActionButton fab) {
        fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
    }


    public class AnimateListener implements Animator.AnimatorListener {


        @Override
        public void onAnimationStart(Animator animation) {
            isAnimateIng = true;
            isShow = !isShow;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            isAnimateIng = false;

        }

        @Override
        public void onAnimationCancel(Animator animation) {


        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }


}
