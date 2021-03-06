package com.renren.wz.tagselected.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.renren.wz.tagselected.R;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;


/**
 * Created by zan on 2018/7/13.
 * 标签条目view
 */
public class UserColorTagView extends AppCompatTextView implements Checkable, View.OnClickListener {

    private static final int DEFAULT_TAG_COLOR = Color.BLACK;
    private static final int DEFAULT_TAG_DISABLE_COLOR = Color.parseColor("#979797");

    private int mTagColor;
    private int mDisableColor;
    private Paint mPaint;
    private float mStrokeWidth;

    private RectF mRect;
    private RectF mStrokeRect;

    private boolean mChecked;

    public UserColorTagView(Context context) {
        this(context, null);
    }

    public UserColorTagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UserColorTagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorTagView);
        final boolean clickable = a.getBoolean(R.styleable.ColorTagView_tag_clickable, true);
        mTagColor = a.getColor(R.styleable.ColorTagView_tag_color, DEFAULT_TAG_COLOR);
        mDisableColor = a.getColor(R.styleable.ColorTagView_tag_disable_color, DEFAULT_TAG_DISABLE_COLOR);
        mStrokeWidth = a.getDimension(R.styleable.ColorTagView_tag_stroke_width, getResources().getDisplayMetrics().density);
        a.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mStrokeWidth);

        super.setOnClickListener(this);
        setClickable(clickable);
        changeTextColor();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRect = new RectF(0, 0, w, h);
        final float stroke = mStrokeWidth / 2;
        mStrokeRect = new RectF(0 + stroke, 0 + stroke, w - stroke, h - stroke);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final float radius = (mRect.bottom - mRect.top) / 2;
        if (isEnabled()) {
            if (mChecked) {
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mTagColor);
                canvas.drawRoundRect(mRect, radius, radius, mPaint);
            } else {
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(Color.WHITE);
                canvas.drawRoundRect(mRect, radius, radius, mPaint);

                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mTagColor);
                canvas.drawRoundRect(mStrokeRect, radius, radius, mPaint);
            }
        } else {
            if (mChecked) {
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mTagColor);
                canvas.drawRoundRect(mRect, radius, radius, mPaint);
            }else{
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(Color.WHITE);
                canvas.drawRoundRect(mRect, radius, radius, mPaint);

                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(mDisableColor);
                canvas.drawRoundRect(mStrokeRect, radius, radius, mPaint);
            }

        }
        super.onDraw(canvas);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        changeTextColor();
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        changeTextColor();
        postInvalidate();
    }

    private void changeTextColor() {
        if (isEnabled()) {
            if (isChecked()) {
                setTextColor(Color.WHITE);
            } else {
                setTextColor(mTagColor);
            }
        } else {
            if (isChecked()) {
                setTextColor(Color.WHITE);
            }else{
                setTextColor(mDisableColor);
            }

        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
        postInvalidate();
    }

    @Override
    public void onClick(View v) {
        toggle();
        if (mListener != null) {
            mListener.onClick(v);
        }
    }

    public void setTagColor(@ColorInt int color) {
        mTagColor = color;
        changeTextColor();
        postInvalidate();
    }

    public void setTagDisableColor(@ColorInt int color) {
        mDisableColor = color;
        changeTextColor();
        postInvalidate();
    }

    private View.OnClickListener mListener;

    @Override
    public void setOnClickListener(@Nullable View.OnClickListener l) {
        mListener = l;
    }
}
