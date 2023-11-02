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
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.entity.set.ModelLearningSetMapper;
import com.example.mallet.databinding.FragmentUserLibrarySetsBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelLearningSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentUserLibrary_Sets extends Fragment {

    private UserServiceImpl userService;
    private List<ModelLearningSet> sets = new ArrayList<>();
    public static FragmentUserLibrary_Sets newInstance(String param1, String param2) {

        return new FragmentUserLibrary_Sets();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String credential = AuthenticationUtils.get(getContext());
        this.userService = new UserServiceImpl(credential);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        com.example.mallet.databinding.FragmentUserLibrarySetsBinding binding = FragmentUserLibrarySetsBinding.inflate(inflater, container, false);

        LinearLayout userLibrarySetsLl = binding.userLibrarySetsAllSetsLl;
        getUserLibrarySetList(inflater, userLibrarySetsLl, sets, null);

        return binding.getRoot();
    }

    private void setView(@NonNull LayoutInflater inflater, LinearLayout userLibrarySetsLl, List<ModelLearningSet> userLibraryFoldersList) {
        for (ModelLearningSet set : userLibraryFoldersList) {
            View setItemView = inflater.inflate(R.layout.model_learning_set, userLibrarySetsLl, false);

            TextView setNameTv = setItemView.findViewById(R.id.learningSet_nameTv);
            setNameTv.setText(set.getName());

            TextView setNrOfTermsTv = setItemView.findViewById(R.id.learningSet_nrOfTermsTv);
            setNrOfTermsTv.setText(String.valueOf(set.getNrOfTerms()));

            TextView setCreatorTv = setItemView.findViewById(R.id.learningSet_creatorTv);
            setCreatorTv.setText(set.getCreator());


            userLibrarySetsLl.addView(setItemView);
        }

    }

    private void getUserLibrarySetList(@NonNull LayoutInflater inflater,
                                       LinearLayout userLibrarySetsLl,
                                       List<ModelLearningSet> setList,
                                       @Nullable String nextChunkUri) {

        if (Objects.isNull(nextChunkUri)) {
            fetchSets(0, 10, inflater, userLibrarySetsLl, setList);
        } else {
            Uri uri = Uri.parse(nextChunkUri);
            String startPosition = uri.getQueryParameter("startPosition");
            String limit = uri.getQueryParameter("limit");
            fetchSets(Long.parseLong(startPosition), Long.parseLong(limit), inflater, userLibrarySetsLl, setList);
        }
    }

    private void fetchSets(long startPosition,
                           long limit,
                           @NonNull LayoutInflater inflater,
                           LinearLayout userLibrarySetsLl,
                           List<ModelLearningSet> setList) {
        //todo poprawic na scroll view
        userService.getUserSets(startPosition, limit, new Callback<SetBasicDTO>() {
            @Override
            public void onResponse(Call<SetBasicDTO> call, Response<SetBasicDTO> response) {
                SetBasicDTO setBasicDTO = ResponseHandler.handleResponse(response);
                List<ModelLearningSet> modelLearningSets = ModelLearningSetMapper.from(setBasicDTO.sets());
                setList.addAll(modelLearningSets);
                setList.addAll(modelLearningSets);
                setList.addAll(modelLearningSets);
                setList.addAll(modelLearningSets);
                setList.addAll(modelLearningSets);
                setList.addAll(modelLearningSets);
                setList.addAll(modelLearningSets);
                setList.addAll(modelLearningSets);

                setView(inflater, userLibrarySetsLl, setList);
            }

            @Override
            public void onFailure(Call<SetBasicDTO> call, Throwable t) {

            }
        });
    }
}