package tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.cover;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class CoverScanBarView extends View {
    private final String SCAN_BAR_COLOR = "#fcb048";
    private TranslateAnimation translateAnimation;
    private final long ANIMATION_DURATION = 2000;
    public CoverScanBarView(Context context) {
        super(context);
        init();
    }
    //-----------

    private void init() {
        setBackground(getShape());
    }

    //------------

    /***
     * 開始動畫
     */
    public void start(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) {
        translateAnimation = new TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta);
        translateAnimation.setRepeatCount(Animation.INFINITE);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setDuration(ANIMATION_DURATION);
        translateAnimation.start();
        setAnimation(translateAnimation);
    }

    //-----------

    /***
     * 塑形
     * @return
     */
    private GradientDrawable getShape() {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(Color.parseColor(SCAN_BAR_COLOR));
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        return gradientDrawable;
    }
}
