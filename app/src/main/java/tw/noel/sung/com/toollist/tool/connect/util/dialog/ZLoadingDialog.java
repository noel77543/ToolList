package tw.noel.sung.com.toollist.tool.connect.util.dialog;

import android.app.Dialog;
/**
 * Created by noel on 2018/6/12.
 */
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.tool.connect.util.views.ZLoadingView;


public class ZLoadingDialog extends Dialog {

    private LinearLayout layout;
    private ZLoadingView zLoadingView;

    public ZLoadingDialog(Context context) {
        super(context);
        layout = new LinearLayout(context);
        zLoadingView = new ZLoadingView(context);
        layout.addView(zLoadingView);
        setContentView(layout);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

}
