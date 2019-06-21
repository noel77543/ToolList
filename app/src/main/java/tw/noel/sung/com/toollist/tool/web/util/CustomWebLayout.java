package tw.noel.sung.com.toollist.tool.web.util;

import android.app.Activity;
import android.content.Context;
/**
 * Created by noel on 2018/6/20.
 */
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import tw.noel.sung.com.toollist.tool.web.util.client.CustomWebChromeClient;
import tw.noel.sung.com.toollist.tool.web.util.views.CustomProgressBar;
import tw.noel.sung.com.toollist.tool.web.util.views.CustomWebView;


public class CustomWebLayout extends RelativeLayout implements CustomWebChromeClient.OnWebLoadingListener {
    private Context context;

    private CustomWebView customWebView;
    private final int ID_WEB_VIEW = View.generateViewId();

    private CustomProgressBar customProgressBar;
    private final int ID_PROGRESS_BAR = View.generateViewId();


    public CustomWebLayout(Context context) {
        this(context, null);
    }
    //----------

    public CustomWebLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    //----------

    public CustomWebLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }


    //---------------

    private void init() {
        initProgressBar();
        initWebView();
        addView(customProgressBar);
        addView(customWebView);
    }

    //-----------

    private void initProgressBar() {
        customProgressBar = new CustomProgressBar(context);
        customProgressBar.setId(ID_PROGRESS_BAR);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 50);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        customProgressBar.setLayoutParams(layoutParams);
    }

    //---------------

    private void initWebView() {
        customWebView = new CustomWebView(context);
        customWebView.setId(ID_WEB_VIEW);
        customWebView.setOnWebLoadingListener(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.BELOW, ID_PROGRESS_BAR);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        customWebView.setLayoutParams(layoutParams);
    }


    //----------------------

    /***
     *  當網頁讀取進度更新
     * @param progress
     */
    @Override
    public void onWebViewLoading(int progress) {
        customProgressBar.setProgress(progress);
        customProgressBar.setVisibility(progress >= 95 ? GONE : VISIBLE);
    }


    //----------------------

    /**
     * load url
     */
    public void open(String url) {
        customWebView.loadUrl(url);
    }

    //-----------

    /***
     *  返回
     */
    public void back() {
        if (customWebView.canGoBack()) {
            customWebView.goBack();
        } else {
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        }
    }

    //-------------

    /***
     *  取得目前網頁之網址
     * @return
     */
    public String getCurrentWebUrl() {
        return customWebView.getUrl();
    }

}
