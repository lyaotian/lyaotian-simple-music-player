package cn.lyaotian.simple.music.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;

/**
 * Created by lyaotian on 6/5/13.
 */
public class ControlButtonPause extends BaseControlButton {
    public static final String TAG = "ControlButtonPause";
    public static final float ICON_SIZE = 0.04f;
    public static final float MIDDLE_GAP = 0.05f;
    public static final float GAP_ICON = 0.2f;

    private Paint paint;
    private Path p1 = new Path();
    private Path p2 = new Path();

    public ControlButtonPause(Context context) {
        super(context);
    }

    public ControlButtonPause(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ControlButtonPause(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        parseAttributes(context.obtainStyledAttributes());
    }

    @Override
    protected void setup(){
        super.setup();
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(getCurrentColor());

        drawRect(canvas);
    }


    private void drawRect(Canvas canvas) {
        updatePath();

        int halfWidth = width/2;
        float gapPix = GAP_ICON * width;
        float iconSizePix = ICON_SIZE * width;
        float middle = MIDDLE_GAP * width;

        float left = halfWidth - iconSizePix;
        float top = halfWidth - gapPix;
        float right = halfWidth + iconSizePix;
        float bottom = halfWidth + gapPix;

        float t = iconSizePix + middle;
        canvas.translate(-t, 0);
        canvas.drawRect(left, top, right, bottom, paint);

        canvas.translate(t * 2, 0);
        canvas.drawRect(left, top, right, bottom, paint);
        canvas.translate(-t, 0);
    }

    private void updatePath(){
        float gapPix = GAP_ICON * width;
        float iconSizePix = ICON_SIZE * width;

        int halfWidth = width/2;

        Point point1 = new Point((int)(halfWidth - iconSizePix), (int)(halfWidth - gapPix));
        Point point2 = new Point((int)(halfWidth + iconSizePix), point1.y);
        Point point3 = new Point(point2.x, (int)(point1.y + gapPix));

        p1.reset();
        p1.moveTo(point1.x, point1.y);
        p1.lineTo(point2.x, point2.y);
        p1.lineTo(point3.x, point3.y);
        p1.close();
    }

}
