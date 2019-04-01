package tw.noel.sung.com.toollist.tool.password_window.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import butterknife.ButterKnife;


/**
 * Created by noel on 2018/4/8.
 */

public abstract class BaseWindow extends PopupWindow implements View.OnTouchListener {

    private final int MIN_OFFSET = 8;
    private LayoutInflater inflater;
    private View contentView;
    protected Context context;
    protected LayoutSizeTool layoutSizeTool;
    protected int windowWidth;
    protected int windowHeight;
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

    private boolean isLock = false;

    public BaseWindow(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (contentView == null) {
            contentView = inflater.inflate(getContentViewId(), null);
        }
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        contentView.setOnTouchListener(this);
        layoutSizeTool = new LayoutSizeTool(context);
        int[] phoneSize = layoutSizeTool.getPhoneSize();
        windowWidth = phoneSize[0];
        windowHeight = phoneSize[1];
        init();
    }


    //----------

    /***
     *  背景變暗
     * @param alpha
     */
    public void setWindowBackgroundAlpha(Activity activity, float alpha) {

        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = alpha;
        window.setAttributes(layoutParams);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            //手指接觸
            case MotionEvent.ACTION_DOWN:
                oldX = x;
                oldY = y;

                if(!isLock){
                    isLock = true;
                    //當初次手指落下 紀錄上下左右距離
                    oldLeft = contentView.getLeft();
                    oldRight = contentView.getRight();
                    oldTop = contentView.getTop();
                    oldBottom = contentView.getBottom();
                }

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

    //----------

    /***
     *  view id
     * @return
     */
    protected abstract int getContentViewId();

    //----------

    protected abstract void init();

}
