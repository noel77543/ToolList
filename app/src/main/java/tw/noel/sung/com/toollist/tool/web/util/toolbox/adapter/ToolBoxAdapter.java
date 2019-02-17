package tw.noel.sung.com.toollist.tool.web.util.toolbox.adapter;

import android.content.Context;
/**
 * Created by noel on 2018/6/19.
 */
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.noel.sung.com.toollist.R;



public class ToolBoxAdapter extends RecyclerView.Adapter<ToolBoxAdapter.ViewHolder> {

    private OnItemClickListener mOnItemClickListener;
    private List<String> dataArrayList;

    public ToolBoxAdapter(Context context) {
        dataArrayList = new ArrayList<>();
    }

    //-----------

    /***
     *  in put data
     *  */
    public void setData(List<String> dataArrayList) {
        this.dataArrayList = dataArrayList;
        notifyDataSetChanged();
    }

    //---------------
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_toolbox, null);
        return new ViewHolder(view);
    }
    //------------------

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(dataArrayList.get(position));
    }
    //------------------

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    //------------------

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.text_view)
        TextView textView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnItemClickListener.onItemClicked(view, getLayoutPosition());
        }
    }
    //------------------

    public interface OnItemClickListener {
        void onItemClicked(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

}
