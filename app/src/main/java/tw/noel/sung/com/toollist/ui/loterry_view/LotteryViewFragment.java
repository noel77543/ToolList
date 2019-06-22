package tw.noel.sung.com.toollist.ui.loterry_view;


import android.graphics.BitmapFactory;
import android.util.Log;

import butterknife.BindView;
/**
 * Created by noel on 2019/3/24.
 */
import tw.noel.sung.com.toollist.BasePageFragment;
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.ui.loterry_view.util.LotteryView;
import tw.noel.sung.com.toollist.ui.loterry_view.util.implement.OnScratchListener;


public class LotteryViewFragment extends BasePageFragment implements OnScratchListener {
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
        lotteryView.setScratchPercent(10);
        lotteryView.setScratchSize(50);
        lotteryView.setOnScratchListener(this);
    }


    @Override
    public void OnScratching() {
        Log.e("OnScratching", "OnScratching");
    }

    @Override
    public void OnScratchFinish() {
        Log.e("OnScratchFinish", "OnScratchFinish");
    }
}
