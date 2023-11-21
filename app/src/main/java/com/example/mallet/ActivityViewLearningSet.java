package com.example.mallet;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.example.mallet.ActivityViewGroup.groupName;
import static com.example.mallet.MALLet.MAX_RETRY_ATTEMPTS;
import static com.example.mallet.MALLet.MIN_FLASHCARDS_FOR_MATCH;
import static com.example.mallet.MALLet.MIN_FLASHCARDS_FOR_TEST;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.agh.api.SetBasicDTO;
import com.agh.api.SetDetailDTO;
import com.agh.api.SetInformationDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.group.boundary.GroupServiceImpl;
import com.example.mallet.backend.client.set.boundary.SetServiceImpl;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.entity.term.ModelFlashcardMapper;
import com.example.mallet.backend.exception.MalletException;
import com.example.mallet.databinding.ActivityViewLearningSetBinding;
import com.example.mallet.databinding.DialogDeleteAreYouSureBinding;
import com.example.mallet.databinding.DialogViewSetToolbarOptionsBinding;
import com.example.mallet.utils.AdapterFlashcardViewPager;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelFlashcard;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @noinspection deprecation
 */
public class ActivityViewLearningSet extends AppCompatActivity {
    private ActivityViewLearningSetBinding binding;
    private UserServiceImpl userService;
    private ModelLearningSet learningSet;
    private long setId;
    private ImageView toolbarBackIv, toolbarOptionsIv;
    private ViewPager2 flashcardsVp2;
    private TextView setNameTv, setDescriptionTv, setCreatorTv, nrOfTermsTv;
    private LinearLayout flashcardsLl, learnLl, testLl, matchLl;
    private ExtendedFloatingActionButton viewSetStudyEfab;

    // toolbar options
    private ImageView toolbarOptionsBackIv;
    private TextView toolbarOptionsEditTv, toolbarOptionsAddToUsersCollectionTv, toolbarOptionsDeleteTv, toolbarOptionsCancelTv;
    private boolean isSetNew, isSetInGroup, canUserEditSet, isUserSet;
    private List<ModelFlashcard> flashcards;

    private SetServiceImpl setService;
    private ProgressBar progressBar;
    private ScrollView viewSetSv;
    private Animation fadeInAnimation;
    private Long groupId;
    private TextView flashcardsTitleTv, learnTitleTv, testTitleTv, matchTitleTv,
            flashcardsSubtitleTv, learnSubtitleTv, testSubtitleTv, matchSubtitleTv;
    private ImageView flashcardsIv, learnIv, testIv, matchIv;
    private TextView allFlashcardsLlTitleTv;
    private GroupServiceImpl groupService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewLearningSetBinding.inflate(getLayoutInflater());

        this.setId = getIntent().getLongExtra("setId", 0L);


        setupContents();
        setContentView(binding.getRoot());

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        };

        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setupContents() {
        learningSet = getIntent().getParcelableExtra("learningSet");
        groupId = getIntent().getLongExtra("groupId", 0L);

        isUserSet = getIntent().getBooleanExtra("isUserSet", true);
        //isSetNew = getIntent().getBooleanExtra("isSetNew", false);
        isSetInGroup = getIntent().getBooleanExtra("isSetInGroup", false);
        canUserEditSet = getIntent().getBooleanExtra("canUserEditSet", false);

        setupToolbar();

        progressBar = binding.viewSetProgressBar;
        viewSetSv = binding.viewSetSv;
        Utils.hideItems(viewSetSv);


        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        String credential = AuthenticationUtils.get(getApplicationContext());
        this.setService = new SetServiceImpl(credential);
        this.userService = new UserServiceImpl(credential);

        this.groupService = new GroupServiceImpl(credential);

        getLearningSetData();


        flashcardsVp2 = binding.viewSetFlashcardVp2;

        setNameTv = binding.viewSetNameTv;
        setNameTv.setText(learningSet.getName());

        setDescriptionTv = binding.viewSetDescriptionTv;
        setDescriptionTv.setText(learningSet.getDescription());

        flashcardsTitleTv = binding.flashcardsLlTitleTv;
        flashcardsSubtitleTv = binding.flashcardsLlSubtTitleTv;
        flashcardsIv = binding.viewSetFlashcardsIv;

        learnTitleTv = binding.learnLlTitleTv;
        learnSubtitleTv = binding.learnLlSubtitleTv;
        learnIv = binding.viewSetLearnIv;

        testTitleTv = binding.testLlTitleTv;
        testSubtitleTv = binding.testLlSubtitleTv;
        testIv = binding.viewSetTestIv;

        matchTitleTv = binding.matchLlTitleTv;
        matchSubtitleTv = binding.matchLlSubtitleTv;
        matchIv = binding.viewSetMatchIv;

        flashcardsLl = binding.viewSetFlashcardsLl;
        flashcardsLl.setOnClickListener(v -> startFlashcards());

        learnLl = binding.viewSetLearnLl;
        learnLl.setOnClickListener(v -> startLearn());

        testLl = binding.viewSetTestLl;
        testLl.setOnClickListener(v -> startTest());

        matchLl = binding.viewSetMatchLl;
        matchLl.setOnClickListener(v -> startMatch());

        allFlashcardsLlTitleTv = binding.viewSetAllFlashcardsLlTitleTv;

        viewSetStudyEfab = binding.viewSetStudyEfab;
        viewSetStudyEfab.setOnClickListener(v -> startFlashcards());

        Utils.hideItems(flashcardsVp2, allFlashcardsLlTitleTv);

        getSet();
    }

    private void getSetWithRestart(int attemptCount) {
        Utils.disableItems(toolbarOptionsIv, flashcardsVp2, flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);
        setService.getSet(setId, new Callback<SetDetailDTO>() {
            @Override
            public void onResponse(Call<SetDetailDTO> call, Response<SetDetailDTO> response) {
                Utils.hideItems(progressBar);
                Utils.enableItems(toolbarOptionsIv, flashcardsVp2);
                Utils.showItems(viewSetSv);

                SetDetailDTO setDetailDTO = ResponseHandler.handleResponse(response);

                Utils.showItems(flashcardsVp2, allFlashcardsLlTitleTv);
                flashcardsVp2.startAnimation(fadeInAnimation);

                flashcards = ModelFlashcardMapper.from(setDetailDTO.terms());
                learningSet.setFlashcards(flashcards);
                learningSet.setId(Math.toIntExact(setDetailDTO.id()));
                TextView addVocabTv = binding.viewSetAddVocabTv;
                addVocabTv.setOnClickListener(v -> editSet());

                if (flashcards == null || flashcards.size() == 0) {
                    Utils.hideItems(flashcardsVp2, allFlashcardsLlTitleTv);
                    Utils.showItems(binding.viewSetNoVocabHereLl);

                    Utils.disableItems(flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);

                    Utils.visuallyDisableTvs(getApplicationContext(), flashcardsTitleTv, flashcardsSubtitleTv,
                            learnTitleTv, learnSubtitleTv,
                            testTitleTv, testSubtitleTv,
                            matchTitleTv, matchSubtitleTv);

                    Utils.visuallyDisableIvs(getApplicationContext(), flashcardsIv, learnIv, testIv, matchIv);

                    testTitleTv.setText(getApplicationContext().getString(R.string.test_available_more_flashcards, String.valueOf(MIN_FLASHCARDS_FOR_TEST)));

                    matchTitleTv.setText(getApplicationContext().getString(R.string.match_available_more_flashcards, String.valueOf(MIN_FLASHCARDS_FOR_MATCH)));
                } else if (flashcards.size() > 0 && flashcards.size() < MIN_FLASHCARDS_FOR_TEST) {
                    Utils.enableItems(flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);

                    if (flashcards.size() < MIN_FLASHCARDS_FOR_MATCH) {
                        Utils.showItems(flashcardsVp2, allFlashcardsLlTitleTv);

                        Utils.disableItems(matchLl);

                        Utils.visuallyDisableTvs(getApplicationContext(), matchTitleTv, matchSubtitleTv);
                        Utils.visuallyDisableIvs(getApplicationContext(), matchIv);

                        matchTitleTv.setText(getApplicationContext().getString(R.string.match_available_more_flashcards, String.valueOf(MIN_FLASHCARDS_FOR_MATCH)));
                        matchTitleTv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        matchSubtitleTv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        matchIv.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorPrimary));
                    }

                    if (flashcards.size() < MIN_FLASHCARDS_FOR_TEST) {
                        Utils.showItems(flashcardsVp2, allFlashcardsLlTitleTv);

                        Utils.disableItems(testLl);

                        Utils.visuallyDisableTvs(getApplicationContext(), testTitleTv, testSubtitleTv);
                        Utils.visuallyDisableIvs(getApplicationContext(), testIv);

                        testTitleTv.setText(getApplicationContext().getString(R.string.test_available_more_flashcards, String.valueOf(MIN_FLASHCARDS_FOR_TEST)));
                        testTitleTv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        testSubtitleTv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                        testIv.setColorFilter(getApplicationContext().getResources().getColor(R.color.colorPrimary));
                    }

                } else if (flashcards.size() >= MIN_FLASHCARDS_FOR_TEST) {
                    Utils.enableItems(flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);
                }


                displayFlashcardsInViewPager(flashcards, flashcardsVp2);

                displayFlashcardsInLinearLayout(flashcards, binding.viewSetAllFlashcardsLl, getLayoutInflater(),true);
                flashcardsLl.startAnimation(fadeInAnimation);

                fetchNextSets(setId, setDetailDTO.nextChunkUri());
            }

            @Override
            public void onFailure(Call<SetDetailDTO> call, Throwable t) {
                if (attemptCount < MAX_RETRY_ATTEMPTS) {
                    // System.out.println(attemptCount);
                    getSetWithRestart(attemptCount + 1);
                } else {
                    Utils.showToast(getApplicationContext(), "Network error");
                    Utils.disableItems(flashcardsLl, learnLl, testLl, matchLl);
                }
            }

        });
    }

    private void fetchNextSets(long setId,
                               String nextChunkUri) {

        if (Objects.isNull(nextChunkUri)) {
        } else {
            Uri uri = Uri.parse(nextChunkUri);
            String startPosition = uri.getQueryParameter("startPosition");
            String limit = uri.getQueryParameter("limit");
            if (startPosition != null && limit != null) {
                fetchNextSets(setId,Long.parseLong(startPosition), Long.parseLong(limit));
            }
        }
    }

    private void fetchNextSets(long setId,long startPosition,
                           long limit) {
        setService.getSet(setId, startPosition, limit, new Callback<SetDetailDTO>() {
            @Override
            public void onResponse(Call<SetDetailDTO> call, Response<SetDetailDTO> response) {
                SetDetailDTO setDetailDTO = ResponseHandler.handleResponse(response);
                flashcards.addAll(ModelFlashcardMapper.from(setDetailDTO.terms()));

                displayFlashcardsInViewPager(flashcards, flashcardsVp2);
                displayFlashcardsInLinearLayout(flashcards, binding.viewSetAllFlashcardsLl, getLayoutInflater(),false);
                fetchNextSets(setId, setDetailDTO.nextChunkUri());
            }

            @Override
            public void onFailure(Call<SetDetailDTO> call, Throwable t) {
                Utils.showToast(getApplicationContext(), "Network error");
            }
        });
    }

    private void getSet() {
        getSetWithRestart(0);
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.viewSetToolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        toolbarBackIv = binding.viewSetToolbarBackIv;
        toolbarOptionsIv = binding.viewSetToolbarOptionsIv;

        binding.viewSetToolbarBackIv.setOnClickListener(v -> finish());

        binding.viewSetToolbarOptionsIv.setOnClickListener(v -> viewSetOptionsDialog());
    }

    private void startFlashcards() {
        Intent intent = new Intent(this, ActivityLearn.class);

        intent.putExtra("fragment_class", FragmentFlashcards.class.getName()); // Pass the class name
        intent.putExtra("learningSet", ModelLearningSet.builder().terms(flashcards).build());

        startActivity(intent);
    }

    private void startLearn() {
        Intent intent = new Intent(this, ActivityLearn.class);

        intent.putExtra("fragment_class", FragmentLearn.class.getName()); // Pass the class name
        intent.putExtra("learningSet", ModelLearningSet.builder().terms(flashcards).build());

        startActivity(intent);
    }

    private void startTest() {
        Intent intent = new Intent(this, ActivityLearn.class);

        intent.putExtra("fragment_class", FragmentTest.class.getName()); // Pass the class name
        intent.putExtra("learningSet", ModelLearningSet.builder().terms(flashcards).build());

        startActivity(intent);
    }

    private void startMatch() {
        Intent intent = new Intent(this, ActivityLearn.class);

        intent.putExtra("fragment_class", FragmentMatch.class.getName()); // Pass the class name
        intent.putExtra("learningSet", ModelLearningSet.builder().terms(flashcards).build());

        startActivity(intent);

    }

    private void viewSetOptionsDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_view_set_toolbar_options, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MATCH_PARENT), Gravity.BOTTOM);
        DialogViewSetToolbarOptionsBinding dialogBinding = DialogViewSetToolbarOptionsBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        toolbarOptionsBackIv = dialogBinding.viewSetOptionsBackIv;
        toolbarOptionsBackIv.setOnClickListener(v -> dialog.dismiss());

        toolbarOptionsEditTv = dialogBinding.viewSetOptionsEditTv;
        toolbarOptionsAddToUsersCollectionTv = dialogBinding.viewSetOptionsAddToCollectionTv;
        toolbarOptionsDeleteTv = dialogBinding.viewSetOptionsDeleteSetTv;


        if (!isSetInGroup) {
            if (isUserSet) {
                Utils.showItems(toolbarOptionsEditTv, toolbarOptionsDeleteTv);
                Utils.hideItems(toolbarOptionsAddToUsersCollectionTv);
                Utils.enableItems(toolbarOptionsEditTv);
                Utils.disableItems(toolbarOptionsAddToUsersCollectionTv);

                toolbarOptionsEditTv.setOnClickListener(v -> {
                    dialog.dismiss();
                    editSet();
                });

                toolbarOptionsDeleteTv.setText(getString(R.string.delete_set));
                toolbarOptionsDeleteTv.setOnClickListener(v -> {
                    dialog.dismiss();
                    deleteSetDialog();
                });
            } else if (!isUserSet) {
                Utils.showItems(toolbarOptionsAddToUsersCollectionTv);
                Utils.enableItems(toolbarOptionsAddToUsersCollectionTv);
                Utils.hideItems(toolbarOptionsEditTv, toolbarOptionsDeleteTv);
                Utils.disableItems(toolbarOptionsEditTv, toolbarOptionsDeleteTv);

                toolbarOptionsAddToUsersCollectionTv.setOnClickListener(v -> {
                    dialog.dismiss();
                    addSetToUsersCollection();
                });
            }
        } else if (isSetInGroup) {
            if (canUserEditSet) {
                Utils.showItems(toolbarOptionsEditTv, toolbarOptionsAddToUsersCollectionTv);
                Utils.enableItems(toolbarOptionsEditTv, toolbarOptionsAddToUsersCollectionTv);

                toolbarOptionsEditTv.setOnClickListener(v -> {
                    dialog.dismiss();
                    editSetInGroup();
                });

                toolbarOptionsAddToUsersCollectionTv.setOnClickListener(v -> {
                    dialog.dismiss();
                    addSetToUsersCollection();
                });
            } else if (!canUserEditSet) {
                Utils.showItems(toolbarOptionsAddToUsersCollectionTv);
                Utils.hideItems(toolbarOptionsEditTv, toolbarOptionsDeleteTv);
                Utils.enableItems(toolbarOptionsAddToUsersCollectionTv);
                Utils.disableItems(toolbarOptionsEditTv, toolbarOptionsDeleteTv);

                toolbarOptionsAddToUsersCollectionTv.setOnClickListener(v -> {
                    dialog.dismiss();
                    addSetToUsersCollection();
                });
            }
        }

        toolbarOptionsCancelTv = dialogBinding.viewSetOptionsCancelTv;
        toolbarOptionsCancelTv.setOnClickListener(v -> dialog.dismiss());
    }

    private void editSetInGroup() {
        isSetNew = false;

        if (canUserEditSet) {
            Intent intent = new Intent(this, ActivityEditLearningSet.class);

            intent.putExtra("learningSet", learningSet);

            intent.putExtra("groupName", groupName);
            intent.putExtra("groupId", groupId);

            intent.putExtra("isSetNew", isSetNew);
            intent.putExtra("isSetInGroup", isSetInGroup);
            intent.putExtra("canUserEditSet", canUserEditSet);

            startActivity(intent);

            this.finish();
        } else {
            Utils.showToast(this, getString(R.string.no_permissions_to_edit));
        }
    }

    private void editSet() {
        Intent intent = new Intent(this, ActivityEditLearningSet.class);

        intent.putExtra("learningSet", learningSet);
        intent.putExtra("isSetNew", isSetNew);

        startActivity(intent);

        finish();
    }

    private boolean isUserSet(long id) {
        id = setId;
        // TODO zapytać starego Michała

        return false;
    }

    // If learningSet != userSet:
    private void addSetToUsersCollectionWithRestart(int attemptCount) {
        Utils.disableItems(toolbarOptionsBackIv, toolbarOptionsEditTv,
                toolbarOptionsAddToUsersCollectionTv, toolbarOptionsDeleteTv,
                toolbarOptionsCancelTv, toolbarBackIv, toolbarOptionsIv, flashcardsVp2,
                flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);
        userService.addSetToUserSets(setId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                try {
                    ResponseHandler.handleResponse(response);
                    Utils.showToast(getApplicationContext(), "Added to collection");

                    Utils.enableItems(toolbarOptionsBackIv, toolbarOptionsEditTv,
                            toolbarOptionsAddToUsersCollectionTv, toolbarOptionsDeleteTv,
                            toolbarOptionsCancelTv, toolbarBackIv, toolbarOptionsIv, flashcardsVp2,
                            flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);

                } catch (MalletException e) {
                    Utils.showToast(getApplicationContext(), e.getMessage());
                    Utils.enableItems(toolbarOptionsBackIv, toolbarOptionsEditTv,
                            toolbarOptionsAddToUsersCollectionTv, toolbarOptionsDeleteTv,
                            toolbarOptionsCancelTv, toolbarBackIv, toolbarOptionsIv, flashcardsVp2,
                            flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (attemptCount < MAX_RETRY_ATTEMPTS) {
                    // System.out.println(attemptCount);
                    addSetToUsersCollectionWithRestart(attemptCount + 1);
                } else {
                    Utils.showToast(getApplicationContext(), "Network error");
                    Utils.enableItems(toolbarOptionsBackIv, toolbarOptionsEditTv,
                            toolbarOptionsAddToUsersCollectionTv, toolbarOptionsDeleteTv,
                            toolbarOptionsCancelTv, toolbarBackIv, toolbarOptionsIv, flashcardsVp2,
                            flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);

                }
            }
        });
    }

    private void addSetToUsersCollection() {
        addSetToUsersCollectionWithRestart(0);
    }

    public void deleteSetDialog() {
        Dialog dialog = Utils.createDialog(this, R.layout.dialog_delete_are_you_sure, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogDeleteAreYouSureBinding dialogBinding = DialogDeleteAreYouSureBinding.inflate(LayoutInflater.from(this));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView cancelTv = dialogBinding.deleteCancelTv;
        TextView confirmTv = dialogBinding.deleteConfirmTv;

        cancelTv.setOnClickListener(v -> dialog.dismiss());
        confirmTv.setOnClickListener(v -> deleteSet());
    }


    private void deleteSetWithRestart(int attemptCount) {
        Utils.disableItems(toolbarOptionsBackIv, toolbarOptionsEditTv,
                toolbarOptionsAddToUsersCollectionTv, toolbarOptionsDeleteTv,
                toolbarOptionsCancelTv, toolbarBackIv, toolbarOptionsIv, flashcardsVp2,
                flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);
        userService.deleteUserSet(setId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // TODO stary Michał mocno śpi
                try {
                    ResponseHandler.handleResponse(response);
                    Utils.showToast(getApplicationContext(), learningSet.getName() + " was deleted");
                    Utils.enableItems(toolbarOptionsBackIv, toolbarOptionsEditTv,
                            toolbarOptionsAddToUsersCollectionTv, toolbarOptionsDeleteTv,
                            toolbarOptionsCancelTv, toolbarBackIv, toolbarOptionsIv, flashcardsVp2,
                            flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);

                    closeActivity();
                } catch (MalletException e) {
                    Utils.showToast(getApplicationContext(), e.getMessage());
                    Utils.enableItems(toolbarOptionsBackIv, toolbarOptionsEditTv,
                            toolbarOptionsAddToUsersCollectionTv, toolbarOptionsDeleteTv,
                            toolbarOptionsCancelTv, toolbarBackIv, toolbarOptionsIv, flashcardsVp2,
                            flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (attemptCount < MAX_RETRY_ATTEMPTS) {
                    // System.out.println(attemptCount);
                    addSetToUsersCollectionWithRestart(attemptCount + 1);
                } else {
                    Utils.showToast(getApplicationContext(), "Network error");
                    Utils.enableItems(toolbarOptionsBackIv, toolbarOptionsEditTv,
                            toolbarOptionsAddToUsersCollectionTv, toolbarOptionsDeleteTv,
                            toolbarOptionsCancelTv, toolbarBackIv, toolbarOptionsIv, flashcardsVp2,
                            flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);

                }
            }
        });
    }

    private void deleteSet() {
        deleteSetWithRestart(0);
    }

    private void deleteSetFromGroupWithRestart(int attemptCount) {
        Utils.disableItems(toolbarOptionsBackIv, toolbarOptionsEditTv,
                toolbarOptionsAddToUsersCollectionTv, toolbarOptionsDeleteTv,
                toolbarOptionsCancelTv, toolbarBackIv, toolbarOptionsIv, flashcardsVp2,
                flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);
        groupService.removeSet(groupId, setId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                try {
                    ResponseHandler.handleResponse(response);
                    Utils.showToast(getApplicationContext(), learningSet.getName() + " was deleted");
                    Utils.enableItems(toolbarOptionsBackIv, toolbarOptionsEditTv,
                            toolbarOptionsAddToUsersCollectionTv, toolbarOptionsDeleteTv,
                            toolbarOptionsCancelTv, toolbarBackIv, toolbarOptionsIv, flashcardsVp2,
                            flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);

                    startViewGroupActivity();

                    closeActivity();
                } catch (MalletException e) {
                    Utils.showToast(getApplicationContext(), e.getMessage());
                    Utils.enableItems(toolbarOptionsBackIv, toolbarOptionsEditTv,
                            toolbarOptionsAddToUsersCollectionTv, toolbarOptionsDeleteTv,
                            toolbarOptionsCancelTv, toolbarBackIv, toolbarOptionsIv, flashcardsVp2,
                            flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (attemptCount < MAX_RETRY_ATTEMPTS) {
                    // System.out.println(attemptCount);
                    addSetToUsersCollectionWithRestart(attemptCount + 1);
                } else {
                    Utils.showToast(getApplicationContext(), "Network error");
                    Utils.enableItems(toolbarOptionsBackIv, toolbarOptionsEditTv,
                            toolbarOptionsAddToUsersCollectionTv, toolbarOptionsDeleteTv,
                            toolbarOptionsCancelTv, toolbarBackIv, toolbarOptionsIv, flashcardsVp2,
                            flashcardsLl, learnLl, testLl, matchLl, viewSetStudyEfab);

                }
            }
        });
    }

    private void deleteSetFromGroup() {
        deleteSetFromGroupWithRestart(0);
    }


    private void startViewGroupActivity() {
        finish();

        Intent intent = new Intent(getApplicationContext(), ActivityViewGroup.class);

        intent.putExtra("groupId", groupId);

        startActivity(intent);
    }

    private void closeActivity() {
        this.finish();
    }

    private void displayFlashcardsInViewPager(List<ModelFlashcard> flashcards, ViewPager2
            viewPager) {
        if (flashcards != null && flashcards.size() > 0) {
            AdapterFlashcardViewPager adapter = new AdapterFlashcardViewPager(flashcards);
            viewPager.setAdapter(adapter);
            viewPager.setPageTransformer(Utils::applySwipeTransformer);
        }
    }

    private void displayFlashcardsInLinearLayout
            (List<ModelFlashcard> flashcards, LinearLayout linearLayout, LayoutInflater inflater, boolean addNew) {
        if(addNew) {
            linearLayout.removeAllViews();
        }
        if (flashcards != null && flashcards.size() > 0) {
            for (ModelFlashcard flashcard : flashcards) {
                View flashcardItemView = inflater.inflate(R.layout.model_flashcard, linearLayout, false);
                Utils.setMargins(this, flashcardItemView, 0, 0, 0, 10);

                int paddingInDp = 20;
                float scale = getResources().getDisplayMetrics().density;
                int paddingInPixels = (int) (paddingInDp * scale + 0.5f);

                LinearLayout flashcardLL = flashcardItemView.findViewById(R.id.flashcard_mainLl);

                flashcardLL.setPadding(paddingInPixels, paddingInPixels, paddingInPixels, paddingInPixels);

                TextView flashcardTermTv = flashcardItemView.findViewById(R.id.flashcard_termTv);
                flashcardTermTv.setVisibility(View.VISIBLE);
                flashcardTermTv.setText(flashcard.getTerm());
                flashcardTermTv.setGravity(Gravity.START);
                flashcardTermTv.setTextSize(20.0f);
                Utils.makeTextPhat(this, flashcardTermTv);

                if (Objects.nonNull(flashcard.getDefinition())) {
                    View aboveDefinition = flashcardItemView.findViewById(R.id.flashcard_aboveDefinitionView);
                    Utils.setViewLayoutParams(aboveDefinition, MATCH_PARENT, 5);
                    Utils.showItems(aboveDefinition);

                    TextView flashcardDefinitionTv = flashcardItemView.findViewById(R.id.flashcard_definitionTv);
                    flashcardDefinitionTv.setVisibility(View.VISIBLE);
                    flashcardDefinitionTv.setText(flashcard.getDefinition());
                    int textColorResId = R.color.colorPrimaryDark;
                    Utils.setTextColor(this, flashcardDefinitionTv, textColorResId);
                    flashcardDefinitionTv.setGravity(Gravity.START);
                    flashcardDefinitionTv.setTextSize(15.0f);
                }

                View aboveTranslation = flashcardItemView.findViewById(R.id.flashcard_aboveTranslationView);
                Utils.setViewLayoutParams(aboveTranslation, MATCH_PARENT, 5);
                Utils.showItems(aboveTranslation);

                TextView flashcardTranslationTv = flashcardItemView.findViewById(R.id.flashcard_translationTv);
                flashcardTranslationTv.setVisibility(View.VISIBLE);
                flashcardTranslationTv.setText(flashcard.getTranslation());
                flashcardTranslationTv.setGravity(Gravity.START);
                flashcardTranslationTv.setTextSize(15.0f);

                linearLayout.addView(flashcardItemView);
            }
        }
    }

    private void getLearningSetDataWithRestart(int attemptCount) {
        Utils.disableItems(viewSetSv);
        setService.getBasicSet(Collections.singleton(setId), new Callback<SetBasicDTO>() {
            @Override
            public void onResponse(Call<SetBasicDTO> call, Response<SetBasicDTO> response) {
                Utils.hideItems(progressBar);
                Utils.enableItems(viewSetSv);

                SetBasicDTO setBasicDTO = ResponseHandler.handleResponse(response);
                SetInformationDTO setInformationDTO = setBasicDTO.sets().get(0);

                TextView setNameTv = binding.viewSetNameTv;
                TextView setCreatorTv = binding.viewSetCreatorTv;
                TextView setDescriptionTv = binding.viewSetDescriptionTv;
                TextView setTermsTv = binding.viewSetNrOfTermsTv;

                setNameTv.setText(setInformationDTO.name());
                learningSet.setId(Math.toIntExact(setInformationDTO.id()));
                if(Objects.isNull(setInformationDTO.creator())){
                    setCreatorTv.setText("");
                }else{
                    setCreatorTv.setText(setInformationDTO.creator().name());
                }

                Optional.ofNullable(setInformationDTO.description())
                        .ifPresent((setDescription -> {
                            Utils.showItems(setDescriptionTv);
                            setDescriptionTv.setText(setDescription);
                        }));

                setTermsTv.setText(getString(R.string.string_terms, String.valueOf(setInformationDTO.numberOfTerms())));
            }

            @Override
            public void onFailure(Call<SetBasicDTO> call, Throwable t) {
                if (attemptCount < MAX_RETRY_ATTEMPTS) {
                    // System.out.println(attemptCount);
                    getLearningSetDataWithRestart(attemptCount + 1);
                } else {
                    Utils.showToast(getApplicationContext(), "Network error");
                    Utils.hideItems(progressBar);
                    Utils.enableItems(viewSetSv);

                }
            }
        });
    }

    private void getLearningSetData() {
        getLearningSetDataWithRestart(0);
    }
}