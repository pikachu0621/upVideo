package com.pikachu.upvideo.util.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;

import com.pikachu.upvideo.R;

/**
 * @author Pikachu
 * @Project upVideo
 * @Package com.pikachu.upvideo.util.view
 * @Date 2021/1/28 ( 下午 6:02 )
 * @description
 */
public class CameraBtView extends View {


    @FloatRange(from = 0, to = 1)
    private float maxToMin = 0.2F; // 大圆和小圆的间隔 (百分比 0~1)
    @ColorInt
    private int maxWasColor = 0x60FF0000, maxNoColor = 0xFFFFFFFF; // 外部圆 选中/不选中 颜色（大圆颜色）
    @ColorInt
    private int minWasColor = 0xFFFF0000, minNoColor = 0xFFFF0000; // 内部圆 选中/不选中 颜色（小圆颜色）
    private boolean isChangeColor = false; // 是否在在按下时改变颜色


    //以下不用改
    private float maxOfRadius = 0; // 外部圆半径（大圆半径）
    private float minOfRadius = 0; // 内部圆半径（小圆半径）
    private final Paint paint = new Paint(); // 画笔
    private float viewCX = 0, viewCY = 0; // 中心
    private boolean isSed = false;
    private OnClickListener onClickListener;


    public interface OnClickListener{
        /**
         * 监听点击事件
         * @param view this
         * @param isSed 是否点击状态
         * @return true 点击成功 / false 点击失败
         */
        boolean onClick(View view,boolean isSed);
    }


    public CameraBtView(Context context) {
        this(context, null);
    }

    public CameraBtView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraBtView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CameraBtView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        @SuppressLint("Recycle")
        TypedArray array = context.obtainStyledAttributes(
                attrs, R.styleable.CameraBtView, defStyleAttr, 0);
        maxToMin = array.getFloat(R.styleable.CameraBtView_pkMaxToMin, maxToMin);
        if (maxToMin > 1) maxToMin = 1;
        else if (maxToMin < 0) maxToMin = 0;
        maxWasColor = array.getColor(R.styleable.CameraBtView_pkMaxWasColor, maxWasColor);
        minWasColor = array.getColor(R.styleable.CameraBtView_pkMinWasColor, minWasColor);
        maxNoColor = array.getColor(R.styleable.CameraBtView_pkMaxNoColor, maxNoColor);
        minNoColor = array.getColor(R.styleable.CameraBtView_pkMinNoColor, minNoColor);
        isChangeColor = array.getBoolean(R.styleable.CameraBtView_pkIsChangeColor, isChangeColor);
    }

    private void getRadius(int widthMeasureSpec, int heightMeasureSpec) {
        maxOfRadius = Math.min(widthMeasureSpec, heightMeasureSpec) >> 1;
        minOfRadius = maxOfRadius - maxOfRadius * maxToMin;
        viewCX = widthMeasureSpec >> 1;
        viewCY = heightMeasureSpec >> 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getRadius(getWidth(), getHeight());
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(isSed ? maxWasColor : maxNoColor);
        canvas.drawCircle(viewCX, viewCY, maxOfRadius, paint);
        paint.setColor(isSed ? minWasColor : minNoColor);
        canvas.drawCircle(viewCX, viewCY, isSed ? minOfRadius * 0.7F : minOfRadius, paint);
    }


    @SuppressLint("ClickableViewAccessibility")
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onClickListener != null) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN && isChangeColor)
                isSed = !isSed;
            else if (action == MotionEvent.ACTION_UP) {
                if (onClickListener.onClick(this, !isSed))
                    isSed = !isSed;
            }
            invalidate();
            return true;
        }
        return super.onTouchEvent(event);

    }

    public float getMaxToMin() {
        return maxToMin;
    }


    public void setMaxToMin(@SuppressLint("SupportAnnotationUsage")
                            @FloatRange(from = 0, to = 1) float maxToMin) {
        this.maxToMin = maxToMin;
        invalidate();
    }

    public int getMaxWasColor() {
        return maxWasColor;
    }

    public void setMaxWasColor(int maxWasColor) {
        this.maxWasColor = maxWasColor;
        invalidate();
    }

    public int getMaxNoColor() {
        return maxNoColor;
    }

    public void setMaxNoColor(int maxNoColor) {
        this.maxNoColor = maxNoColor;
        invalidate();
    }

    public int getMinWasColor() {
        return minWasColor;
    }

    public void setMinWasColor(int minWasColor) {
        this.minWasColor = minWasColor;
        invalidate();
    }

    public int getMinNoColor() {
        return minNoColor;
    }

    public void setMinNoColor(int minNoColor) {
        this.minNoColor = minNoColor;
        invalidate();
    }

    public boolean isSed() {
        return isSed;
    }

    public void setSed(boolean sed) {
        isSed = sed;
        invalidate();
    }

    public boolean isChangeColor() {
        return isChangeColor;
    }

    public void setChangeColor(boolean changeColor) {
        isChangeColor = changeColor;
    }
}
