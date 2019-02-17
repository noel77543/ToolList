package tw.noel.sung.com.toollist.ui.link_view;

import butterknife.BindView;
import tw.noel.sung.com.toollist.BasePageFragment;
/**
 * Created by noel on 2019/2/16.
 */
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.ui.link_view.util.LinkView;
import tw.noel.sung.com.toollist.ui.link_view.util.implement.OnDrawLineFinishedListener;

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

    }
}