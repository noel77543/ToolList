package tw.noel.sung.com.toollist.tool.web.util;

import android.content.Context;
/**
 * Created by noel on 2018/6/15.
 */
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;



public abstract class BasicPopupWindow extends PopupWindow {
    private Context context;
    private View contentView;

    public BasicPopupWindow(Context context) {

        contentView = LayoutInflater.from(context).inflate(getContentLayoutId(), null);
        setContentView(contentView);
        initContentView(contentView);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(getBackgroundDrawable());
        setOutsideTouchable(true);
        setFocusable(true);

    }

    //----------

    /***
     *  設置layout ID
     * @return
     */
    protected abstract int getContentLayoutId();
    //----------

    /***
     *find view by id
     */
    protected abstract void initContentView(View view);

    //---------

    protected abstract Drawable getBackgroundDrawable();

}
