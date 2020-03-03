package tw.noel.sung.com.toollist.tool.password_window.util.views;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.tool.password_window.util.CustomPasswordTransformationMethod;


public class PasswordEditText extends AppCompatEditText implements View.OnTouchListener {
    public static final int _STRING_LENGTH = 1;
    private final int TEXT_SIZE = 18;
    private Context context;

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }


    private void init() {
        setTextColor(context.getResources().getColor(R.color.colorAccent));
        setGravity(Gravity.CENTER);
        setTextSize(TEXT_SIZE);
        setBackground(null);
        setPadding(0, 0, 0, 0);
        setSingleLine(true);
        setMaxLines(1);
        setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_NUMBER);
        setCursorVisible(false);
        setHint("●");
        setHintTextColor(context.getResources().getColor(android.R.color.darker_gray));
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(_STRING_LENGTH)});
        setTransformationMethod(new CustomPasswordTransformationMethod());
        setOnTouchListener(this);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }
}
