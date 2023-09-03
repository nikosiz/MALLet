package com.example.mallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterGroup extends BaseAdapter {

    private final Context context;
    private final List<String> groupNameList;

    public AdapterGroup(Context context, List<String> groupNameList) {
        this.context = context;
        this.groupNameList = groupNameList;
    }

    @Override
    public int getCount() {
        return groupNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            itemView = inflater.inflate(R.layout.model_group, parent, false);
        }

        String groupName = groupNameList.get(position);

        TextView groupNameTv = itemView.findViewById(R.id.group_model_name_tv);

        groupNameTv.setText(groupName);

        return itemView;
    }
}
