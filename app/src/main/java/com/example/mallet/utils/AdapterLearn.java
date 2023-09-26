package com.example.mallet.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallet.R;

public class AdapterLearn extends RecyclerView.Adapter<AdapterLearn.ViewHolder> {

    private static final int LAYOUT_MODEL_MULTIPLE = R.layout.model_multiple_choice;
    private static final int LAYOUT_MODEL_WRITTEN = R.layout.model_written;

    // Add boolean flags to control the visibility of ModelMultipleChoice and ModelWritten
    private boolean isMultipleChoiceEnabled;
    private boolean isWrittenEnabled;

    // Constructor to set the initial state of isMultipleChoiceEnabled and isWrittenEnabled
    public AdapterLearn(boolean isMultipleChoiceEnabled, boolean isWrittenEnabled) {
        this.isMultipleChoiceEnabled = isMultipleChoiceEnabled;
        this.isWrittenEnabled = isWrittenEnabled;
    }

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
        // Calculate the layout resource ID based on the position and the state of flags
        if (isMultipleChoiceEnabled && isWrittenEnabled) {
            // If both switches are checked, alternate between the two layouts (5 of each)
            return (position % 2 == 0) ? LAYOUT_MODEL_MULTIPLE : LAYOUT_MODEL_WRITTEN;
        } else if (isMultipleChoiceEnabled) {
            // If only multipleChoiceMs is checked, use LAYOUT_MODEL_MULTIPLE for all
            return LAYOUT_MODEL_MULTIPLE;
        } else if (isWrittenEnabled) {
            // If only writtenMs is checked, use LAYOUT_MODEL_WRITTEN for all
            return LAYOUT_MODEL_WRITTEN;
        }
        return super.getItemViewType(position);
    }

    // Method to update the state of isMultipleChoiceEnabled
    public void setMultipleChoiceEnabled(boolean isEnabled) {
        this.isMultipleChoiceEnabled = isEnabled;
        notifyDataSetChanged();
    }

    // Method to update the state of isWrittenEnabled
    public void setWrittenEnabled(boolean isEnabled) {
        this.isWrittenEnabled = isEnabled;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        // Return the number of layouts you want to display (up to a maximum of 10)
        if (isMultipleChoiceEnabled && isWrittenEnabled) {
            // If both switches are checked, display 5 of each type
            return 10;
        } else {
            // If only one switch is checked or none are checked, display 10 of the checked type
            return (isMultipleChoiceEnabled || isWrittenEnabled) ? 10 : 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
