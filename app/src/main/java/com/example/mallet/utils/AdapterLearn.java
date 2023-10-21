package com.example.mallet.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallet.R;

import java.util.List;

public class AdapterLearn extends RecyclerView.Adapter<AdapterLearn.ViewHolder> {
    private static final int LAYOUT_MODEL_MULTIPLE = R.layout.model_multiple_choice;
    private static final int LAYOUT_MODEL_WRITTEN = R.layout.model_written;
    private boolean isMultipleChoiceEnabled, isWrittenEnabled;
    private final List<ModelWritten> pages;

    public AdapterLearn(boolean isMultipleChoiceEnabled, boolean isWrittenEnabled, List<ModelWritten> pages) {
        this.isMultipleChoiceEnabled = isMultipleChoiceEnabled;
        this.isWrittenEnabled = isWrittenEnabled;
        this.pages = pages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (isWrittenEnabled && position < pages.size()) {
            ModelWritten page = pages.get(position);
            holder.setContentText(page.getQuestion());
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Calculate the layout resource ID based on the position and the state of flags
        if (isMultipleChoiceEnabled && isWrittenEnabled) {
            return (position % 2 == 0) ? LAYOUT_MODEL_MULTIPLE : LAYOUT_MODEL_WRITTEN;
        } else if (isMultipleChoiceEnabled) {
            return LAYOUT_MODEL_MULTIPLE;
        } else if (isWrittenEnabled) {
            return LAYOUT_MODEL_WRITTEN;
        }
        return super.getItemViewType(position);
    }

    public void setMultipleChoiceEnabled(boolean isEnabled) {
        this.isMultipleChoiceEnabled = isEnabled;
        notifyDataSetChanged();
    }

    public void setWrittenEnabled(boolean isEnabled) {
        this.isWrittenEnabled = isEnabled;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (isMultipleChoiceEnabled && isWrittenEnabled) {
            return 10;
        } else {
            return (isMultipleChoiceEnabled || isWrittenEnabled) ? 10 : 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView writtenQuestionTv;

        ViewHolder(View itemView) {
            super(itemView);
            writtenQuestionTv = itemView.findViewById(R.id.written_questionTv);
        }

        void setContentText(String writtenQuestion) {
            try {
                writtenQuestionTv.setText(writtenQuestion);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error with setting question");
            }
        }
    }
}
