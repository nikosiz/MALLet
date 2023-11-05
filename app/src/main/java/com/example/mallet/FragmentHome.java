package com.example.mallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.agh.api.GroupBasicDTO;
import com.agh.api.SetBasicDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.entity.group.ModelGroupMapper;
import com.example.mallet.backend.entity.set.ModelLearningSetMapper;
import com.example.mallet.databinding.FragmentHomeBinding;
import com.example.mallet.utils.AdapterGroup;
import com.example.mallet.utils.AdapterLearningSet;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelGroup;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {

    private FragmentHomeBinding binding;
    private ActivityMain activityMain;
    private UserServiceImpl userService;
    private ViewPager2 homeGroupsViewPager;
    private ViewPager2 homeSetsViewPager;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        String credential = AuthenticationUtils.get(getContext());
        this.userService = new UserServiceImpl(credential);

        setupContents();

        activityMain = (ActivityMain) getActivity();

        Executors.newFixedThreadPool(1).submit(this::setupGroups);
        Executors.newFixedThreadPool(1).submit(this::setupLearningSets);

        return binding.getRoot();
    }

    private void setupContents() {
        homeSetsViewPager = binding.homeSetsViewPager;
        homeGroupsViewPager = binding.homeGroupsViewPager;
        binding.homeSetViewAllTv.setOnClickListener(v -> showAllItems(0));
        binding.homeFolderViewAllTv.setOnClickListener(v -> showAllItems(1));
        binding.homeGroupViewAllTv.setOnClickListener(v -> showAllItems(2));

        progressBar = binding.fragmentHomeProgressBar;
    }

    private void setupGroups() {
        userService.getUserGroups(0, 5, new Callback<GroupBasicDTO>() {
            @Override
            public void onResponse(Call<GroupBasicDTO> call, Response<GroupBasicDTO> response) {
                Utils.hideItems(progressBar);

                GroupBasicDTO groupDTO = ResponseHandler.handleResponse(response);
                List<ModelGroup> modelGroups = ModelGroupMapper.from(groupDTO.groups());

                AdapterGroup adapterGroups = new AdapterGroup(getContext(), modelGroups, openActivityViewGroup());
                homeGroupsViewPager.setAdapter(adapterGroups);

                homeGroupsViewPager.setPageTransformer(Utils::applySwipeTransformer);
            }

            @Override
            public void onFailure(Call<GroupBasicDTO> call, Throwable t) {
                Utils.showToast(getContext(), "Network failure");
            }
        });
    }

    @NonNull
    private AdapterGroup.OnGroupClickListener openActivityViewGroup() {
        return v -> {
            Context context = getContext();
            Intent intent = new Intent(context, ActivityViewGroup.class);
            intent.putExtra("groupId", v.getId());
            intent.putExtra("groupName", v.getGroupName());
            context.startActivity(intent);
        };
    }

    private void setupLearningSets() {
        // todo replace with userService below - this is for testing
        List<ModelLearningSet> sets = new ArrayList<>();

        ModelLearningSet set = Utils.readFlashcards(requireContext(),"animals.txt");

        sets.add(set);

        AdapterLearningSet adapterSets = new AdapterLearningSet(getContext(), sets, learningSet -> {
            Intent intent = new Intent(getContext(), ActivityViewLearningSet.class);

            intent.putExtra("learningSet", learningSet);

            startActivity(intent);
        });

        homeSetsViewPager.setAdapter(adapterSets);

        homeSetsViewPager.setPageTransformer(Utils::applySwipeTransformer);

        /*userService.getUserSets(0, 5, new Callback<SetBasicDTO>() {
            @Override
            public void onResponse(Call<SetBasicDTO> call, Response<SetBasicDTO> response) {
                SetBasicDTO setBasicDTO = ResponseHandler.handleResponse(response);
                List<ModelLearningSet> modelLearningSets = ModelLearningSetMapper.from(setBasicDTO.sets());

                AdapterLearningSet adapterSets = new AdapterLearningSet(getContext(), modelLearningSets, openActivityViewSet());

                homeSetsViewPager.setAdapter(adapterSets);

                homeSetsViewPager.setPageTransformer(Utils::applySwipeTransformer);
            }

            @Override
            public void onFailure(Call<SetBasicDTO> call, Throwable t) {
                Utils.showToast(getContext(), "Network failure");
            }
        });*/
    }

    @NonNull
    private AdapterLearningSet.OnLearningSetClickListener openActivityViewSet() {
        return learningSet -> {
            Intent intent = new Intent(getContext(), ActivityViewLearningSet.class);

            intent.putExtra("setId", learningSet.getId());

            startActivity(intent);
        };
    }

    private void showAllItems(int index) {
        // TODO: Implement this method to display all items of a specific type
        Utils.showToast(getContext(), "Here should open a list of " + index);
    }
}
