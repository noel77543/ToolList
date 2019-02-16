package tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.cover;

import android.content.Context;
/**
 * Created by noel on 2019/2/16.
 */
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.surfaceview.CustomSurfaceView;


public class CoverLayout extends RelativeLayout implements Runnable {


    private final String BACKGROUND_COLOR = "#D9040E15";
    private final int SCAN_BAR_WIDTH = 3;
    private CoverScanBarView coverScanBarView;
    private int coverScanBarViewID;

    private CoverButton coverButton;
    private int coverButtonID;
    private CoverActionBar coverActionBar;
    private int coverActionBarID;
    private TransparentView transparentView;
    private int transparentViewID;

    private Context context;
    private CoverDrawable coverDrawable;

    private int viewWidth;
    private int viewHeight;


    public CoverLayout(Context context) {
        super(context);
        this.context = context;
        init();
        post(this);
    }


    //------------

    private void init() {

        coverDrawable = new CoverDrawable(new ColorDrawable(Color.parseColor(BACKGROUND_COLOR)));
        setBackground(coverDrawable);
        coverActionBar = new CoverActionBar(context);
        coverActionBarID = View.generateViewId();
        coverActionBar.setId(coverActionBarID);
        transparentView = new TransparentView(context);
        transparentViewID = View.generateViewId();
        transparentView.setId(transparentViewID);
        coverButton = new CoverButton(context);
        coverButtonID = View.generateViewId();
        coverButton.setId(coverButtonID);
        coverScanBarView = new CoverScanBarView(context);
        coverScanBarViewID = View.generateViewId();
        coverScanBarView.setId(coverScanBarViewID);
        initCoverActionBar();
        initCoverScanBarView();

        addView(coverActionBar);
        addView(transparentView);
        addView(coverButton);
        addView(coverScanBarView);
    }


    //------------

    /***
     * init 上方bar
     */
    private void initCoverActionBar() {

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_TOP);
        params.addRule(ALIGN_PARENT_START);
        coverActionBar.setLayoutParams(params);

    }
    //------------

    /***
     * 中間掃描的scan bar
     */
    private void initCoverScanBarView() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SCAN_BAR_WIDTH);
        params.addRule(ALIGN_TOP, transparentViewID);
        params.leftMargin = CustomSurfaceView.RANGE / 5;
        params.rightMargin = CustomSurfaceView.RANGE / 5;
        coverScanBarView.setLayoutParams(params);
    }
    //-------------

    /***
     * 中心掃描區塊的透明view
     */
    private void initTransparentView() {
        LayoutParams params = new LayoutParams(CustomSurfaceView.RANGE, CustomSurfaceView.RANGE);
        params.leftMargin = (viewWidth - CustomSurfaceView.RANGE) / 2;
        params.topMargin = (viewHeight - CustomSurfaceView.RANGE) / 2;
        transparentView.setLayoutParams(params);
    }


    //------------

    private void initCoverButton(int buttonSize) {

        LayoutParams params = new LayoutParams(buttonSize, buttonSize);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.BELOW, transparentViewID);
        coverButton.setLayoutParams(params);
        coverButton.setText(context.getString(R.string.close));
    }
    //------------

    @Override
    public void run() {
        viewWidth = getWidth();
        viewHeight = getHeight();


        initTransparentView();
        initCoverButton(CustomSurfaceView.RANGE / 5);
        coverScanBarView.start(0, 0, 0, CustomSurfaceView.RANGE);
    }


    //------------


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Path path = null;


        if (transparentView != null) {
            path = new Path();
            // 矩形透明區域
            path.addRoundRect(transparentView.getLeft(), transparentView.getTop(), transparentView.getRight(), transparentView.getBottom(), 30, 30, Path.Direction.CW);
        }
        if (path != null) {
            coverDrawable.setSrcPath(path);
        }
    }

    //--------

    /***
     * 設置標題
     * @param title
     */
    public void setTitle(String title) {
        coverActionBar.setTitle(title);
    }

    //--------

    /***
     * 當按下coverbutton
     */
    public void setOnCoverButtonClickListener(OnClickListener onClickCloseListener) {
        coverButton.setOnCoverButtonClickListener(onClickCloseListener);
    }
    //-------

    /***
     * 更改按鈕文字
     * @param text
     */
    public void setCoverButtonText(String text){
        coverButton.setText(text);
    }

}
