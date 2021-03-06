package tw.noel.sung.com.toollist.tool.password_window;

import android.os.Bundle;

import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.tool.password_window.util.PasswordWindow;

public class PasswordWindowActivity extends FragmentActivity implements PasswordWindow.OnInputFinishListener {
    @BindView(R.id.button)
    Button button;
    @BindView(R.id.linear_layout)
    LinearLayout linearLayout;

    private PasswordWindow passwordWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_window);
        ButterKnife.bind(this);
        passwordWindow = new PasswordWindow(this);
        passwordWindow.setOnInputFinishListener(this);
    }

    //--------------

    @OnClick(R.id.button)
    public void onViewClicked() {
        passwordWindow.showAsDropDown(linearLayout, Gravity.CENTER | Gravity.BOTTOM, 0, 0);
    }
    //--------------

    @Override
    public void onInputFinished(String password) {
        Toast.makeText(this, "password = "+password, Toast.LENGTH_SHORT).show();
    }
}
