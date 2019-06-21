package tw.noel.sung.com.toollist.tool.web.util.client;

import android.app.AlertDialog;
/**
 * Created by noel on 2018/6/20.
 */
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import tw.noel.sung.com.toollist.R;



public class CustomWebViewClient extends WebViewClient {

    private Context context;

    public CustomWebViewClient(Context context) {
        this.context = context;
    }

    //-----------

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return super.shouldOverrideUrlLoading(view, url);
    }
    //-----------

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }
    //-----------

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }
    //-----------

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

    //-----------

    /***
     *  需另加上 才支援https 且需 補上判斷 否則上架會遭拒 _noel
     */
    @Override
    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(context.getString(R.string.web_view_ssl_title));
        builder.setMessage(context.getString(R.string.web_view_ssl_message));
        builder.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });
        builder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
                Toast.makeText(context, context.getString(R.string.web_view_ssl_toast), Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
