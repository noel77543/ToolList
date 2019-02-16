package tw.noel.sung.com.toollist.ui.simple_pie_view;

import butterknife.BindView;
import tw.noel.sung.com.toollist.base.BasePageFragment;
/**
 * Created by noel on 2019/2/16.
 */
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.ui.simple_pie_view.util.SimplePieView;

public class SimplePieViewFragment extends BasePageFragment {

    @BindView(R.id.simple_pie_view)
    SimplePieView simplePieView;

    @Override
    protected int getContentView() {
        return R.layout.fragment_simple_pie;
    }

    @Override
    protected void init() {
        simplePieView.setProgress(83.7f);
    }
}
