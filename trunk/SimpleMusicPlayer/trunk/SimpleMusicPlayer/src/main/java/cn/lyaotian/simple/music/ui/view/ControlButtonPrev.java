package cn.lyaotian.simple.music.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;

/**
 * Created by lyaotian on 6/5/13.
 */
public class ControlButtonPrev extends BaseControlButton {
    public static final String TAG = "ControlButtonPrev";
    public static final float GAP_ICON = 0.12f;
    public static final float GAP_ICON_VERTICAL = 0.2f;
    public static final float RECT_SIZE = 0.05f;
    public static final float RECT_OFFSET = 0.15f;

    private Paint paint;
    private Path trianglePath = new Path();

    public ControlButtonPrev(Context context) {
        super(context);
    }

    public ControlButtonPrev(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ControlButtonPrev(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        parseAttributes(context.obtainStyledAttributes());
    }

    @Override
    protected void setup(){
        super.setup();

        paint = new Paint();
        paint.setAntiAlias(true);
    }

    private void parseAttributes(TypedArray a){
        a.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(getCurrentColor());

        drawTriangle(canvas);
        drawRect(canvas);
    }

    private void drawRect(Canvas canvas) {
        int halfWidth = width/2;
        float rectVertical = GAP_ICON_VERTICAL * width;
        float rectSize = RECT_SIZE * width;
        float rectOffect = RECT_OFFSET * width;

        float left = halfWidth - rectSize;
        float right = halfWidth + rectSize;
        float top = halfWidth - rectVertical;
        float bottom = halfWidth + rectVertical;
        canvas.translate(-rectOffect, 0);
        canvas.drawRect(left, top, right, bottom, paint);
        canvas.translate(rectOffect, 0);
    }


    private void drawTriangle(Canvas canvas) {
        updateTrianglePath();

        int halfWidth = width/2;
        canvas.translate(10, 0);
        canvas.rotate(270, halfWidth, halfWidth);
        canvas.drawPath(trianglePath, paint);
        canvas.rotate(90, halfWidth, halfWidth);
        canvas.translate(-10, 0);

    }

    private void updateTrianglePath(){
        float gapPix = GAP_ICON * width;

        int halfWidth = width/2;
        Point pCenter = new Point(halfWidth, halfWidth);
        Point triangleP1 = new Point();
        Point triangleP2 = new Point();
        Point triangleP3 = new Point();

        //calc p1
        double rad = Math.toRadians(60);
        int len = (int) (Math.tan(rad) * gapPix);
        triangleP1.x = len + pCenter.x;
        triangleP1.y = pCenter.y + (int)gapPix;

        //calc p2
        triangleP2.x = pCenter.x - len;
        triangleP2.y = triangleP1.y;

        //calc p3
        triangleP3.x = pCenter.x;
        triangleP3.y = (int) (pCenter.y - 2 * gapPix);

        trianglePath.reset();
        trianglePath.moveTo(triangleP1.x, triangleP1.y);
        trianglePath.lineTo(triangleP2.x, triangleP2.y);
        trianglePath.lineTo(triangleP3.x, triangleP3.y);
        trianglePath.close();
    }
}
