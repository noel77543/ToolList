package tw.noel.sung.com.toollist.adapter;

import android.content.Context;
/**
 * Created by noel on 2019/2/6.
 */
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tw.noel.sung.com.toollist.R;

public class MainExpandableListViewAdapter extends BaseExpandableListAdapter {
    private Context context;
    private ArrayList<String> groups;
    private ArrayList<ArrayList<String>> children;
    private LayoutInflater inflater;

    public MainExpandableListViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        groups = new ArrayList<>();
        children = new ArrayList<>();
    }

    //--------------

    public void setData(ArrayList<String> groups, ArrayList<ArrayList<String>> children) {
        this.groups = groups;
        this.children = children;
        notifyDataSetChanged();
    }

    //-------------

    @Override
    public int getGroupCount() {
        return groups.size();
    }
    //-------------

    @Override
    public int getChildrenCount(int groupPosition) {
        return children.get(groupPosition).size();
    }
    //-------------

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }
    //-------------

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return children.get(groupPosition).get(childPosition);
    }
    //-------------

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    //-------------

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    //-------------

    @Override
    public boolean hasStableIds() {
        return false;
    }
    //-------------

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        HeaderViewHolder headerViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_main_header, null);
            headerViewHolder = new HeaderViewHolder(convertView);
            convertView.setTag(headerViewHolder);
        } else {
            headerViewHolder = (HeaderViewHolder) convertView.getTag();
        }

        headerViewHolder.imageView.setBackgroundResource(isExpanded ? R.drawable.ic_open : R.drawable.ic_close);
        headerViewHolder.textView.setText(groups.get(groupPosition));
        return convertView;
    }
    //-------------

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHeader childViewHeader;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_main_child, null);
            childViewHeader = new ChildViewHeader(convertView);
            convertView.setTag(childViewHeader);
        } else {
            childViewHeader = (ChildViewHeader) convertView.getTag();
        }

        childViewHeader.textView.setText(children.get(groupPosition).get(childPosition));
        return convertView;
    }
    //-------------

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    //-------------

    class HeaderViewHolder {
        @BindView(R.id.text_view)
        TextView textView;
        @BindView(R.id.image_view)
        ImageView imageView;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //-------------

    class ChildViewHeader {
        @BindView(R.id.text_view)
        TextView textView;

        ChildViewHeader(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
