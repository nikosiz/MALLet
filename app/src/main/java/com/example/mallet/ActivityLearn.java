package com.example.mallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mallet.databinding.ActivityLearnBinding;
import com.example.mallet.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ActivityLearn extends AppCompatActivity {

    private ActivityLearnBinding binding;
    ModelLearningSet learningSet;

    // In ActivityLearn, read the flashcards and pass them to FragmentLearn
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String fragmentClassName = getIntent().getStringExtra("fragment_class");

        if (fragmentClassName != null) {
            try {
                // Load the specified fragment
                Fragment fragment = (Fragment) Class.forName(fragmentClassName).newInstance();

                // Pass the flashcards to the fragment
                Bundle args = new Bundle();
                ArrayList<ModelFlashcard> flashcards = getIntent().getParcelableArrayListExtra("learningSetFlashcards");
                args.putParcelableArrayList("learningSetFlashcards", flashcards);
                fragment.setArguments(args);

                // TODO: Delete these two lines when data sharing is done well
                LinearLayout dataCheckLl = binding.learnDataLl;
                dataCheckLl.setVisibility(View.GONE);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.learn_mainLl, fragment)
                        .commit();
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            // No fragment specified, load a layout (for example, activity_learn.xml)
            setContentView(R.layout.activity_learn); // Replace with your layout resource ID
        }

        learningSet = getIntent().getParcelableExtra("learningSet");
        displayData(Utils.createFlashcardList(learningSet), binding.learnSetTermsLl, getLayoutInflater());

        getLearningSetData();
    }


    private void getLearningSetData() {
        Intent intent = getIntent();
        if (intent != null) {
            ModelLearningSet learningSet = intent.getParcelableExtra("learningSet");
            if (learningSet != null) {
                String setName = learningSet.getName();
                String setDescription = learningSet.getDescription();
                String setCreator = learningSet.getCreator();
                String numberOfTerms = String.valueOf(learningSet.getNumberOfTerms());

                List<ModelFlashcard> flashcards = learningSet.getTerms();

                /*Bundle bundle = new Bundle();
                bundle.putString("setName", setName);
                bundle.putString("setCreator", setCreator);
                bundle.putString("numberOfTerms", numberOfTerms);
                bundle.putString("setDescription", setDescription);

                // Pass the bundle as arguments to your Fragment
                FragmentFlashcards fragment = new FragmentFlashcards();
                fragment.setArguments(bundle);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flashcardsCsv, fragment)
                        .commit();*/

                TextView setNameTv = binding.learnSetNameTv;
                TextView setCreatorTv = binding.learnSetCreatorTv;
                TextView setDescriptionTv = binding.learnSetDescriptionTv;
                TextView setTermsTv = binding.learnSetNumberOfTermsTv;


                if (setName != null) {
                    setNameTv.setText(setName);
                }

                if (setCreator != null) {
                    setCreatorTv.setText(setCreator);
                }

                if (setDescription != null) {
                    Utils.showItem(setDescriptionTv);
                    setDescriptionTv.setText(setDescription);
                }

                setTermsTv.setText(numberOfTerms + " terms");
            }
        }
        //System.out.println("Data received from ActivityViewLearningSet: "+learningSet.getName());
    }

    private void displayData
            (List<ModelFlashcard> flashcards, LinearLayout linearLayout, LayoutInflater inflater) {
        linearLayout.removeAllViews();

        for (ModelFlashcard flashcard : flashcards) {
            View flashcardItemView = inflater.inflate(R.layout.model_flashcard, linearLayout, false);

            TextView flashcardTermTv = flashcardItemView.findViewById(R.id.flashcard_termTv);
            flashcardTermTv.setText(flashcard.getTerm());

            TextView flashcardDefinitionTv = flashcardItemView.findViewById(R.id.flashcard_definitionTv);
            flashcardDefinitionTv.setText(flashcard.getDefinition());

            TextView flashcardTranslationTv = flashcardItemView.findViewById(R.id.flashcard_translationTv);
            flashcardTranslationTv.setText(flashcard.getTranslation());

            linearLayout.addView(flashcardItemView);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ActivityViewLearningSet.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
