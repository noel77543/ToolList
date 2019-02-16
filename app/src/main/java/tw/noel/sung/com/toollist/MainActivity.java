package tw.noel.sung.com.toollist;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
/**
 * Created by noel on 2019/2/16.
 */
import android.provider.Settings;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import tw.noel.sung.com.toollist.adapter.MainExpandableListViewAdapter;
import tw.noel.sung.com.toollist.tool.qr_code_scan.QRCodeScanActivity;
import tw.noel.sung.com.toollist.ui.UIActivity;


@RuntimePermissions
public class MainActivity extends FragmentActivity implements Runnable, ExpandableListView.OnChildClickListener {

    @BindView(R.id.text_view_title)
    TextView textViewTitle;
    @BindView(R.id.expandable_list_view)
    ExpandableListView expandableListView;

    //計時器
    private Timer timer;
    //計時器任務
    private TimerTask timerTask;
    //重複行為
    private Runnable runnable;
    //第幾個文字
    private int textIndex = 0;
    //文字長度
    private int textLength;
    //最初的文字大小
    private float textSize;
    //行為重複間隔
    private final int DURATION = 200;
    //放大倍率
    private final float TEXT_SIZE = 1.5f;
    private MainExpandableListViewAdapter mainExpandableListViewAdapter;

    @IntDef({PERMISSION_OPEN_QRCODE_SCANNER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PermissionActionType {
    }

    //開啟qr code 掃描器
    public static final int PERMISSION_OPEN_QRCODE_SCANNER = 101;
    private @PermissionActionType
    int permissionActionType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initTitleAnimation();
        initExpandableListView();
    }


    //-------------

    /***
     * 建立 標題動畫
     */
    private void initTitleAnimation() {
        textSize = textViewTitle.getTextSize();
        textLength = textViewTitle.getText().toString().length();
        runnable = this;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(runnable);
            }
        };
        timer.schedule(timerTask, 0, DURATION);
    }

    //--------------

    private void initExpandableListView() {
        mainExpandableListViewAdapter = new MainExpandableListViewAdapter(this);
        expandableListView.setAdapter(mainExpandableListViewAdapter);
        ArrayList<String> groups = new ArrayList<>();
        groups.add(getString(R.string.header_ui));
        groups.add(getString(R.string.header_tool));

        ArrayList<ArrayList<String>> allChildren = new ArrayList<>();


        ArrayList<String> childrenUI = new ArrayList<>();
        childrenUI.add("1. FlowerButton");
        childrenUI.add("2. LinkView");
        childrenUI.add("3. SimplePieView");
        childrenUI.add("4. BlockPieView");


        ArrayList<String> childrenTool = new ArrayList<>();
        childrenTool.add("1. QRCode Scanner");

        allChildren.add(childrenUI);
        allChildren.add(childrenTool);

        mainExpandableListViewAdapter.setData(groups, allChildren);
        expandableListView.setOnChildClickListener(this);
    }
    //--------------

    @Override
    public void run() {
        if (textIndex < textLength) {
            textViewTitle.setText(getSpannedText(textIndex));
            textIndex++;
        } else {
            textIndex = 0;
        }
    }

    //----------------

    /***
     * 部分字串放大
     * @param strIndext
     * @return
     */
    private CharSequence getSpannedText(int strIndext) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textViewTitle.getText().toString());
        builder.setSpan(new RelativeSizeSpan(TEXT_SIZE), strIndext, strIndext + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }


    //-----------------------------

    /**
     * 前往開啟權限
     */
    private void goToSettingPermissions() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(getString(R.string.dialog_message_goto_setting));
        alert.setPositiveButton(getString(R.string.dialog_go), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent settings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                settings.addCategory(Intent.CATEGORY_DEFAULT);
                settings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(settings);
            }
        });
        alert.show();
    }
    //----------------

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {


        //UI
        if (groupPosition == 0) {
            Intent intent = new Intent(this, UIActivity.class);
            final String KEY_TITLE = "title";
            final String KEY_PAGE = "page";

            switch (childPosition) {
                //FlowerButton
                case 0:
                    intent.putExtra(KEY_PAGE, UIActivity.PAGE_FLOWER_BUTTON);
                    break;
                //LinkView
                case 1:
                    intent.putExtra(KEY_PAGE, UIActivity.PAGE_LINK_VIEW);
                    break;
                //SimplePieView
                case 2:
                    intent.putExtra(KEY_PAGE, UIActivity.PAGE_SIMPLE_PIE_VIEW);
                    break;
                //BlockPieView
                case 3:
                    intent.putExtra(KEY_PAGE, UIActivity.PAGE_BLOCK_PIE_VIEW);
                    break;
            }
            intent.putExtra(KEY_TITLE, (String) parent.getExpandableListAdapter().getChild(groupPosition, childPosition));
            startActivity(intent);
        }
        //功能
        else if (groupPosition == 1) {
            switch (childPosition) {
                //QRCode Scanner
                case 0:
                    permissionActionType = PERMISSION_OPEN_QRCODE_SCANNER;
                    MainActivityPermissionsDispatcher.onPermissionAllowedWithPermissionCheck(this);
                    break;
            }
        }
        return false;
    }


    //-----------------

    @NeedsPermission(Manifest.permission.CAMERA)
    void onPermissionAllowed() {
        switch (permissionActionType) {
            //開啟掃描器
            case PERMISSION_OPEN_QRCODE_SCANNER:
                startActivity(new Intent(this, QRCodeScanActivity.class));
                break;
        }
    }

    //-----------------

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
    //-----------------

    @OnShowRationale(Manifest.permission.CAMERA)
    void onShowRationale(final PermissionRequest request) {
        request.proceed();
    }
    //-----------------

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void onPermissionDenied() {
        Toast.makeText(this, getString(R.string.toast_permission_refuse), Toast.LENGTH_SHORT).show();
    }
    //-----------------

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void onNeverAskAgain() {
        goToSettingPermissions();
    }
}
