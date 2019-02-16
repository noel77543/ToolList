package tw.noel.sung.com.toollist.ui.flower_button;


import butterknife.BindView;
import tw.noel.sung.com.toollist.R;
/**
 * Created by noel on 2019/2/16.
 */
import tw.noel.sung.com.toollist.base.BasePageFragment;
import tw.noel.sung.com.toollist.ui.flower_button.util.FlowerButton;


public class FlowerButtonFragment extends BasePageFragment implements FlowerButton.OnMainButtonClickListener, FlowerButton.OnChildButtonClickListener {
    @BindView(R.id.flower_button)
    FlowerButton flowerButton;

    @Override
    protected int getContentView() {
        return R.layout.fragment_flower_button;
    }

    //--------------

    @Override
    protected void init() {
        flowerButton.setChildCount(8);
        flowerButton.setOnMainButtonClickListener(this);
        flowerButton.setOnChildButtonClickListener(this);
    }
    //--------------

    @Override
    public void onMainButtonCLicked() {

    }
    //--------------

    @Override
    public void onChildButtonCLicked(int position) {

    }
}