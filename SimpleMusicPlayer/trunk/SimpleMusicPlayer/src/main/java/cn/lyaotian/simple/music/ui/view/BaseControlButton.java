package cn.lyaotian.simple.music.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import cn.lyaotian.simple.music.R;

/**
 * Created by lyaotian on 6/5/13.
 */
public abstract class BaseControlButton extends View {
    public static final float GAP_ICON = 0.12f;
    public static final float GAP_CIRCLE = 0.1f;
    public static final int CIRCLE_STOCKE = 3;

    protected int width;
    protected int height;
    protected Paint circlePaint;

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

        drawCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        circlePaint.setColor(getCurrentColor());
        int halfWidth = width / 2;
        float r = halfWidth - GAP_CIRCLE * width;
        canvas.drawCircle(halfWidth, halfWidth, r, circlePaint);
    }

    protected int getCurrentColor() {
        return Color.WHITE;
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
}
