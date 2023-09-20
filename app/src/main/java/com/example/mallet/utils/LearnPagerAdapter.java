package com.example.mallet.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallet.R;

public class LearnPagerAdapter extends RecyclerView.Adapter<LearnPagerAdapter.ViewHolder> {

    private static final int LAYOUT_MODEL_MULTIPLE = R.layout.model_multiple_choice;
    private static final int LAYOUT_MODEL_WRITTEN = R.layout.model_written;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // You can perform any custom logic here if needed
    }

    @Override
    public int getItemViewType(int position) {
        // Return the layout resource ID based on the position
        if (position == 0) {
            return LAYOUT_MODEL_MULTIPLE;
        } else {
            return LAYOUT_MODEL_WRITTEN;
        }
    }

    @Override
    public int getItemCount() {
        // Return the number of layouts you want to display
        return 2;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}