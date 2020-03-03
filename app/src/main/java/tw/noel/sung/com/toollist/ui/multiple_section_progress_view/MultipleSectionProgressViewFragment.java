package tw.noel.sung.com.toollist.ui.multiple_section_progress_view;



import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import tw.noel.sung.com.toollist.BasePageFragment;
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.ui.multiple_section_progress_view.adapter.ItemAdapter;
import tw.noel.sung.com.toollist.ui.multiple_section_progress_view.model.ModelItem;

public class MultipleSectionProgressViewFragment extends BasePageFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ItemAdapter itemAdapter;
    @Override
    protected int getContentView() {
        return R.layout.fragment_multiple_section_progress_view;
    }

    @Override
    protected void init() {
        itemAdapter = new ItemAdapter(uiActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(uiActivity,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(itemAdapter);

        itemAdapter.setData(getData());
    }
    //-------------

    private ArrayList<ModelItem> getData() {
        ArrayList<ModelItem> modelItems = new ArrayList<>();
        modelItems.add(new ModelItem(new float[]{500,600},new int[]{android.R.color.holo_red_dark,android.R.color.holo_blue_light},1000,800));
        modelItems.add(new ModelItem(new float[]{500,600,800},new int[]{android.R.color.holo_red_dark,android.R.color.holo_blue_light,android.R.color.holo_green_light},2000,1200));
        modelItems.add(new ModelItem(new float[]{200,400,600,800},new int[]{android.R.color.holo_red_dark,android.R.color.holo_blue_light,android.R.color.holo_green_light,android.R.color.holo_orange_light},3000,2000));
        modelItems.add(new ModelItem(new float[]{20},new int[]{android.R.color.holo_red_dark},100,60));
        modelItems.add(new ModelItem(new float[]{500,600},new int[]{android.R.color.holo_red_dark,android.R.color.holo_blue_light},1000,800));
        modelItems.add(new ModelItem(new float[]{500,600},new int[]{android.R.color.holo_red_dark,android.R.color.holo_blue_light},1000,900));
        modelItems.add(new ModelItem(new float[]{500,600},new int[]{android.R.color.holo_red_dark,android.R.color.holo_blue_light},1000,300));
        modelItems.add(new ModelItem(new float[]{500,600},new int[]{android.R.color.holo_red_dark,android.R.color.holo_blue_light},1000,500));
        modelItems.add(new ModelItem(new float[]{500},new int[]{android.R.color.holo_red_dark},1000,550));
        return modelItems;
    }

}
