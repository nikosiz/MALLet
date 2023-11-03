package com.example.mallet;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.agh.api.SetBasicDTO;
import com.agh.api.SetInformationDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.entity.set.ModelLearningSetMapper;
import com.example.mallet.databinding.FragmentUserLibrarySetsBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.jakewharton.rxbinding.widget.RxTextView;

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
    private FragmentUserLibrarySetsBinding binding;
    private UserServiceImpl userService;
    private final List<ModelLearningSet> sets = new ArrayList<>();
    private final List<ModelLearningSet> foundSets = new ArrayList<>();
    private TextInputEditText searchEt;
    private LinearLayout userSetsLl;
    private LayoutInflater inflater;
    private AtomicBoolean firstTime = new AtomicBoolean(true);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String credential = AuthenticationUtils.get(getContext());
        this.userService = new UserServiceImpl(credential);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserLibrarySetsBinding.inflate(inflater, container, false);
        setupContents(inflater);

        fetchSets(0, 50);
        getUserLibrarySetList(inflater, userSetsLl, sets, null);
        return binding.getRoot();
    }

    private void fetchSets(long startPosition, long limit) {
        RxTextView.textChanges(searchEt)
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(text -> {
                    if(firstTime.get()){
                        return;
                    }
                    foundSets.clear();
                    if (text.length() == 0) {
                        getUserLibrarySetList(inflater, userSetsLl, sets, null);
                        return;
                    }
                    userService.getUserSets(startPosition, limit, new Callback<SetBasicDTO>() {
                        @Override
                        public void onResponse(Call<SetBasicDTO> call, Response<SetBasicDTO> response) {
                            userSetsLl.removeAllViews();
                            fetchSets(text.toString(),response);
                        }

                        @Override
                        public void onFailure(Call<SetBasicDTO> call, Throwable t) {
                            Utils.showToast(getContext(), "Network failure");
                        }
                    });
                });
    }

    private void fetchSets(String text,Response<SetBasicDTO> response) {
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

            fetchSets(Long.parseLong(startPosition), Long.parseLong(limit));
        }else{
            setView(inflater,foundSets);
        }
    }

    private void setupContents(LayoutInflater inflater) {
        searchEt = binding.userLibrarySetsSearchEt;
        userSetsLl = binding.userLibrarySetsAllSetsLl;
        this.inflater = inflater;
    }

    private void getUserLibrarySetList(@NonNull LayoutInflater inflater,
                                       LinearLayout setsLl,
                                       List<ModelLearningSet> setList,
                                       @Nullable String nextChunkUri) {

        if (Objects.isNull(nextChunkUri)) {
            fetchSets(0, 10, inflater, setsLl, setList);
        } else {
            Uri uri = Uri.parse(nextChunkUri);
            String startPosition = uri.getQueryParameter("startPosition");
            String limit = uri.getQueryParameter("limit");
            fetchSets(Long.parseLong(startPosition), Long.parseLong(limit), inflater, userSetsLl, setList);
        }
    }

    private void fetchSets(long startPosition,
                           long limit,
                           @NonNull LayoutInflater inflater,
                           LinearLayout setsLl,
                           List<ModelLearningSet> setList) {
        userService.getUserSets(startPosition, limit, new Callback<SetBasicDTO>() {
            @Override
            public void onResponse(Call<SetBasicDTO> call, Response<SetBasicDTO> response) {
                SetBasicDTO setBasicDTO = ResponseHandler.handleResponse(response);
                List<ModelLearningSet> modelLearningSets = ModelLearningSetMapper.from(setBasicDTO.sets());
                if(!setList.equals(modelLearningSets)){
                    setList.clear();
                    setList.addAll(modelLearningSets);
                }

                setView(inflater, setList);
                firstTime.set(false);
            }

            @Override
            public void onFailure(Call<SetBasicDTO> call, Throwable t) {
                Utils.showToast(getContext(), "Network failure");
            }
        });
    }

    private void setView(@NonNull LayoutInflater inflater, List<ModelLearningSet> userLibraryFoldersList) {
       userSetsLl.removeAllViews();
        for (ModelLearningSet set : userLibraryFoldersList) {
            View setItemView = inflater.inflate(R.layout.model_learning_set, userSetsLl, false);

            TextView setNameTv = setItemView.findViewById(R.id.learningSet_nameTv);
            setNameTv.setText(set.getName());

            TextView setNrOfTermsTv = setItemView.findViewById(R.id.learningSet_nrOfTermsTv);
            setNrOfTermsTv.setText(String.valueOf(set.getNrOfTerms()));

            TextView setCreatorTv = setItemView.findViewById(R.id.learningSet_creatorTv);
            setCreatorTv.setText(set.getCreator());


            userSetsLl.addView(setItemView);
        }
    }
}