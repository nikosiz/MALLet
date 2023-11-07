package com.example.mallet.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        holder.groupNameTv.setText(group.getGroupName());
        holder.groupNrOfSetsTv.setText(group.getNrOfSets() + " sets");
        holder.groupNrOfMembersTv.setText(group.getNrOfMembers() + " members");

        holder.itemView.setOnClickListener(view -> {
            if (groupClickListener != null) {
                groupClickListener.onGroupClick(group);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the nr of items in the group list
        return groupList.size();
    }

    // ViewHolder class that holds references to the views within each item view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView groupNameTv,groupNrOfSetsTv,groupNrOfMembersTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextView with its corresponding view from the layout
            groupNameTv = itemView.findViewById(R.id.group_nameTv);
            groupNrOfSetsTv = itemView.findViewById(R.id.group_nrOfSetsTv);
            groupNrOfMembersTv = itemView.findViewById(R.id.group_nrOfMembersTv);
        }
    }

    // Interface for handling group click events
    public interface OnGroupClickListener {
        void onGroupClick(ModelGroup group);
    }
}