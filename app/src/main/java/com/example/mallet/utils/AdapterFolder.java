package com.example.mallet.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallet.R;

import java.util.List;

public class AdapterFolder extends RecyclerView.Adapter<AdapterFolder.ViewHolder> {

    private final Context context;
    private final List<ModelFolder> folderList;
    private final OnFolderClickListener folderClickListener;

    // Constructor to initialize the adapter with data and click listener
    public AdapterFolder(Context context, List<ModelFolder> folderList, OnFolderClickListener folderClickListener) {
        this.context = context;
        this.folderList = folderList;
        this.folderClickListener = folderClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the folder_item.xml layout for each item view
        View itemView = LayoutInflater.from(context).inflate(R.layout.model_folder, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the folder data at the current position
        ModelFolder folder = folderList.get(position);

        // Bind folder data to the views within the ViewHolder
        holder.folderOwnerTV.setText(folder.getFolderCreator());
        holder.folderNameTV.setText(folder.getFolderName());

        // Set a click listener for the group item
        holder.itemView.setOnClickListener(view -> {
            if (folderClickListener != null) {
                folderClickListener.onFolderClick(folder);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Return the number of items in the folder list
        return folderList.size();
    }

    // ViewHolder class that holds references to the views within each item view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView folderOwnerTV;
        final TextView folderNameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the TextViews with their corresponding views from the layout
            folderOwnerTV = itemView.findViewById(R.id.folder_model_creator_tv);
            folderNameTV = itemView.findViewById(R.id.folder_model_name_tv);
        }
    }

    // Interface for handling folder click events
    public interface OnFolderClickListener {
        void onFolderClick(ModelFolder folder);
    }
}