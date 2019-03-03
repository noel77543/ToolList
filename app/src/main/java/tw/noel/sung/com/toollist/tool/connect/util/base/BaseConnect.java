package tw.noel.sung.com.toollist.tool.connect.util.base;

import android.annotation.SuppressLint;
import android.content.Context;
/**
 * Created by noel on 2019/1/21.
 */
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.google.gson.Gson;

import java.io.Reader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import tw.noel.sung.com.toollist.tool.connect.util.dialog.ZLoadingDialog;
import tw.noel.sung.com.toollist.tool.connect.util.implement.ConnectCallback;

public class BaseConnect {


    public final static MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    public final static String _FILE_TYPE_JPG = "image/jpg";
    public final static String _FILE_TYPE_PNG = "image/png";
    public final static String _FILE_TYPE_CSV = "text/csv";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({_FILE_TYPE_JPG, _FILE_TYPE_PNG, _FILE_TYPE_CSV})
    public @interface uploadFileType {
    }


    protected final int TIME_OUT = 15000;

    protected OkHttpClient client;
    protected Request request;
    protected Gson gson;
    protected RequestBody requestBody;
    protected Context context;
    protected ZLoadingDialog zLoadingDialog;


    public static final int SHOW_DIALOG = 77;
    public static final int DISMISS_DIALOG = 78;


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SHOW_DIALOG, DISMISS_DIALOG})
    public @interface LoadingDialogStatus {
    }

    public static final int SUCCESS = 79;
    public static final int FAIL = 80;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SUCCESS, FAIL})
    public @interface ConnectResult {
    }

    protected ConnectCallback connectCallback;

    public BaseConnect(Context context) {
        this.context = context;
        gson = new Gson();
        zLoadingDialog = new ZLoadingDialog(context);
        client = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .build();
    }


    //------------------

    /**
     * 確認網路功能可用
     */
    protected boolean isNetWorkable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }


    //----------------

    /***
     *  顯示/隱藏 loading dialog
     * @param status
     */
    protected void displayLoadingDialog(@LoadingDialogStatus int status) {
        Message message = Message.obtain();
        message.what = status;
        handler.sendMessage(message);
    }

    //----------------

    /***
     * 傳遞 連線結果  成功
     */
    protected void success( @Nullable  Object object,int statusCode) {
        Message message = Message.obtain();
        message.what = SUCCESS;
        message.obj = object;
        message.arg1 = statusCode;
        handler.sendMessage(message);
    }

    //----------------

    /***
     * 傳遞 連線結果 失敗
     */
    protected void fail( ) {
        Message message = Message.obtain();
        message.what = FAIL;
        handler.sendMessage(message);
    }

    //-----------------

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                //show loading dialog
                case SHOW_DIALOG:
                    zLoadingDialog.show();
                    break;
                //dismiss loading dialog
                case DISMISS_DIALOG:
                    zLoadingDialog.dismiss();
                    break;
                //連線成功
                case SUCCESS:
                    connectCallback.onSuccess((String) msg.obj,msg.arg1);
                    break;
                //連線失敗
                case FAIL:
                    connectCallback.onFailed();
                    break;
            }

        }
    };

    //----------------
}
