package tw.noel.sung.com.toollist.ui.multiple_section_progress_view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.noel.sung.com.toollist.R;
import tw.noel.sung.com.toollist.ui.multiple_section_progress_view.model.ModelItem;
import tw.noel.sung.com.toollist.ui.multiple_section_progress_view.util.MultipleSectionProgressView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private ArrayList<ModelItem> modelItems;
    private Context context;
    private int startColor = R.color.multiple_section_progress_view_start;
    private int endColor = R.color.multiple_section_progress_view_end;


    public ItemAdapter(Context context) {
        this.context = context;
        modelItems = new ArrayList<>();
    }

    //---------

    public void setData(ArrayList<ModelItem> modelItems) {
        this.modelItems = modelItems;
        notifyDataSetChanged();
    }

    //---------
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_multiple_section_progress_view, viewGroup,false));
    }
    //----------

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ModelItem modelItem = modelItems.get(i);
        viewHolder.multipleSectionProgressView
                .setAnimationTime(2000)
                .setValue(modelItem.getMaxValue(), modelItem.getTargetValue())
                .setSections(startColor, endColor, modelItem.getSectionColors(), modelItem.getSections())
                .draw(new AccelerateInterpolator());
    }
    //----------

    @Override
    public int getItemCount() {
        return modelItems.size();
    }

    //----------

   static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.multiple_section_progress_view)
        MultipleSectionProgressView multipleSectionProgressView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
