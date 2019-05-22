package tw.noel.sung.com.toollist.ui.rocker_view;

import android.util.Log;
import android.widget.TextView;

import butterknife.BindView;
import tw.noel.sung.com.toollist.BasePageFragment;
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.ui.rocker_view.util.RockerView;
import tw.noel.sung.com.toollist.ui.rocker_view.util.implement.OnSwipeListener;

public class RockerViewFragment extends BasePageFragment implements OnSwipeListener {

    @BindView(R.id.rocker_view)
    RockerView rockerView;
    @BindView(R.id.text_view)
    TextView textView;

    @Override
    protected int getContentView() {
        return R.layout.fragment_rocker_view;
    }

    @Override
    protected void init() {
        rockerView.setOnSwipeListener(this);
    }

    //---------

    @Override
    public void onStartSwipe() {

    }

    @Override
    public void onSwiping(int event, double angle) {
        textView.setText(angle + "");
        Log.e("event = ", "" + event);
    }

    @Override
    public void onSwiped() {

    }
}
