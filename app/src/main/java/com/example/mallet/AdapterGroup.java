package com.example.mallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class AdapterGroup extends BaseAdapter {

    private Context context;
    private List<ModelGroup> groupList;

    public AdapterGroup(Context context, List<ModelGroup> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupList.get(position);
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

        ModelGroup group = groupList.get(position);

        TextView groupNameTv = itemView.findViewById(R.id.group_model_name_tv);

        groupNameTv.setText(group.getGroupName());

        return itemView;
    }
}
