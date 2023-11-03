package com.example.mallet.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mallet.R;

public class AdapterTest extends RecyclerView.Adapter<AdapterTest.ViewHolder> {

    private static final int LAYOUT_MODEL_MULTIPLE = R.layout.model_multiple_choice;
    private static final int LAYOUT_MODEL_WRITTEN = R.layout.model_written;
    private static final int LAYOUT_MODEL_TRUE_FALSE = R.layout.model_true_false;
    private static final int LAYOUT_MODEL_MATCH = R.layout.model_match;
    private boolean isMultipleChoiceEnabled, isWrittenEnabled, isTrueFalseEnabled, isMatchEnabled;
    private int nrOfQuestions;

    public AdapterTest(int nrOfQuestions, boolean isMultipleChoiceEnabled, boolean isWrittenEnabled, boolean isTrueFalseEnabled) {
        this.nrOfQuestions = nrOfQuestions;
        this.isMultipleChoiceEnabled = isMultipleChoiceEnabled;
        this.isWrittenEnabled = isWrittenEnabled;
        this.isTrueFalseEnabled = isTrueFalseEnabled;
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
        if (isMultipleChoiceEnabled && isWrittenEnabled && isTrueFalseEnabled && isMatchEnabled) {
            int mod4 = position % 4;
            if (mod4 == 0) {
                return LAYOUT_MODEL_MULTIPLE;
            } else if (mod4 == 1) {
                return LAYOUT_MODEL_WRITTEN;
            } else if (mod4 == 2) {
                return LAYOUT_MODEL_TRUE_FALSE;
            } else {
                return LAYOUT_MODEL_MATCH;
            }
        } else if (isMultipleChoiceEnabled && isWrittenEnabled && isTrueFalseEnabled) {
            return (position % 3 == 0) ? LAYOUT_MODEL_MULTIPLE :
                    (position % 3 == 1) ? LAYOUT_MODEL_WRITTEN :
                            LAYOUT_MODEL_TRUE_FALSE;
        } else if (isMultipleChoiceEnabled && isWrittenEnabled && isMatchEnabled) {
            return (position % 3 == 0) ? LAYOUT_MODEL_MULTIPLE :
                    (position % 3 == 1) ? LAYOUT_MODEL_WRITTEN :
                            LAYOUT_MODEL_MATCH;
        } else if (isMultipleChoiceEnabled && isTrueFalseEnabled && isMatchEnabled) {
            return (position % 3 == 0) ? LAYOUT_MODEL_MULTIPLE :
                    (position % 3 == 1) ? LAYOUT_MODEL_TRUE_FALSE :
                            LAYOUT_MODEL_MATCH;
        } else if (isWrittenEnabled && isTrueFalseEnabled && isMatchEnabled) {
            return (position % 3 == 0) ? LAYOUT_MODEL_WRITTEN :
                    (position % 3 == 1) ? LAYOUT_MODEL_TRUE_FALSE :
                            LAYOUT_MODEL_MATCH;
        } else if (isMultipleChoiceEnabled && isWrittenEnabled) {
            return (position % 2 == 0) ? LAYOUT_MODEL_MULTIPLE :
                    LAYOUT_MODEL_WRITTEN;
        } else if (isMultipleChoiceEnabled && isTrueFalseEnabled) {
            return (position % 2 == 0) ? LAYOUT_MODEL_MULTIPLE :
                    LAYOUT_MODEL_TRUE_FALSE;
        } else if (isMultipleChoiceEnabled && isMatchEnabled) {
            return (position % 2 == 0) ? LAYOUT_MODEL_MULTIPLE :
                    LAYOUT_MODEL_MATCH;
        } else if (isWrittenEnabled && isTrueFalseEnabled) {
            return (position % 2 == 0) ? LAYOUT_MODEL_WRITTEN :
                    LAYOUT_MODEL_TRUE_FALSE;
        } else if (isWrittenEnabled && isMatchEnabled) {
            return (position % 2 == 0) ? LAYOUT_MODEL_WRITTEN :
                    LAYOUT_MODEL_MATCH;
        } else if (isTrueFalseEnabled && isMatchEnabled) {
            return (position % 2 == 0) ? LAYOUT_MODEL_TRUE_FALSE :
                    LAYOUT_MODEL_MATCH;
        } else if (isMultipleChoiceEnabled) {
            return LAYOUT_MODEL_MULTIPLE;
        } else if (isWrittenEnabled) {
            return LAYOUT_MODEL_WRITTEN;
        } else if (isTrueFalseEnabled) {
            return LAYOUT_MODEL_TRUE_FALSE;
        } else if (isMatchEnabled) {
            return LAYOUT_MODEL_MATCH;
        }
        return super.getItemViewType(position);
    }


    // Method to update the state of isMultipleChoiceEnabled
    public void setMultipleChoiceEnabled(boolean isEnabled) {
        this.isMultipleChoiceEnabled = isEnabled;
        notifyDataSetChanged();
    }

    public void setWrittenEnabled(boolean isEnabled) {
        this.isWrittenEnabled = isEnabled;
        notifyDataSetChanged();
    }

    public void setTrueFalseEnabled(boolean isEnabled) {
        this.isTrueFalseEnabled = isEnabled;
        notifyDataSetChanged();
    }

    public void setMatchEnabled(boolean isEnabled) {
        this.isMatchEnabled = isEnabled;
        notifyDataSetChanged();
    }

    public void setNrOfQuestions(int nrOfQuestions) {
        this.nrOfQuestions = nrOfQuestions;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        if (!isMultipleChoiceEnabled && !isWrittenEnabled && !isTrueFalseEnabled && !isMatchEnabled) {
            return 0;
        }

        return nrOfQuestions;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
