package com.example.mallet.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallet.ModelGroup;
import com.example.mallet.R;

import java.util.List;

public class AdapterGroup extends RecyclerView.Adapter<AdapterGroup.ViewHolder> {

    private final Context context;
    private final List<ModelGroup> groupList;
    private final OnGroupClickListener groupClickListener;

    public AdapterGroup(Context context, List<ModelGroup> groupList, OnGroupClickListener groupClickListener) {
        this.context = context;
        this.groupList = groupList;
        this.groupClickListener = groupClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.model_group, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelGroup group = groupList.get(position);

        holder.groupNameTV.setText(group.getGroupName());

        // Set a click listener for the group item
        holder.itemView.setOnClickListener(view -> {
            if (groupClickListener != null) {
                groupClickListener.onGroupClick(group);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView groupNameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            groupNameTV = itemView.findViewById(R.id.group_model_name_tv);
        }
    }

    public interface OnGroupClickListener {
        void onGroupClick(ModelGroup group);
    }
}
