package tw.noel.sung.com.toollist.tool.web.util.client;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class CustomWebChromeClient extends WebChromeClient {

    private OnWebLoadingListener onWebLoadingListener;


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if(onWebLoadingListener != null){
            onWebLoadingListener.onWebViewLoading(newProgress);
        }
    }

    //------------------


    public interface OnWebLoadingListener {
        void onWebViewLoading(int progress);
    }

    //--------------

    public void setOnWebLoadingListener(OnWebLoadingListener onWebLoadingListener) {
        this.onWebLoadingListener = onWebLoadingListener;
    }
}
