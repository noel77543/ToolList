package tw.noel.sung.com.toollist.ui.dice_view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;

import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DiceView extends ViewGroup {

    //滑動值的分母  值越小滑動敏感度越高
    private final float RESISTANCE = 1f;
    //觸發滾動的最小滑動距離  單位 : PX
    private final int MAX_SPEED = 300;
    //旋轉的角度
    private final float ROTATE_ANGLE = 90;
    //三種狀態
    private static final int STATE_PRE = 0;
    private static final int STATE_NEXT = 1;
    private static final int STATE_NORMAL = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_PRE, STATE_NEXT, STATE_NORMAL})
    public @interface DiceStateType {
    }

    private @DiceStateType int currentState;
    private Camera camera;
    private Matrix matrix;

    //起始Item
    private int startIndex = 1;
    //當前item
    private int currentIndex = startIndex;
    //高度
    private int viewHeight;
    //前一刻的Y值
    private float oldY;
    //前一刻的X值
    private float oldX;
    private VelocityTracker velocityTracker;
    private Scroller scroller;


    public DiceView(Context context) {
        this(context, null);
    }

    public DiceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        camera = new Camera();
        matrix = new Matrix();

        if (scroller == null) {
            scroller = new Scroller(getContext(), new LinearInterpolator());
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(measureWidth, measureHeight);

        MarginLayoutParams params = (MarginLayoutParams) this.getLayoutParams();
        int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                measureWidth - (params.leftMargin + params.rightMargin), MeasureSpec.EXACTLY);
        int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                measureHeight - (params.topMargin + params.bottomMargin), MeasureSpec.EXACTLY);

        measureChildren(childWidthMeasureSpec, childHeightMeasureSpec);

        viewHeight = getMeasuredHeight();

        scrollTo(0, startIndex * viewHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        MarginLayoutParams params = (MarginLayoutParams) this.getLayoutParams();
        int childTop = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                if (i == 0) {
                    childTop += params.topMargin;
                }
                child.layout(params.leftMargin, childTop,
                        child.getMeasuredWidth() + params.leftMargin, childTop + child.getMeasuredHeight());
                childTop = childTop + child.getMeasuredHeight();
            }
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        for (int i = 0; i < getChildCount(); i++) {
            drawScreen(canvas, i, getDrawingTime());
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return false;
        }
        return true;
    }

    //-----------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        float y = event.getY();
        float x = event.getX();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                //當手指落下 如果滾動器尚未結束則
//                if (!scroller.isFinished()) {
                    //設置滾動器目前Y值為最終Y值 -> 停止滾動
                    scroller.setFinalY(scroller.getCurrY());
                    scroller.abortAnimation();
                    //
//                    scrollTo(0, getScrollY());
//                    Log.e("AAA", getScrollY() + "");
//                }
                oldY = y;
                oldX = x;
                break;
            case MotionEvent.ACTION_MOVE:


                int distanceX = (int) (oldX - x);
                int distanceY = (int) (oldY - y);
                oldY = y;
                oldX = x;
                if (scroller.isFinished()) {
                    recycleVerticalScroll(distanceY);
                }
                break;

            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);

                float yVelocity = velocityTracker.getYVelocity();
                //滑動的速度大於規定的速度，或者向上滑動時，上一頁頁面展杏初的高度超過1/2，則設定狀態為STATE_PRE
                if (yVelocity > MAX_SPEED || ((getScrollY() + viewHeight / 2) / viewHeight < startIndex)) {
                    currentState = STATE_PRE;
                } else if (yVelocity < -MAX_SPEED || ((getScrollY() + viewHeight / 2) / viewHeight > startIndex)) {
                    // 滑動的速度大於規定的速度，或者向下滑動時，下一頁頁面展現出的高度超過1/2，則設定狀態為STATE_NEXT
                    currentState = STATE_NEXT;
                } else {
                    currentState = STATE_NORMAL;
                }
                // 根據STATE進行對應的變化
                changeByState(currentState);
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
        }
        return true;
    }

    //-------------

    /***
     * 循環船值滾動
     * @param delta
     */
    private void recycleVerticalScroll(int delta) {
        delta = delta % viewHeight;
        delta = (int) (delta / RESISTANCE);
//        if (Math.abs(delta) > viewHeight / 4) {
//            return;
//        }
        if (getScrollY() <= 0 && currentIndex <= 0 && delta <= 0) {
            return;
        }
        if (viewHeight * currentIndex <= getScrollY() && currentIndex == getChildCount() - 1 && delta >= 0) {
            return;
        }
        scrollBy(0, delta);

//        Log.e("AAA",""+getScrollY());

        //手指由上往下滾
        if (getScrollY() < 8 && currentIndex != 0) {
            //設置前一個
            setPrevious();
//            scrollBy(0, viewHeight);
        }
        //手指由下往上滾
        else if (getScrollY() > (getChildCount() - 1) * viewHeight - 8) {
            //設置下一個
            setNext();
//            scrollBy(0, -viewHeight);
        }

    }
    //-----------

    private void changeByState(@DiceStateType int currentState) {
        switch (currentState) {
            case STATE_NORMAL:
                toNormalAction();
                break;
            case STATE_PRE:
                toPrePager();
                break;
            case STATE_NEXT:
                toNextPager();
                break;
        }
        invalidate();
    }

    /**
     *  當前頁
     */
    private void toNormalAction() {
        int startY;
        int delta;
        int duration;
        startY = getScrollY();
        delta = viewHeight * startIndex - getScrollY();
        duration = (Math.abs(delta)) * 4;
        scroller.startScroll(0, startY, 0, delta, duration);
    }

    /**
     * 上一頁
     */
    private void toPrePager() {
        int startY;
        int delta;
        int duration;
        //增加新的頁面
        setPrevious();
        //scroller起始座標
        startY = getScrollY() + viewHeight;
        setScrollY(startY);
        //scroller移動的距離
        delta = -(startY - startIndex * viewHeight);
        duration = (Math.abs(delta)) * 2;
        scroller.startScroll(0, startY, 0, delta, duration);
    }

    /**
     * 下一页
     */
    private void toNextPager() {
        int startY;
        int delta;
        int duration;
        setNext();
        startY = getScrollY() - viewHeight;
        setScrollY(startY);
        delta = viewHeight * startIndex - startY;
        duration = (Math.abs(delta)) * 2;
        scroller.startScroll(0, startY, 0, delta, duration);
    }

    private void setNext() {
        currentIndex = (currentIndex + 1) % getChildCount();

        int childCount = getChildCount();
        View view = getChildAt(0);
        removeViewAt(0);
        addView(view, childCount - 1);
    }

    private void setPrevious() {
        currentIndex = ((currentIndex - 1) + getChildCount()) % getChildCount();

        int childCount = getChildCount();
        View view = getChildAt(childCount - 1);
        removeViewAt(childCount - 1);
        addView(view, 0);
    }



    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    /**
     *繪製單個頁面
     * @param canvas
     * @param screen
     * @param drawingTime
     */
    private void drawScreen(Canvas canvas, int screen, long drawingTime) {
        // 得到當前子View的高度
        final int height = getHeight();
        final int scrollHeight = screen * height;
        final int scrollY = this.getScrollY();
        // 如果偏移量不足
        if (scrollHeight > scrollY + height || scrollHeight + height < scrollY) {
            return;
        }
        final View child = getChildAt(screen);
        final int faceIndex = screen;
        final float currentDegree = getScrollY() * (ROTATE_ANGLE / getMeasuredHeight());
        final float faceDegree = currentDegree - faceIndex * ROTATE_ANGLE;
        if (faceDegree > 90 || faceDegree < -90) {
            return;
        }
        final float centerY = (scrollHeight < scrollY) ? scrollHeight + height
                : scrollHeight;
        final float centerX = getWidth() / 2;
        final Camera camera = this.camera;
        final Matrix matrix = this.matrix;
        canvas.save();
        camera.save();
        camera.rotateX(faceDegree);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
        canvas.concat(matrix);
        drawChild(canvas, child, drawingTime);
        canvas.restore();
    }
}