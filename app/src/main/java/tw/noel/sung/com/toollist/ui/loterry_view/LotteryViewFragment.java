package tw.noel.sung.com.toollist.ui.loterry_view;



import android.graphics.BitmapFactory;

import butterknife.BindView;
/**
 * Created by noel on 2019/3/24.
 */
import tw.noel.sung.com.toollist.BasePageFragment;
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.ui.loterry_view.util.LotteryView;


public class LotteryViewFragment extends BasePageFragment {
    @BindView(R.id.lottery_view)
    LotteryView lotteryView;


    @Override
    protected int getContentView() {
        return R.layout.fragment_lottery_view;
    }

    @Override
    protected void init() {
        lotteryView.setScratchImage(BitmapFactory.decodeResource(getResources(), R.drawable.img_scratch));
        lotteryView.setRewardImage(BitmapFactory.decodeResource(getResources(), R.drawable.img_reward));
    }


}
