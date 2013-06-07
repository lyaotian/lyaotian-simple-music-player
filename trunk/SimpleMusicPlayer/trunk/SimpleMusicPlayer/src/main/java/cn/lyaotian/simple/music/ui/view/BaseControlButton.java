package cn.lyaotian.simple.music.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import cn.lyaotian.simple.music.R;

/**
 * Created by lyaotian on 6/5/13.
 */
public abstract class BaseControlButton extends View {
    public static final String TAG = "BaseControlButton";

    public static final int DEFAULT_COLOR = Color.GREEN;
    public static final float GAP_ICON = 0.12f;
    public static final float GAP_CIRCLE = 0.1f;
    public static final int CIRCLE_STOCKE = 3;

    protected int width;
    protected int height;
    protected Paint circlePaint;
    protected ColorStateList mForegroundColor;
    protected int mCurForegroundColor;

    public BaseControlButton(Context context) {
        super(context);
        setup();
    }

    public BaseControlButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public BaseControlButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
//        parseAttributes(context.obtainStyledAttributes());
    }

    protected void setup() {
        Resources resources = getResources();
        width = resources.getDimensionPixelSize(R.dimen.control_button_default_size);
        height = width;

        mForegroundColor = resources.getColorStateList(R.drawable.color_control_button);
        setForegroundColor(mForegroundColor);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(CIRCLE_STOCKE);
    }

    private void parseAttributes(TypedArray a){
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circlePaint.setColor(mCurForegroundColor);

        drawCircle(canvas);
    }

    public void setForegroundColor(ColorStateList colors){
        if (colors == null) {
            throw new NullPointerException();
        }

        mForegroundColor = colors;
        updateTextColors();
    }

    private void drawCircle(Canvas canvas) {
        int halfWidth = width / 2;
        float r = halfWidth - GAP_CIRCLE * width;
        canvas.drawCircle(halfWidth, halfWidth, r, circlePaint);
    }

    protected int getCurrentColor() {
        int color = DEFAULT_COLOR;
        if(mForegroundColor != null){
            color = mForegroundColor.getColorForState(getDrawableState(), DEFAULT_COLOR);
            Log.d(TAG, "getCurrentColor color=" + color + "; white=" + DEFAULT_COLOR);
        }
        return color;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int defaultSize = width;

        switch (mode){
            case MeasureSpec.EXACTLY:
                width = size;
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                width = Math.min(defaultSize, size);
                break;
            default:
                throw new IllegalArgumentException("unknow spec mode");
        }
        return width;
    }

    private int measureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int defaultSize = height;
        switch (mode){
            case MeasureSpec.EXACTLY:
                height = size;
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                height = Math.min(defaultSize, size);
                break;
            default:
                throw new IllegalArgumentException("unknow spec mode");
        }
        return height;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    private void updateTextColors() {
        boolean inval = false;
        int color = mForegroundColor.getColorForState(getDrawableState(), 0);
        if (color != mCurForegroundColor) {
            mCurForegroundColor = color;
            inval = true;
        }

        if (inval) {
            invalidate();
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mForegroundColor != null && mForegroundColor.isStateful()) {
            updateTextColors();
            return;
        }

//        int [] drawableState = getDrawableState();
//
//        for(int state : drawableState){
//            switch (state){
//                case android.R.attr.state_focused:
//                    Log.e(TAG, "state_focused");
//                    break;
//                case android.R.attr.state_selected:
//                    Log.e(TAG, "state_selected");
//                    break;
//                case android.R.attr.state_pressed:
//                    Log.e(TAG, "state_pressed");
//                    break;
//                case android.R.attr.state_checked:
//                    Log.e(TAG, "state_checked");
//                    break;
//                case android.R.attr.state_above_anchor:
//                    Log.e(TAG, "state_above_anchor");
//                    break;
//                case android.R.attr.state_accelerated:
//                    Log.e(TAG, "state_accelerated");
//                    break;
//                case android.R.attr.state_activated:
//                    Log.e(TAG, "state_activated");
//                    break;
//                case android.R.attr.state_active:
//                    Log.e(TAG, "state_active");
//                    break;
//                case android.R.attr.state_checkable:
//                    Log.e(TAG, "state_checkable");
//                    break;
//                case android.R.attr.state_drag_can_accept:
//                    Log.e(TAG, "state_drag_can_accept");
//                    break;
//                case android.R.attr.state_drag_hovered:
//                    Log.e(TAG, "state_drag_hovered");
//                    break;
//                case android.R.attr.state_empty:
//                    Log.e(TAG, "state_empty");
//                    break;
//                case android.R.attr.state_enabled:
//                    Log.e(TAG, "state_enabled");
//                    break;
//                case android.R.attr.state_expanded:
//                    Log.e(TAG, "state_expanded");
//                    break;
//                case android.R.attr.state_first:
//                    Log.e(TAG, "state_first");
//                    break;
//                case android.R.attr.state_hovered:
//                    Log.e(TAG, "state_hovered");
//                    break;
//                case android.R.attr.state_last:
//                    Log.e(TAG, "state_last");
//                    break;
//                case android.R.attr.state_long_pressable:
//                    Log.e(TAG, "state_long_pressable");
//                    break;
//                case android.R.attr.state_middle:
//                    Log.e(TAG, "state_middle");
//                    break;
//                case android.R.attr.state_multiline:
//                    Log.e(TAG, "state_multiline");
//                    break;
//                case android.R.attr.state_single:
//                    Log.e(TAG, "state_single");
//                    break;
//                case android.R.attr.state_window_focused:
//                    Log.e(TAG, "state_window_focused");
//                    break;
//                default:
//                    Log.e(TAG, "unknow state");
//            }
//        }
//        Log.i(TAG, "----------------------------");
    }
}
