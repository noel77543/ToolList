package tw.noel.sung.com.toollist.ui.link_view;

import android.widget.Toast;

import butterknife.BindView;
import tw.noel.sung.com.library_ui_linkview.link_view.LinkView;
import tw.noel.sung.com.library_ui_linkview.link_view.implement.OnDrawLineFinishedListener;
import tw.noel.sung.com.toollist.BasePageFragment;
/**
 * Created by noel on 2019/2/16.
 */
import tw.noel.sung.com.toollist.R;

public class LinkViewFragment extends BasePageFragment implements OnDrawLineFinishedListener {
    @BindView(R.id.link_view)
    LinkView linkView;
    //-------------

    @Override
    protected int getContentView() {
        return R.layout.fragment_link_view;
    }
    //-------------

    @Override
    protected void init() {
        linkView.setOnDrawLineFinishedListener(this);
    }

    //-------------

    /***
     *  當畫線完畢
     * @param password
     */
    @Override
    public void onDrawLineFinished(String password) {
        Toast.makeText(uiActivity, password, Toast.LENGTH_SHORT).show();
    }
}