package tw.noel.sung.com.toollist.tool.web.util.views;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;

import tw.noel.sung.com.toollist.tool.web.util.client.CustomWebChromeClient;
import tw.noel.sung.com.toollist.tool.web.util.client.CustomWebViewClient;

public class CustomWebView extends WebView {

    private Context context;
    private CustomWebViewClient customWebViewClient;
    private CustomWebChromeClient customWebChromeClient;
    private WebSettings webSettings;

    public CustomWebView(Context context) {
        this(context, null);
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initSetting();
        init();
    }

    //----------
    private void init() {
        customWebViewClient = new CustomWebViewClient(context);
        customWebChromeClient = new CustomWebChromeClient();
        setWebViewClient(customWebViewClient);
        setWebChromeClient(customWebChromeClient);
    }

    //---------

    private void initSetting() {
        webSettings = getSettings();
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
    }
    //---------

    /***
     *  設置網頁載入進度監聽
     */
    public void setOnWebLoadingListener(CustomWebChromeClient.OnWebLoadingListener onWebLoadingListener) {
        customWebChromeClient.setOnWebLoadingListener(onWebLoadingListener);
    }



}
