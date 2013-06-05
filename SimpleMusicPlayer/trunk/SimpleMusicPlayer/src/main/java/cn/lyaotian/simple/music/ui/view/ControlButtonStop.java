package cn.lyaotian.simple.music.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by lyaotian on 6/5/13.
 */
public class ControlButtonStop extends BaseControlButton {
    public static final String TAG = "ControlButtonStop";
    public static final float GAP_ICON = 0.15f;

    private Paint paint;

    public ControlButtonStop(Context context) {
        super(context);
    }

    public ControlButtonStop(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ControlButtonStop(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        parseAttributes(context.obtainStyledAttributes());
    }

    @Override
    protected void setup(){
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    private void parseAttributes(TypedArray a){
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas);
    }

    private void drawRect(Canvas canvas) {
        int halfWidth = width/2;
        float gapPix = GAP_ICON * width;

        float left = halfWidth - gapPix;
        float right = halfWidth + gapPix;
        float top = left;
        float bottom = right;
        canvas.drawRect(left, top, right, bottom, paint);
    }

}
