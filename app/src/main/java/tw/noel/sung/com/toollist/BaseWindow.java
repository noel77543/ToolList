package tw.noel.sung.com.toollist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import butterknife.ButterKnife;
import tw.noel.sung.com.toollist.tool.password_window.util.LayoutSizeTool;


/**
 * Created by noel on 2018/4/8.
 */

public abstract class BaseWindow extends PopupWindow {

    private LayoutInflater inflater;
    protected View contentView;
    protected Context context;
    protected LayoutSizeTool layoutSizeTool;
    protected int windowWidth;
    protected int windowHeight;



    public BaseWindow(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (contentView == null) {
            contentView = inflater.inflate(getContentViewId(), null);
        }
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
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




    //----------

    /***
     *  view id
     * @return
     */
    protected abstract int getContentViewId();

    //----------

    protected abstract void init();

}
