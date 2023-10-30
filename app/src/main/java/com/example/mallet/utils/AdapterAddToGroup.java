package com.example.mallet.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.mallet.R;

import java.util.List;

public class AdapterAddToGroup extends BaseAdapter {
    private final Context context;
    private final List<String> usernames;
    private final List<Boolean> checkedList;

    public AdapterAddToGroup(Context context, List<String> usernames, List<Boolean> checkedList) {
        this.context = context;
        this.usernames = usernames;
        this.checkedList = checkedList;
    }

    @Override
    public int getCount() {
        return usernames.size();
    }

    @Override
    public String getItem(int position) {
        return usernames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.model_add_member_to_group, parent, false);
        }

        TextView usernameTv = convertView.findViewById(R.id.modelAddMemberToGroup_usernameTv);
        CheckBox usernameCb = convertView.findViewById(R.id.addMemberToGroupCb);

        String username = getItem(position);
        boolean isChecked = checkedList.get(position);

        usernameTv.setText(username);
        usernameCb.setChecked(isChecked);

        return convertView;
    }
}
