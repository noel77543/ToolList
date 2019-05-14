package tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.cover;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import tw.noel.sung.com.toollist.R;

public class CoverActionBar extends RelativeLayout {

    public static int TEXT_VIEW_PHOTO_ID;

    private TextView textViewTitle;
    private TextView textViewPhoto;
    private Context context;
    private final int TEXT_SIZE = 18;

    //---------
    public CoverActionBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    //--------

    private void init() {
        initTitle();
        initPhoto();
        setGravity(Gravity.CENTER_VERTICAL);
        addView(textViewTitle);
        addView(textViewPhoto);
    }

    private void initPhoto() {
        TEXT_VIEW_PHOTO_ID = View.generateViewId();
        textViewPhoto = new TextView(context);
        textViewPhoto.setId(TEXT_VIEW_PHOTO_ID);
        textViewPhoto.setTextSize(TEXT_SIZE);
        textViewPhoto.setTextColor(Color.WHITE);
        textViewPhoto.setText(context.getString(R.string.qr_code_photo));
        textViewPhoto.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_END);
        textViewPhoto.setLayoutParams(params);
    }


    //--------


    private void initTitle() {
        textViewTitle = new TextView(context);
        textViewTitle.setTextSize(TEXT_SIZE);
        textViewTitle.setTextColor(Color.WHITE);
        textViewTitle.setGravity(Gravity.CENTER);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(ALIGN_PARENT_START);
        params.addRule(ALIGN_PARENT_TOP);
        textViewTitle.setLayoutParams(params);
    }

    //--------

    public void setTitle(String title) {
        textViewTitle.setText(title);
    }

    //--------

    public void setOnPhotoButtonClickListener(OnClickListener onClickListener) {
        textViewPhoto.setOnClickListener(onClickListener);
    }

    //--------


    /***
     * 顯示 / 隱藏 右上角『照片』
     */
    public void setPhotoButtonVisibility(int visibility){
        textViewPhoto.setVisibility(visibility);
    }
}
