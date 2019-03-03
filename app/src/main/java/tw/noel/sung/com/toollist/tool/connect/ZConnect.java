package tw.noel.sung.com.toollist.tool.connect;

import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import tw.noel.sung.com.toollist.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import tw.noel.sung.com.toollist.tool.connect.util.base.BaseConnect;
import tw.noel.sung.com.toollist.tool.connect.util.implement.ConnectCallback;

public class ZConnect extends BaseConnect implements Callback {

    public ZConnect(Context context) {
        super(context);
    }

    //---------------

    /***
     *  http get
     *  無header
     */
    public void get(String apiURL, final ConnectCallback connectCallback) {
        if (!isNetWorkable()) {
            Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
            return;
        }
        this.connectCallback = connectCallback;
        displayLoadingDialog(SHOW_DIALOG);
        request = new Request.Builder()
                .url(apiURL)
                .get()
                .build();
        client.newCall(request).enqueue(this);
    }

    //---------------

    /***
     *  http get
     *  有header
     */
    public void get(String apiURL, Map<String, String> headers, final ConnectCallback connectCallback) {

        if (!isNetWorkable()) {
            Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
            return;
        }
        this.connectCallback = connectCallback;
        displayLoadingDialog(SHOW_DIALOG);

        Request.Builder builder = new Request.Builder()
                .url(apiURL)
                .get();

        for (String key : headers.keySet()) {
            builder.addHeader(key, headers.get(key));
        }

        request = builder.build();
        client.newCall(request).enqueue(this);
    }

    //-----------------

    /***
     *  http post
     *  有header
     *  有params
     */
    public void post(String apiURL, Map<String, String> headers, Map<String, String> params, final ConnectCallback connectCallback) {
        if (!isNetWorkable()) {
            Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
            return;
        }
        this.connectCallback = connectCallback;
        displayLoadingDialog(SHOW_DIALOG);

        Request.Builder builder = new Request.Builder()
                .url(apiURL);

        for (String key : headers.keySet()) {
            builder.addHeader(key, headers.get(key));
        }

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (String key : params.keySet()) {
            formBodyBuilder.add(key, params.get(key));
        }

        requestBody = formBodyBuilder.build();
        request = builder.post(requestBody).build();
        client.newCall(request).enqueue(this);
    }


    //-----------------

    /***
     *  http post
     *  無header
     *  有params
     */
    public void post(String apiURL, Map<String, String> params, final ConnectCallback connectCallback) {
        if (!isNetWorkable()) {
            Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
            return;
        }
        this.connectCallback = connectCallback;

        displayLoadingDialog(SHOW_DIALOG);


        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (String key : params.keySet()) {
            formBodyBuilder.add(key, params.get(key));
        }
        requestBody = formBodyBuilder.build();

        request = new Request.Builder()
                .url(apiURL).post(requestBody).build();

        client.newCall(request).enqueue(this);
    }

    //-----------------

    /***
     *  http post
     *  有header
     *  有 json request
     */
    public void post(String apiURL, Map<String, String> headers, Object requestModel, final ConnectCallback connectCallback) {
        if (!isNetWorkable()) {
            Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
            return;
        }
        this.connectCallback = connectCallback;

        displayLoadingDialog(SHOW_DIALOG);

        Request.Builder builder = new Request.Builder()
                .url(apiURL);

        for (String key : headers.keySet()) {
            builder.addHeader(key, headers.get(key));
        }

        requestBody = RequestBody.create(MEDIA_TYPE_JSON, gson.toJson(requestModel));

        request = builder.post(requestBody).build();
        client.newCall(request).enqueue(this);
    }


    //-----------------

    /***
     *  http post
     *  無header
     *  有 json request
     */
    public void post(String apiURL, Object requestModel, final ConnectCallback connectCallback) {
        if (!isNetWorkable()) {
            Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
            return;
        }
        this.connectCallback = connectCallback;

        displayLoadingDialog(SHOW_DIALOG);

        requestBody = RequestBody.create(MEDIA_TYPE_JSON, gson.toJson(requestModel));

        request = new Request.Builder()
                .url(apiURL)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(this);
    }

    //-----------------------

    /***
     *  http post
     *  有header
     *  有params
     *  post file
     *  如果不需params其他參數 則欄位帶入null即可
     */
    public void post(String apiURL, @Nullable Map<String, String> params, String fileKey, String fileName, File file, @uploadFileType String fileType, final ConnectCallback connectCallback) {
        if (!isNetWorkable()) {
            Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
            return;
        }
        this.connectCallback = connectCallback;

        displayLoadingDialog(SHOW_DIALOG);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(fileKey, fileName, RequestBody.create(MediaType.parse(fileType), file));

        if (params != null) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key));
            }
        }

        requestBody = builder.build();
        request = new Request.Builder()
                .url(apiURL).post(requestBody).build();

        client.newCall(request).enqueue(this);
    }

    //----------------------

    /***
     *  http post
     *  有header
     *  有json request
     *  post file
     */
    public void post(String apiURL, Object requestModel, String fileKey, String fileName, File file, @uploadFileType String fileType, final ConnectCallback connectCallback) {
        if (!isNetWorkable()) {
            Toast.makeText(context, context.getString(R.string.z_connect_net_work_not_work), Toast.LENGTH_SHORT).show();
            return;
        }
        this.connectCallback = connectCallback;

        displayLoadingDialog(SHOW_DIALOG);

        requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(fileKey, fileName, RequestBody.create(MediaType.parse(fileType), file))
                .addPart(RequestBody.create(MEDIA_TYPE_JSON, gson.toJson(requestModel)))
                .build();

        request = new Request.Builder()
                .url(apiURL).post(requestBody).build();

        client.newCall(request).enqueue(this);
    }

    //-----------------

    /***
     * 連線失敗
     * @param call
     * @param e
     */
    @Override
    public void onFailure(Call call, IOException e) {
        displayLoadingDialog(DISMISS_DIALOG);
        fail();
    }

    //----------

    /***
     * 連線成功
     * @param call
     * @param response
     */
    @Override
    public void onResponse(Call call, Response response) {
        displayLoadingDialog(DISMISS_DIALOG);

        try {
            String jsonString = response.body().string();
            success(jsonString, response.code());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
