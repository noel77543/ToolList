package tw.noel.sung.com.toollist.tool.password_window.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;

import tw.noel.sung.com.toollist.R;


import butterknife.BindView;
import butterknife.OnClick;
import tw.noel.sung.com.toollist.tool.password_window.util.views.PasswordLayout;

public class PasswordWindow extends BaseWindow implements PopupWindow.OnDismissListener {

    @BindView(R.id.button_close)
    Button buttonClose;
    @BindView(R.id.button_1)
    Button button1;
    @BindView(R.id.button_2)
    Button button2;
    @BindView(R.id.button_3)
    Button button3;
    @BindView(R.id.button_4)
    Button button4;
    @BindView(R.id.button_5)
    Button button5;
    @BindView(R.id.button_6)
    Button button6;
    @BindView(R.id.button_7)
    Button button7;
    @BindView(R.id.button_8)
    Button button8;
    @BindView(R.id.button_9)
    Button button9;
    @BindView(R.id.button_0)
    Button button0;
    @BindView(R.id.image_button_delete)
    ImageButton buttonDelete;
    @BindView(R.id.password_layout)
    PasswordLayout passwordLayout;


    private final int CODE_LENGTH = 6;
    private int index = 0;
    private OnInputFinishListener onInputFinishListener;

    public PasswordWindow(Context context) {
        super(context);
    }

    //--------

    @Override
    protected int getContentViewId() {
        return R.layout.window_password;
    }
    //--------

    @Override
    protected void init() {
        setOutsideTouchable(false);
        setFocusable(true);

        setHeight(windowHeight / 2);
        setWidth(windowWidth);
        setAnimationStyle(R.style.animation_window);
        setOnDismissListener(this);
        passwordLayout.setCodeLength(windowWidth / (CODE_LENGTH + 5), CODE_LENGTH);
    }

    //--------
    @OnClick({R.id.button_close, R.id.image_button_delete})
    public void onViewActionClicked(View view) {
        switch (view.getId()) {
            case R.id.button_close:
                dismiss();
                break;
            case R.id.image_button_delete:
                if (index > 0) {
                    index--;
                    passwordLayout.clearTarget(index);
                }
                break;
        }
    }


    //--------

    @OnClick({R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9, R.id.button_0})
    public void onViewNumberClicked(View view) {
        String text = ((Button)view).getText().toString();

        passwordLayout.setSingleText(index, text);
        if (index < CODE_LENGTH) {
            index++;
        }


        if (index > CODE_LENGTH - 1) {
            if (onInputFinishListener != null) {
                onInputFinishListener.onInputFinished(passwordLayout.getAllText());
            }
            reset();
        }
    }

    //------

    /***
     * 清空回初始
     */
    private void reset(){
        index = 0;
        passwordLayout.clearAll();
    }
    //------

    @Override
    public void showAsDropDown(View parent, int gravity, int x, int y) {
        super.showAsDropDown(parent, gravity,x, y);
        setWindowBackgroundAlpha((Activity) context, 0.6f);
    }

    //------

    @Override
    public void onDismiss() {
        setWindowBackgroundAlpha((Activity) context, 1f);
        reset();
    }


    //----------


    public interface OnInputFinishListener {
        void onInputFinished(String password);
    }

    public void setOnInputFinishListener(OnInputFinishListener onInputFinishListener) {
        this.onInputFinishListener = onInputFinishListener;
    }
}
