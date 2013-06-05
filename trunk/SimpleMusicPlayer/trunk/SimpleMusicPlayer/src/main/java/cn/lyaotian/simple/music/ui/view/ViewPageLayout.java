package cn.lyaotian.simple.music.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class ViewPageLayout extends ViewGroup {
    public static final String TAG = "ViewPageLayout";
    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;
    private static final int SNAP_VELOCITY = 600;
    private static final int TOUCH_SLOP = 20;

    private Context mContext;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mCurrentScreen;
    private int mTouchState = TOUCH_STATE_REST;
    private float mLastMotionX;
    private float mLastMotionY;
    private int mScreenOffsetX;
    private OnPageChangeListener onPageChangeListener;
    private boolean flag;
    private boolean enableScrollOver = true;

    public ViewPageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
        setup();
    }

    public ViewPageLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        setup();
    }

    private void setup() {
        mScroller = new Scroller(mContext);
        snapToScreen(mCurrentScreen);
        setWillNotDraw(true);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);

            if (childView.getVisibility() != View.GONE) {
                final int childWidth = childView.getMeasuredWidth();
                childView.layout(
                        childLeft,
                        0,
                        childLeft + childWidth,
                        childView.getMeasuredHeight());

                childLeft += childWidth;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException(
                    "ScrollLayout only canmCurScreen run at EXACTLY mode!");
        }

        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException("ScrollLayout only can run at EXACTLY mode!");
        }

        // The children are given the same width and height as the scrollLayout
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }

        // Log.d(TAG, "moving to screen "+mCurScreen);
        scrollTo(mCurrentScreen * width, 0);
    }

    /**
     * 根据X轴Scroll到的地方跳到合适的屏幕
     */
    public void snapToDestination() {
        int width = getMeasuredWidth();
        final int destScreen = (getScrollX() + width / 2) / width;
        snapToScreen(destScreen);
    }

    /**
     * 跳到指定屏幕
     */
    private void snapToScreen(int whichScreen) {
        // get the valid layout page
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

        if (getScrollX() != (whichScreen * getWidth())) {
            final int delta = whichScreen * getWidth() - getScrollX();
            mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2);
            mCurrentScreen = whichScreen;
            invalidate(); // Redraw the layout
        }
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageSelected(mCurrentScreen);
        }
    }

    public int getCurrentScreen() {
        return mCurrentScreen;
    }

    public void setCurrentScreen(int screen){
        this.mCurrentScreen = screen;
        requestLayout();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            int currX = mScroller.getCurrX();
            int currY = mScroller.getCurrY();
            scrollTo(currX, currY);
            invalidate();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }

        final float x = event.getX();
        final float y = event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                flag = false;
                mLastMotionX = x;
                mLastMotionY = y;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
                break;
            case MotionEvent.ACTION_MOVE:
                if (flag) {
                    return false;
                }

                final int xDiff = (int) Math.abs(mLastMotionX - x);
                final int yDiff = (int) Math.abs(mLastMotionY - y);
                if (xDiff > TOUCH_SLOP) {
                    mTouchState = TOUCH_STATE_SCROLLING;
                }
                if (yDiff > TOUCH_SLOP) {
                    mTouchState = TOUCH_STATE_REST;
                    flag = true;
                }

                if (xDiff > TOUCH_SLOP && yDiff > TOUCH_SLOP) {
                    if (xDiff > yDiff) {
                        mTouchState = TOUCH_STATE_SCROLLING;
                    } else {
                        mTouchState = TOUCH_STATE_REST;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(event);

        int action = event.getAction();
        float x = event.getX();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScreenOffsetX = 0;
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (mLastMotionX - x);
                mScreenOffsetX += deltaX;
                mLastMotionX = x;

                if(enableScrollOver){
                    scrollBy(deltaX, 0);
                }else{
                    if(mCurrentScreen == 0){
                        if(mScreenOffsetX > 0){
                            scrollBy(deltaX, 0);
                        }
                    }else if(mCurrentScreen == getChildCount() - 1) {
                        if(mScreenOffsetX < 0){
                            scrollBy(deltaX, 0);
                        }
                    }else{
                        scrollBy(deltaX, 0);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mScreenOffsetX = 0;
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity();

                if (velocityX > SNAP_VELOCITY && mCurrentScreen > 0) {// Fling enough to move left
                    snapToScreen(mCurrentScreen - 1);
                } else if (velocityX < -SNAP_VELOCITY && mCurrentScreen < getChildCount() - 1) {// Fling enough to move right
                    snapToScreen(mCurrentScreen + 1);
                } else {
                    snapToDestination();
                }
                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                mScreenOffsetX = 0;
                mTouchState = TOUCH_STATE_REST;
                break;
        }

        if(onPageChangeListener != null){
            int width = getWidth();

            int offsetX = mScreenOffsetX;
            int position = mCurrentScreen;

            if(mScreenOffsetX < 0){
                if(mCurrentScreen <= 0){
                    offsetX = 0;
                }else{
                    offsetX = width + mScreenOffsetX;
                    position = mCurrentScreen - 1;
                }
            }else if(mCurrentScreen >= getChildCount() - 1){
                offsetX = 0;
            }

            onPageChangeListener.onPageScrolled(position, ((float)offsetX)/width, offsetX);
        }
        return true;
    }

    public OnPageChangeListener getOnPageChangeListener() {
        return onPageChangeListener;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public interface OnPageChangeListener{
        public void onPageScrollStateChanged(int state);
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        public void onPageSelected(int position);
    }

}
