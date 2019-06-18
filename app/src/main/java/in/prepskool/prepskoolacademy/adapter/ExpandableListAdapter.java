package in.prepskool.prepskoolacademy.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import in.prepskool.prepskoolacademy.R;
import in.prepskool.prepskoolacademy.model.NavigationMenu;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<NavigationMenu> listHeaderName;
    private LinkedHashMap<String, ArrayList<NavigationMenu>> mapGroupAndChild;

    public ExpandableListAdapter(Context context, ArrayList<NavigationMenu> listHeaderName, LinkedHashMap<String, ArrayList<NavigationMenu>> mapGroupAndChild) {
        this.context = context;
        this.listHeaderName = listHeaderName;
        this.mapGroupAndChild = mapGroupAndChild;
    }

    @Override
    public NavigationMenu getChild(int groupPosition, int childPosititon) {
        return mapGroupAndChild.get(listHeaderName.get(groupPosition).getTitle()).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildViewHolder childViewHolder;
        final NavigationMenu child = getChild(groupPosition, childPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_group_child, parent, false);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        }

        else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        childViewHolder.txtListChild.setText(child.getTitle());
        childViewHolder.imgSubMenuIcon.setImageResource(child.getId());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if (mapGroupAndChild.get(getGroup(groupPosition).getTitle()) == null)
            return 0;
        else
            return mapGroupAndChild.get(getGroup(groupPosition).getTitle()).size();
    }

    @Override
    public NavigationMenu getGroup(int groupPosition) {
        return this.listHeaderName.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listHeaderName.size();

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupViewHolder viewHolder;
        final NavigationMenu header = getGroup(groupPosition);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_group_header, parent, false);
            viewHolder = new GroupViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (GroupViewHolder) convertView.getTag();
        }

        viewHolder.lblListHeader.setTypeface(null, Typeface.BOLD);
        viewHolder.lblListHeader.setText(header.getTitle());
        viewHolder.imgHeaderIcon.setImageResource(header.getId());
        if (getChildrenCount(groupPosition) == 0)
            viewHolder.imgDropUpDown.setVisibility(View.GONE);
        else
            viewHolder.imgDropUpDown.setVisibility(View.VISIBLE);

        if (isExpanded) {
            int rotationAngle = 0;
            rotationAngle = rotationAngle == 0 ? 180 : 0;
            viewHolder.imgDropUpDown.animate().rotation(rotationAngle).setDuration(300).start();
        }
        else {
            int rotationAngle = 180;
            rotationAngle = rotationAngle == 0 ? 180 : 0;
            viewHolder.imgDropUpDown.animate().rotation(rotationAngle).setDuration(300).start();
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupViewHolder {

        TextView lblListHeader;
        ImageView imgHeaderIcon;
        ImageView imgDropUpDown;

        GroupViewHolder(View view) {

            lblListHeader = view.findViewById(R.id.lblListHeader);
            imgHeaderIcon = view.findViewById(R.id.img_menu_icon);
            imgDropUpDown = view.findViewById(R.id.img_drop_up_down);
        }
    }

    private class ChildViewHolder {

        TextView txtListChild;
        ImageView imgSubMenuIcon;

        ChildViewHolder(View view) {
            txtListChild = view.findViewById(R.id.lblListItem);
            imgSubMenuIcon =  view.findViewById(R.id.img_submenu_icon);
        }
    }
}

