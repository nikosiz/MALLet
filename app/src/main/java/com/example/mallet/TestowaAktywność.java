package com.example.mallet;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.ActivityLearnBinding;
import com.example.mallet.databinding.DialogConfirmExitBinding;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class TestowaAktywność extends AppCompatActivity {

    private ActivityLearnBinding binding;
    private int clickCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using data binding
        binding = ActivityLearnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the fragment class name from the intent
        String fragmentClassName = getIntent().getStringExtra("fragment_class");

        // Check if a fragment class is specified
        if (fragmentClassName != null) {
            try {
                // Load the specified fragment dynamically
                Fragment fragment = (Fragment) Class.forName(fragmentClassName).newInstance();

                // Pass the flashcards to the fragment via arguments
                Bundle args = new Bundle();
                ArrayList<ModelFlashcard> flashcards = getIntent().getParcelableArrayListExtra("learningSetFlashcards");
                args.putParcelableArrayList("learningSetFlashcards", flashcards);
                fragment.setArguments(args);

                // TODO: Remove these lines when data sharing is handled correctly
                LinearLayout dataCheckLl = binding.learnDataLl;
                dataCheckLl.setVisibility(View.GONE);

                // Replace the fragment container with the loaded fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.learn_mainLl, fragment)
                        .commit();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            // If no fragment class is specified, load a default layout
            setContentView(R.layout.activity_learn); // Replace with your layout resource ID
        }

        // Retrieve and display learning set data
        getAndDisplayLearningSetData();
    }

    private void getAndDisplayLearningSetData() {
        // Retrieve the intent associated with this activity
        Intent intent = getIntent();

        // Check if the intent exists and contains the "learningSet" extra
        if (intent != null && intent.hasExtra("learningSet")) {
            // Get the learning set data from the intent
            ModelLearningSet learningSet = intent.getParcelableExtra("learningSet");

            // Check if the learning set data is not null
            if (learningSet != null) {
                // Retrieve various attributes of the learning set
                String setName = learningSet.getName();
                String setDescription = learningSet.getDescription();
                String setCreator = learningSet.getCreator();
                String nrOfTerms = String.valueOf(learningSet.getNrOfTerms());

                // Print the received data to the console
                System.out.println("Received Data:");
                System.out.println("setName: " + setName);
                System.out.println("setDescription: " + setDescription);
                System.out.println("setCreator: " + setCreator);
                System.out.println("nrOfTerms: " + nrOfTerms);

                // Update TextViews with the retrieved data
                TextView setNameTv = binding.learnSetNameTv;
                TextView setCreatorTv = binding.learnSetCreatorTv;
                TextView setDescriptionTv = binding.learnSetDescriptionTv;
                TextView setTermsTv = binding.learnSetNrOfTermsTv;

                setNameTv.setText(setName);


                if (setCreator != null) {
                    setCreatorTv.setText(setCreator);
                }

                if (setDescription != null) {
                    Utils.showItem(setDescriptionTv);
                    setDescriptionTv.setText(setDescription);
                }

                String nrOfTermsStr = nrOfTerms;
                setTermsTv.setText(getString(R.string.string_terms, nrOfTermsStr));
            }
        }
    }

    public void confirmExitDialog() {
        final Dialog dialog = createDialog(R.layout.dialog_confirm_exit);
        DialogConfirmExitBinding dialogBinding = DialogConfirmExitBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog.getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        dialogBinding.confirmExitCancelTv.setOnClickListener(v -> dialog.dismiss());
        dialogBinding.confirmExitConfirmTv.setOnClickListener(v -> {
            this.finish();
            dialog.dismiss();
        });


    }

    @Override
    public void onBackPressed() {
        confirmExitDialog();
    }

    private Dialog createDialog(int layoutResId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResId);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        return dialog;
    }
}
