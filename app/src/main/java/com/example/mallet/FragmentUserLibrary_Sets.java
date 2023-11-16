package com.example.mallet;

import static com.example.mallet.MALLet.MAX_RETRY_ATTEMPTS;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.agh.api.SetBasicDTO;
import com.agh.api.SetInformationDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.group.boundary.GroupServiceImpl;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.entity.set.ModelLearningSetMapper;
import com.example.mallet.backend.exception.MalletException;
import com.example.mallet.databinding.DialogDeleteAreYouSureBinding;
import com.example.mallet.databinding.FragmentUserLibrarySetsBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.rxbinding.widget.RxTextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentUserLibrary_Sets extends Fragment {
    private ModelLearningSet learningSet;
    private ActivityMain activityMain;
    private GroupServiceImpl groupService;
    private FragmentUserLibrarySetsBinding binding;
    private UserServiceImpl userService;
    private final List<ModelLearningSet> sets = new ArrayList<>();
    private final List<ModelLearningSet> foundSets = new ArrayList<>();
    private TextInputEditText searchEt;
    private LinearLayout userSetsLl;
    private LayoutInflater inflater;
    private final AtomicBoolean firstTime = new AtomicBoolean(true);
    private ProgressBar progressBar;
    private Animation fadeInAnimation;
    private ScrollView userSetsSv;
    private String nextChunkUri = StringUtils.EMPTY;
    private boolean isNextChunkUriChanged = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ActivityMain) {
            activityMain = (ActivityMain) context;
            fadeInAnimation = AnimationUtils.loadAnimation(requireActivity(), R.anim.fade_in);
        } else {
            // Handle the case where the activity does not implement the method
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String credential = AuthenticationUtils.get(requireActivity());
        this.userService = new UserServiceImpl(credential);
    }

    private ImageView indicatorIv;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserLibrarySetsBinding.inflate(inflater, container, false);
        setupContents(inflater);


        userSetsSv.getViewTreeObserver().addOnScrollChangedListener(() -> {
            View view = userSetsSv.getChildAt(userSetsSv.getChildCount() - 1);

            int diff = (view.getBottom() - (userSetsSv.getHeight() + userSetsSv
                    .getScrollY()));

            if (diff == 0) {
                if (!nextChunkUri.isEmpty() && isNextChunkUriChanged) {
                    getUserLibrarySetList(sets, nextChunkUri);
                    Utils.showItems(indicatorIv);
                }
            } else {
                Utils.hideItems(indicatorIv);
            }
        });

        getUserLibrarySetList(sets, null);
        setupSearchAndFetchSets(0, 15);
        return binding.getRoot();
    }

    private void setupSearchAndFetchSetsWithRestart(long startPosition, long limit, int attemptCount) {
        RxTextView.textChanges(searchEt)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(text -> {
                    if (firstTime.get() || text.length() < 2) {
                        return;
                    }

                    foundSets.clear();

                    if (text.length() == 0) {
                        getUserLibrarySetList(sets, null);
                        return;
                    }
                    userService.getUserSets(startPosition, limit, new Callback<SetBasicDTO>() {
                        @Override
                        public void onResponse(Call<SetBasicDTO> call, Response<SetBasicDTO> response) {
                            userSetsLl.removeAllViews();
                            fetchSetsForSearch(text.toString(), response);
                        }

                        @Override
                        public void onFailure(Call<SetBasicDTO> call, Throwable t) {
                            if (attemptCount < MAX_RETRY_ATTEMPTS) {
                               // System.out.println(attemptCount);
                                // Retry the network call
                                setupSearchAndFetchSetsWithRestart(startPosition, limit, attemptCount + 1);
                            } else {
                                Utils.showToast(getContext(), "Network error");
                            }
                        }
                    });
                });
    }

    private void setupSearchAndFetchSets(long startPosition, long limit) {
        int attemptCount = MAX_RETRY_ATTEMPTS;
        setupSearchAndFetchSetsWithRestart(startPosition, limit, attemptCount);
    }

    private void fetchSetsForSearch(String text, Response<SetBasicDTO> response) {
        SetBasicDTO setBasicDTO = ResponseHandler.handleResponse(response);
        List<SetInformationDTO> collect = setBasicDTO.sets().stream()
                .filter(set -> set.name().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
        List<ModelLearningSet> sets = ModelLearningSetMapper.from(collect);
        foundSets.addAll(sets);
        if (Objects.nonNull(setBasicDTO.nextChunkUri())) {
            Uri uri = Uri.parse(setBasicDTO.nextChunkUri());
            String startPosition = uri.getQueryParameter("startPosition");
            String limit = uri.getQueryParameter("limit");

            if (startPosition != null && limit != null) {
                setupSearchAndFetchSets(Long.parseLong(startPosition), Long.parseLong(limit));
            }
        } else {
            setupSetView(inflater, foundSets, false);
        }
    }

    private void setupContents(LayoutInflater inflater) {
        learningSet = getActivity().getIntent().getParcelableExtra("learningSet");

        indicatorIv = binding.userLibrarySetsIndicatorIv;

        searchEt = binding.userLibrarySetsSearchEt;
        userSetsSv = binding.userLibrarySetsSv;
        userSetsLl = binding.userLibrarySetsAllSetsLl;

        progressBar = binding.userLibrarySetsProgressBar;
        this.inflater = inflater;
    }

    private void getUserLibrarySetList(List<ModelLearningSet> setList,
                                       @Nullable String nextChunkUri) {

        if (Objects.isNull(nextChunkUri)) {
            fetchUserSets(0, 10, false, setList);
        } else {
            Uri uri = Uri.parse(nextChunkUri);
            String startPosition = uri.getQueryParameter("startPosition");
            String limit = uri.getQueryParameter("limit");
            if (startPosition != null && limit != null) {
                fetchUserSets(Long.parseLong(startPosition), Long.parseLong(limit), true, setList);
            }
        }
    }

    private void fetchUserSetsWithRestart(long startPosition,
                                          long limit,
                                          boolean isBottom,
                                          List<ModelLearningSet> setList,
                                          int attemptCount) {
        userService.getUserSets(startPosition, limit, new Callback<SetBasicDTO>() {
            @Override
            public void onResponse(Call<SetBasicDTO> call, Response<SetBasicDTO> response) {
                Utils.hideItems(progressBar);
                SetBasicDTO setBasicDTO = ResponseHandler.handleResponse(response);
                List<ModelLearningSet> modelLearningSets = ModelLearningSetMapper.from(setBasicDTO.sets());
                if (Objects.nonNull(setBasicDTO.nextChunkUri()) && !nextChunkUri.equals(setBasicDTO.nextChunkUri())) {
                    if (!isBottom) {
                        isNextChunkUriChanged = true;
                        nextChunkUri = setBasicDTO.nextChunkUri();
                    } else {
                        List<ModelLearningSet> newSets = modelLearningSets.stream()
                                .filter(newSet -> !setList.contains(newSet))
                                .collect(Collectors.toList());
                        setList.addAll(newSets);

                        setupSetView(inflater, setList, true);
                        return;
                    }
                } else {
                    isNextChunkUriChanged = false;
                }
                List<ModelLearningSet> newSets = modelLearningSets.stream()
                        .filter(newSet -> !setList.contains(newSet))
                        .collect(Collectors.toList());
                setList.addAll(newSets);

                setupSetView(inflater, setList, false);
                firstTime.set(false);
            }

            @Override
            public void onFailure(Call<SetBasicDTO> call, Throwable t) {
                if (attemptCount < MAX_RETRY_ATTEMPTS) {
                   // System.out.println(attemptCount);
                    fetchUserSetsWithRestart(startPosition, limit, isBottom, setList, attemptCount + 1);
                } else {
                    Utils.showToast(requireActivity(), "Network error");
                }
            }
        });
    }

    private void fetchUserSets(long startPosition,
                               long limit,
                               boolean isBottom,
                               List<ModelLearningSet> setList) {
        int attemptCount = MAX_RETRY_ATTEMPTS;
        fetchUserSetsWithRestart(startPosition, limit, isBottom, setList, attemptCount);
    }

    private void setupSetView(@NonNull LayoutInflater inflater,
                              List<ModelLearningSet> userLibraryFoldersList,
                              boolean addSetView) {
        if (!addSetView) {
            userSetsLl.removeAllViews();
        }
        for (ModelLearningSet set : userLibraryFoldersList) {
            View setItemView = inflater.inflate(R.layout.model_learning_set, userSetsLl, false);

            setItemView.setOnClickListener(v -> viewSet(set));

            TextView setNameTv = setItemView.findViewById(R.id.learningSet_nameTv);
            setNameTv.setText(set.getName());

            TextView setNrOfTermsTv = setItemView.findViewById(R.id.learningSet_nrOfTermsTv);
            if (set != null) {
                if (set.getNrOfTerms() == 1) {
                    setNrOfTermsTv.setText(getActivity().getString(R.string.nr_of_terms_singular, String.valueOf(set.getNrOfTerms())));
                } else {
                    setNrOfTermsTv.setText(getActivity().getString(R.string.nr_of_terms_plural, String.valueOf(set.getNrOfTerms())));
                }
            }

            TextView setCreatorTv = setItemView.findViewById(R.id.learningSet_creatorTv);
            setCreatorTv.setText(set.getCreator());

            ImageView deleteIv = setItemView.findViewById(R.id.learningSet_deleteIv);
            deleteIv.setOnClickListener(v -> confirmSetDeletion(set));

            setItemView.startAnimation(fadeInAnimation);

            userSetsLl.addView(setItemView);
        }
    }


    private void viewSet(ModelLearningSet set) {
        Intent intent = new Intent(requireContext(), ActivityViewLearningSet.class);

        intent.putExtra("setId", set.getId());
        intent.putExtra("learningSet", set);

        intent.putExtra("isSetNew", false);
        intent.putExtra("isUserSet", true);
        intent.putExtra("isSetInGroup", false);

        startActivity(intent);
    }

    public void confirmSetDeletionWithRestart(ModelLearningSet set, int attemptCount) {
        Dialog dialog = Utils.createDialog(requireActivity(), R.layout.dialog_delete_are_you_sure, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT), Gravity.BOTTOM);
        DialogDeleteAreYouSureBinding dialogBinding = DialogDeleteAreYouSureBinding.inflate(LayoutInflater.from(requireActivity()));
        Objects.requireNonNull(dialog).setContentView(dialogBinding.getRoot());
        dialog.show();

        TextView cancelTv = dialogBinding.deleteCancelTv;
        TextView confirmTv = dialogBinding.deleteConfirmTv;

        cancelTv.setOnClickListener(v -> dialog.dismiss());
        confirmTv.setOnClickListener(v -> {
            Utils.disableItems(cancelTv, confirmTv);
            userService.deleteUserSet(set.getId(), new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    try {
                        ResponseHandler.handleResponse(response);
                        Utils.showToast(requireActivity(), set.getName() + " was deleted");
                        sets.clear();
                        getUserLibrarySetList(sets, null);
                        Utils.enableItems(cancelTv, confirmTv);
                    } catch (MalletException e) {
                        Utils.showToast(requireActivity(), e.getMessage());
                        Utils.enableItems(cancelTv, confirmTv);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    if (attemptCount < MAX_RETRY_ATTEMPTS) {
                       // System.out.println(attemptCount);
                        confirmSetDeletionWithRestart(set, attemptCount + 1);
                    } else {
                        Utils.showToast(requireActivity(), "Network error");
                        Utils.enableItems(cancelTv, confirmTv);
                    }
                }
            });


            dialog.dismiss();
        });
    }

    private void confirmSetDeletion(ModelLearningSet set) {
        int attemptCount = MAX_RETRY_ATTEMPTS;
        confirmSetDeletionWithRestart(set, attemptCount);
    }
}