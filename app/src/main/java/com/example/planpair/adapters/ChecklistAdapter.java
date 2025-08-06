package com.example.planpair.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.planpair.R;

import java.util.HashMap;
import java.util.List;

public class ChecklistAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listGroupTitles;
    private HashMap<String, List<String>> listChildItems;
    private SharedPreferences prefs;

    public ChecklistAdapter(Context context, List<String> listGroupTitles,
                            HashMap<String, List<String>> listChildItems,
                            SharedPreferences prefs) {
        this.context = context;
        this.listGroupTitles = listGroupTitles;
        this.listChildItems = listChildItems;
        this.prefs = prefs;
    }

    @Override
    public int getGroupCount() {
        return listGroupTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listChildItems.get(listGroupTitles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listGroupTitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listChildItems.get(listGroupTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_checklist_group, parent, false);
        }

        TextView groupTitle = convertView.findViewById(R.id.groupTitle);
        ImageView arrowIcon = convertView.findViewById(R.id.img_arrow);

        groupTitle.setText(title);

        // Rotate arrow icon based on expanded state
        arrowIcon.setRotation(isExpanded ? 180f : 0f);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String task = (String) getChild(groupPosition, childPosition);
        String key = listGroupTitles.get(groupPosition) + "_" + childPosition;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_checklist_task, parent, false);
        }

        CheckBox checkBox = convertView.findViewById(R.id.taskCheckbox);
        checkBox.setText(task);
        checkBox.setChecked(prefs.getBoolean(key, false));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(key, isChecked).apply();
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
