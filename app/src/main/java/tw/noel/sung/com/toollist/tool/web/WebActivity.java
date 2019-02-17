package tw.noel.sung.com.toollist.tool.web;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.tool.web.util.CustomWebView;
import tw.noel.sung.com.toollist.tool.web.util.toolbox.ToolBoxPopupWindow;

public class WebActivity extends FragmentActivity implements ToolBoxPopupWindow.OnToolBoxSelectListener{

    @BindView(R.id.image_view_tool)
    ImageView imageViewTool;
    @BindView(R.id.button_back)
    Button buttonBack;
    @BindView(R.id.text_view_title)
    TextView textViewTitle;
    @BindView(R.id.web_view)
    CustomWebView webView;

    private ToolBoxPopupWindow toolBoxPopupWindow;
    private  String _TEST_URL = "https://github.com/noel77543";
    private final String _PAGE = "Like & Share Thx !  ^_^";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        init();
    }



    //-------

    /***
     *  init
     */
    private void init() {
        Intent data = getIntent();


        toolBoxPopupWindow = new ToolBoxPopupWindow(this, ToolBoxPopupWindow.TOOL_BOX_TYPE_WEB);
        toolBoxPopupWindow.setOnToolBoxSelectedListener(this);

        imageViewTool.setImageResource(R.drawable.ic_menu);
        textViewTitle.setText(_PAGE);
        webView.setLoadingMessage(_PAGE);
        webView.open(_TEST_URL);
    }

    //----------

    @OnClick({R.id.button_back, R.id.image_view_tool})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button_back:
                back();

                break;
            case R.id.image_view_tool:
                toolBoxPopupWindow.showAtLocation(imageViewTool, Gravity.TOP | Gravity.END, 0, 0);
                break;
        }

    }

    //---------
    @Override
    public void onBackPressed() {
        back();
    }

    //-----------

    /***
     *  返回
     */
    private void back() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }


    //-----------

    @Override
    public void onToolBoxSelected(View view, int position) {

        switch (position) {
            //重新整理
            case 0:
                webView.open(_TEST_URL);
                break;
            //複製連結
            case 1:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("text label", webView.getUrl());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, getString(R.string.web_tool_toast_copy), Toast.LENGTH_SHORT).show();
                break;
            //分享連結
            case 2:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, _PAGE);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, webView.getUrl());
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.web_tool_box_share)));
                break;
            //以其他應用開啟
            case 3:
                Intent openIntent = new Intent();
                openIntent.setAction(Intent.ACTION_VIEW);
                openIntent.addCategory(Intent.CATEGORY_BROWSABLE);
                if (! _TEST_URL.substring(0, 4).equalsIgnoreCase("http")) {
                    _TEST_URL = "https:" + _TEST_URL;
                }
                openIntent.setData(Uri.parse(_TEST_URL));
                startActivity(openIntent);
                break;
            //關閉網頁
            case 4:
                finish();
                break;
        }
    }
}
