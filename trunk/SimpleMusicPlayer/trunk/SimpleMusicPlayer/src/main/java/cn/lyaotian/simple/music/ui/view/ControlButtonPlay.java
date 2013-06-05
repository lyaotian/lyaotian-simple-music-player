package cn.lyaotian.simple.music.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;

/**
 * Created by lyaotian on 6/4/13.
 */
public class ControlButtonPlay extends BaseControlButton {
    public static final String TAG = "ControlButtonPlay";
    private Path trianglePath = new Path();

    public ControlButtonPlay(Context context) {
        super(context);
    }

    public ControlButtonPlay(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ControlButtonPlay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawTriangle(canvas);
    }

    private void drawTriangle(Canvas canvas) {
        updateTrianglePath();

        int halfWidth = width/2;
        canvas.rotate(90, halfWidth, halfWidth);
        canvas.drawPath(trianglePath, paint);
        canvas.rotate(270, halfWidth, halfWidth);

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
