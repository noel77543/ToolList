package tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.cover;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.surfaceview.CustomSurfaceView;


public class CoverLayout extends RelativeLayout implements ViewTreeObserver.OnGlobalLayoutListener {

    //半透明黑色
    private final String BACKGROUND_COLOR = "#D9040E15";
    //掃描條線條寬度
    private final int SCAN_BAR_WIDTH = 3;
    //上下動畫的掃描條
    private CoverScanBarView coverScanBarView;
    private int coverScanBarViewID;
    //閃光燈開關
    private CoverFlashButton coverFlashButton;
    private int coverFlashButtonID;
    //置頂 actionbar
    private CoverActionBar coverActionBar;
    private int coverActionBarID;
    //中心透明區域
    private TransparentView transparentView;
    private int transparentViewID;
    private CoverDrawable coverDrawable;
    //content
    private TextView textViewContent;
    private int textViewContentID;
    private final int CONTENT_SIZE = 16;

    private Context context;
    private int viewWidth;
    private int viewHeight;


    public CoverLayout(Context context) {
        super(context);
        this.context = context;
        init();
        getViewTreeObserver().addOnGlobalLayoutListener(this);

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
        coverFlashButton = new CoverFlashButton(context);
        coverFlashButtonID = View.generateViewId();
        coverFlashButton.setId(coverFlashButtonID);
        coverScanBarView = new CoverScanBarView(context);
        coverScanBarViewID = View.generateViewId();
        coverScanBarView.setId(coverScanBarViewID);
        textViewContent = new TextView(context);
        textViewContentID = View.generateViewId();
        textViewContent.setId(textViewContentID);


        initCoverActionBar();
        initCoverScanBarView();

        addView(textViewContent);
        addView(coverActionBar);
        addView(transparentView);
        addView(coverFlashButton);
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
        coverActionBar.setPadding(20, 30, 20, 30);
    }


    //------------

    /***
     * 中間掃描的scan bar
     */
    private void initCoverScanBarView() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SCAN_BAR_WIDTH);
        params.addRule(ALIGN_TOP, transparentViewID);
        params.leftMargin = CustomSurfaceView.range / 5;
        params.rightMargin = CustomSurfaceView.range / 5;
        coverScanBarView.setLayoutParams(params);
    }

    //----------------

    @Override
    public void onGlobalLayout() {

        getViewTreeObserver().removeOnGlobalLayoutListener(this);

        viewWidth = getWidth();
        viewHeight = getHeight();


        initTransparentView();
        initCoverFlashButton();
        initTextViewContent();
        coverScanBarView.start(0, 0, 0, CustomSurfaceView.range);
    }


    //-------------

    /***
     * 中心掃描區塊的透明view
     */
    private void initTransparentView() {
        LayoutParams params = new LayoutParams(CustomSurfaceView.range, CustomSurfaceView.range);
        params.leftMargin = (viewWidth - CustomSurfaceView.range) / 2;
        params.topMargin = ((viewHeight / 2) - (CustomSurfaceView.range / 2));
        transparentView.setLayoutParams(params);
    }

    //------------

    /***
     * 閃光燈按鈕
     */
    private void initCoverFlashButton() {

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.BELOW, transparentViewID);
        params.topMargin = CustomSurfaceView.range / 6 / 2;
        params.bottomMargin = CustomSurfaceView.range / 6 / 2;
        coverFlashButton.setLayoutParams(params);
        coverFlashButton.setText(context.getString(R.string.close));
    }

    //------------

    /***
     * 中心上方描述
     */
    private void initTextViewContent() {
        textViewContent.setGravity(Gravity.CENTER);
        textViewContent.setTextSize(CONTENT_SIZE);
        textViewContent.setTextColor(Color.WHITE);
        textViewContent.setPadding((int) (CustomSurfaceView.range * 0.08), 10, (int) (CustomSurfaceView.range * 0.08), 10);
        LayoutParams params = new LayoutParams(CustomSurfaceView.range, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, coverActionBarID);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.topMargin = CustomSurfaceView.range / 2;
        textViewContent.setLayoutParams(params);
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
     * 設置內文
     */
    public void setContent(String content) {
        textViewContent.setText(content);
    }


    //--------

    /***
     * 當按下coverFlashButton
     */
    public void setOnCoverFlashButtonClickListener(OnClickListener onClickCloseListener) {
        coverFlashButton.setOnCoverFlashButtonClickListener(onClickCloseListener);
    }



    //----------

    /***
     * 右上角照片按鈕
     */
    public void setOnPhotoButtonClickListener(OnClickListener onClickListener) {
        coverActionBar.setOnPhotoButtonClickListener(onClickListener);
    }


    //--------


    /***
     * 顯示 / 隱藏 右上角『照片』
     */
    public void setPhotoButtonVisibility(int visibility) {
        coverActionBar.setPhotoButtonVisibility(visibility);
    }

    //---------------

    public void setCoverFlashButtonText(String text){
        coverFlashButton.setText(text);
    }

}
