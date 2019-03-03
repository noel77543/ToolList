package tw.noel.sung.com.toollist.tool.connect.util.implement;

public interface ConnectCallback {
    void onSuccess(String response, int code);
    void onFailed();
}
