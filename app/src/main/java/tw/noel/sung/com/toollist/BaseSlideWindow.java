package tw.noel.sung.com.toollist;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

public abstract class BaseSlideWindow extends BaseWindow implements View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {

    private final int MIN_OFFSET = 8;
    //落點 x
    private int oldX;
    //落點 y
    private int oldY;
    //位移值 x
    private int offsetX;
    //位移值 y
    private int offsetY;

    private int oldLeft;
    private int oldRight;
    private int oldTop;
    private int oldBottom;

    public BaseSlideWindow(Context context) {
        super(context);
        contentView.setOnTouchListener(this);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (Build.VERSION.SDK_INT >= 16) {
            contentView.getViewTreeObserver()
                    .removeOnGlobalLayoutListener(this);
        } else {
            contentView.getViewTreeObserver()
                    .removeGlobalOnLayoutListener(this);
        }
        //紀錄初始上下左右距離
        oldLeft = contentView.getLeft();
        oldRight = contentView.getRight();
        oldTop = contentView.getTop();
        oldBottom = contentView.getBottom();
    }

    //-----------------

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            //手指接觸
            case MotionEvent.ACTION_DOWN:
                oldX = x;
                oldY = y;
                break;
            //手指移動
            case MotionEvent.ACTION_MOVE:
                // y - lastY >0 手指由上往下
                // y - lastY <0 手指由下往上

                //僅限反方向滑動 順方向移動至多回到原位
                offsetY = (contentView.getBottom() > oldBottom || (y - oldY) > 0) ? (y - oldY) : 0;
                offsetX = 0;
                contentView.layout(contentView.getLeft() + offsetX, contentView.getTop() + offsetY, contentView.getRight() + offsetX, contentView.getBottom() + offsetY);
                break;
            //手指抬起
            case MotionEvent.ACTION_UP:
                //滿足偏移10以上  消失且解除Action down所做的boolean限定
                if (Math.abs(offsetY) > MIN_OFFSET) {
                    dismiss();
                }
                //其他情況則回歸出現時的位置
                else {
                    contentView.layout(oldLeft, oldTop, oldRight, oldBottom);
                }
                break;
        }
        return true;
    }
}
