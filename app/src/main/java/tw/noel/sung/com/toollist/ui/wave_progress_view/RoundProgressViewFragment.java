package tw.noel.sung.com.toollist.ui.wave_progress_view;

import butterknife.BindView;
import tw.noel.sung.com.toollist.BasePageFragment;
/**
 * Created by noel on 2019/2/24.
 */
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.ui.wave_progress_view.util.RoundProgressView;

public class RoundProgressViewFragment extends BasePageFragment{

    @BindView(R.id.wave_progress_view)
    RoundProgressView roundProgressView;

    @Override
    protected int getContentView() {
        return R.layout.fragment_wave_progress_view;
    }

    @Override
    protected void init() {

        roundProgressView.setProgress(833.61f,1000);
    }
}
