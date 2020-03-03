package tw.noel.sung.com.toollist.tool.password_window.util.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;


public class PasswordLayout extends LinearLayout implements TextWatcher, View.OnFocusChangeListener {
    private Context context;
    private PasswordEditText[] passwordEditTexts;
    private StringBuilder stringBuilder;
    private int codeLength;
    private PasswordEditText currentPasswordEditText;
    private OnInputFinishListener onInputFinishListener;
    private int index = 0;


    public PasswordLayout(Context context) {
        this(context, null);
    }

    public PasswordLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    //--------

    private void init() {
        stringBuilder = new StringBuilder();
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    //---------

    /***
     * 指定加入多少edit text 用以輸入密碼
     */
    public void setCodeLength(int editTextWidth, int codeLength) {
        this.codeLength = codeLength;
        int margin = (int) (editTextWidth * 0.2 / 2);
        passwordEditTexts = new PasswordEditText[codeLength];

        for (int i = 0; i < codeLength; i++) {
            PasswordEditText passwordEditText = new PasswordEditText(context);
            passwordEditTexts[i] = passwordEditText;

            LayoutParams params = new LayoutParams(editTextWidth, editTextWidth);
            params.setMargins(margin, 0, margin, 0);
            passwordEditTexts[i].addTextChangedListener(this);
            passwordEditTexts[i].setOnFocusChangeListener(this);
            passwordEditTexts[i].setLayoutParams(params);
            //設立索引
            passwordEditTexts[i].setTag(i);
            addView(passwordEditTexts[i]);
        }
    }
    //---------

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }
    //---------

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (currentPasswordEditText != null) {
            //取得索引
            index = (int) currentPasswordEditText.getTag();

            //每當輸入字元後自動focus下一個
            if (currentPasswordEditText.getText().toString().length() > 0) {
                if (index < passwordEditTexts.length - 1) {
                    index += 1;
                    PasswordEditText passwordEditText = passwordEditTexts[index];
                    passwordEditText.requestFocus();

                }
            }
            //每當刪除字元後 自動focus前一個
            else {
                if (index > 0) {
                    index -= 1;
                    PasswordEditText passwordEditText = passwordEditTexts[index];
                    passwordEditText.requestFocus();
                }
            }
        }


        if (isInputFinished()) {

            //當輸入完成所有輸入匡
            if (onInputFinishListener != null) {
                onInputFinishListener.onInputFinished(this);
            }
        }
    }
    //---------

    @Override
    public void afterTextChanged(Editable s) {

    }
    //---------

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            currentPasswordEditText = (PasswordEditText) v;
        }
    }

    //-----------

    public interface OnInputFinishListener {
        void onInputFinished(View view);
    }
    //--------

    public void setOnInputFinishListener(OnInputFinishListener onInputFinishListener) {
        this.onInputFinishListener = onInputFinishListener;
    }


    //---------

    /***
     * 已輸入所有輸入匡
     */
    private boolean isInputFinished() {

        stringBuilder.setLength(0);
        for (int i = 0; i < passwordEditTexts.length; i++) {
            String text = passwordEditTexts[i].getText().toString();
            stringBuilder.append(text);
            if (text.length() < 1) {
                return false;
            }
        }
        String text = stringBuilder.toString();
        return text.length() == codeLength;
    }


    //----------

    public void setSingleText(int index, String text) {
        passwordEditTexts[index].setText(text);
    }

    //---------

    public void focusTarget(int index) {
        if (index < codeLength) {
            passwordEditTexts[index].requestFocus();
        }
    }

    //---------

    public String getAllText() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < passwordEditTexts.length; i++) {
            stringBuilder.append(passwordEditTexts[i].getText().toString());
        }
        return stringBuilder.toString();
    }
    //---------

    /***
     * 清除所有
     */
    public void clearAll() {
        if (passwordEditTexts.length > 0) {
            for (int i = 0; i < passwordEditTexts.length; i++) {
                passwordEditTexts[i].setText("");
            }
            passwordEditTexts[0].requestFocus();
        }
    }

    //---------

    /***
     * 清除單一個
     * @param index
     */
    public void clearTarget(int index) {
        passwordEditTexts[index].setText("");
        if (index > 0) {
            passwordEditTexts[index - 1].requestFocus();
        }
    }

}
