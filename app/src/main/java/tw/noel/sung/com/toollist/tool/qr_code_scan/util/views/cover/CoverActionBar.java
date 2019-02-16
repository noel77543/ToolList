package tw.noel.sung.com.toollist.tool.qr_code_scan.util.views.cover;

import android.content.Context;
/**
 * Created by noel on 2019/2/16.
 */
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CoverActionBar extends LinearLayout {

    private TextView textView;
    private Context context;
    private final int TEXT_SIZE = 20;

    //---------
    public CoverActionBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    //--------

    private void init() {
        initTitle();
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        addView(textView);

    }
    //--------

    private void initTitle() {
        textView = new TextView(context);
        textView.setTextSize(TEXT_SIZE);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setBackground(new ColorDrawable(Color.TRANSPARENT));
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        textView.setLayoutParams(params);
        textView.setPadding(8, 8, 8, 8);

    }
    //--------

    public void setTitle(String title) {
        textView.setText(title);
    }
}
