package com.fmp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.SeekBar;



/**
 * @Description: 自定义普通进度条
 * @Param:
 * @Return:
 * @Author: zhangliangming
 * @Date: 2018-02-14
 * @Throws:
 */
public class SeekBarView extends SeekBar {
    /**
     * 背景画笔
     */
    private Paint mBackgroundPaint;

    /**
     * 进度画笔
     */
    private Paint mProgressPaint;

    /**
     * 第二进度画笔
     */
    private Paint mSecondProgressPaint;

    /**
     * 游标画笔
     */
    private Paint mThumbPaint;

    /**
     * 默认
     */
    private final int TRACKTOUCH_NONE = -1;
    /**
     * 开始拖动
     */
    private final int TRACKTOUCH_START = 0;
    private int mTrackTouch = TRACKTOUCH_NONE;

    private OnChangeListener mOnChangeListener;

    //TrackingTouch
    private boolean isTrackingTouch = false;
    private int mTrackingTouchSleepTime = 0;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            setTrackTouch(TRACKTOUCH_NONE);
        }
    };

    public SeekBarView(Context context) {
        super(context);
        init(context);
    }

    public SeekBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {

        setBackgroundColor(Color.TRANSPARENT);
        //
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setDither(true);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(Color.parseColor("#e5e5e5"));

        //
        mProgressPaint = new Paint();
        mProgressPaint.setDither(true);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(Color.parseColor("#0288d1"));

        //
        mSecondProgressPaint = new Paint();
        mSecondProgressPaint.setDither(true);
        mSecondProgressPaint.setAntiAlias(true);
        mSecondProgressPaint.setColor(Color.parseColor("#b8b8b8"));

        //
        mThumbPaint = new Paint();
        mThumbPaint.setDither(true);
        mThumbPaint.setAntiAlias(true);
        mThumbPaint.setColor(Color.parseColor("#0288d1"));

        //
        setThumb(new BitmapDrawable());
        setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mTrackTouch == TRACKTOUCH_START) {
                    if (mOnChangeListener != null) {
                        mOnChangeListener.onProgressChanged(SeekBarView.this);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = true;
                mHandler.removeCallbacks(mRunnable);
                if (mTrackTouch == TRACKTOUCH_NONE) {
                    setTrackTouch(TRACKTOUCH_START);
                    if (mOnChangeListener != null) {
                        mOnChangeListener.onTrackingTouchStart(SeekBarView.this);
                    }
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isTrackingTouch = false;
                if (mTrackTouch == TRACKTOUCH_START) {
                    if (mOnChangeListener != null) {
                        mOnChangeListener.onTrackingTouchFinish(SeekBarView.this);
                    }
                    mHandler.postDelayed(mRunnable, mTrackingTouchSleepTime);
                }
            }
        });
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        int rSize = getHeight() / 4;
        if (isTrackingTouch) {
            rSize = getHeight() / 3;
        }
        int height = getHeight() / 4 / 3;
        int leftPadding = rSize;

        if (getProgress() > 0) {
            leftPadding = 0;
        }

        RectF backgroundRect = new RectF(leftPadding, getHeight() / 2 - height, getWidth(),
                getHeight() / 2 + height);
        canvas.drawRoundRect(backgroundRect, rSize, rSize, mBackgroundPaint);


        if (getMax() != 0) {
            int secondRight = (int) ((float) getSecondaryProgress() / getMax() * getWidth());
            RectF secondProgressRect = new RectF(leftPadding, getHeight() / 2 - height,
                    secondRight, getHeight()
                    / 2 + height);
            canvas.drawRoundRect(secondProgressRect, rSize, rSize, mSecondProgressPaint);

            int progressRight = (int) ((float) getProgress() / getMax() * getWidth());
            RectF progressRect = new RectF(leftPadding, getHeight() / 2 - height,
                    progressRight, getHeight() / 2
                    + height);
            canvas.drawRoundRect(progressRect, rSize, rSize, mProgressPaint);


            int cx = (int) ((float) getProgress() / getMax() * getWidth());
            if ((cx + rSize) > getWidth()) {
                cx = getWidth() - rSize;
            } else {
                cx = Math.max(cx, rSize);
            }
            int cy = getHeight() / 2;
            canvas.drawCircle(cx, cy, rSize, mThumbPaint);
        }
    }

    @Override
    public synchronized void setProgress(int progress) {
        if (mTrackTouch == TRACKTOUCH_NONE && getMax() != 0) {
            super.setProgress(progress);
        }
        postInvalidate();
    }

    @Override
    public synchronized void setSecondaryProgress(int secondaryProgress) {
        super.setSecondaryProgress(secondaryProgress);
        postInvalidate();
    }

    @Override
    public synchronized void setMax(int max) {
        super.setMax(max);
        postInvalidate();
    }

    private synchronized void setTrackTouch(int trackTouch) {
        this.mTrackTouch = trackTouch;
    }

    /**
     * 设置背景颜色
     *
     * @param backgroundColor
     */
    public void setBackgroundPaintColor(int backgroundColor) {
        mBackgroundPaint.setColor(backgroundColor);
        postInvalidate();
    }

    /**
     * 设置进度颜色
     *
     * @param progressColor
     */
    public void setProgressColor(int progressColor) {
        mProgressPaint.setColor(progressColor);
        postInvalidate();
    }

    /**
     * 设置第二进度颜色
     *
     * @param secondProgressColor
     */
    public void setSecondProgressColor(int secondProgressColor) {
        mSecondProgressPaint.setColor(secondProgressColor);
        postInvalidate();
    }

    /**
     * 设置游标颜色
     *
     * @param thumbColor
     */
    public void setThumbColor(int thumbColor) {
        mThumbPaint.setColor(thumbColor);
        postInvalidate();
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.mOnChangeListener = onChangeListener;
    }

    public void setTrackingTouchSleepTime(int mTrackingTouchSleepTime) {
        this.mTrackingTouchSleepTime = mTrackingTouchSleepTime;
    }

    public interface OnChangeListener {
        /**
         * 进度改变
         *
         * @param seekBar
         */
        void onProgressChanged(SeekBarView seekBar);

        /**
         * 开始拖动
         *
         * @param seekBar
         */
        void onTrackingTouchStart(SeekBarView seekBar);

        /**
         * 拖动结束
         *
         * @param seekBar
         */
        void onTrackingTouchFinish(SeekBarView seekBar);

    }
}