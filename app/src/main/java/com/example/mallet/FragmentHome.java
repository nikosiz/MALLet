package com.example.mallet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import java.util.Objects;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentHome extends Fragment {
    private ActivityMain activityMain;
    private FragmentHomeBinding binding;
    private UserServiceImpl userService;
    private ViewPager2 homeGroupsVp2;
    private ViewPager2 homeSetsVp2;
    private ProgressBar progressBar;
    private Animation fadeInAnimation;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        String credential = AuthenticationUtils.get(Objects.requireNonNull(requireContext()));
        this.userService = new UserServiceImpl(credential);

        setupContents();

        activityMain = (ActivityMain) getActivity();

        Executors.newFixedThreadPool(1).submit(this::setupGroups);
        Executors.newFixedThreadPool(1).submit(this::setupLearningSets);

        return binding.getRoot();
    }

    private void setupContents() {
        homeSetsVp2 = binding.homeSetsViewPager;
        homeGroupsVp2 = binding.homeGroupsViewPager;
        binding.homeSetViewAllTv.setOnClickListener(v -> showAllItems(0));
        //binding.homeFolderViewAllTv.setOnClickListener(v -> showAllItems(1));
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

                AdapterGroup adapterGroups = new AdapterGroup(getActivity(), modelGroups, openActivityViewGroup());
                homeGroupsVp2.setAdapter(adapterGroups);

                homeGroupsVp2.setPageTransformer(Utils::applySwipeTransformer);

                homeGroupsVp2.startAnimation(fadeInAnimation);
            }

            @Override
            public void onFailure(Call<GroupBasicDTO> call, Throwable t) {
                Utils.showToast(getActivity(), "Network failure");
            }
        });
    }

    @NonNull
    private AdapterGroup.OnGroupClickListener openActivityViewGroup() {
        return v -> {
            Context context = getActivity();
            Intent intent = new Intent(context, ActivityViewGroup.class);


            intent.putExtra("groupId", v.getId());
            intent.putExtra("groupName", v.getGroupName());
            if (context != null) {
                context.startActivity(intent);
            }
        };
    }

    private void setupLearningSets() {
        // todo replace with userService below - this is for testing
        /*List<ModelLearningSet> sets = new ArrayList<>();

        ModelLearningSet set = Utils.readFlashcards(requireContext(),"animals.txt");

        sets.add(set);

        AdapterLearningSet adapterSets = new AdapterLearningSet(getContext(), sets, learningSet -> {
            Intent intent = new Intent(getContext(), ActivityViewLearningSet.class);

            intent.putExtra("learningSet", learningSet);

            startActivity(intent);
        });

        homeSetsViewPager.setAdapter(adapterSets);

        homeSetsViewPager.setPageTransformer(Utils::applySwipeTransformer);*/

        userService.getUserSets(0, 5, new Callback<SetBasicDTO>() {
            @Override
            public void onResponse(Call<SetBasicDTO> call, Response<SetBasicDTO> response) {
                SetBasicDTO setBasicDTO = ResponseHandler.handleResponse(response);
                List<ModelLearningSet> modelLearningSets = ModelLearningSetMapper.from(setBasicDTO.sets());

                AdapterLearningSet adapterSets = new AdapterLearningSet(getActivity(), modelLearningSets, openActivityViewSet());

                homeSetsVp2.setAdapter(adapterSets);

                homeSetsVp2.setPageTransformer(Utils::applySwipeTransformer);

                homeSetsVp2.startAnimation(fadeInAnimation);
            }

            @Override
            public void onFailure(Call<SetBasicDTO> call, Throwable t) {
                Utils.showToast(getActivity(), "Network failure");
            }
        });
    }

    @NonNull
    private AdapterLearningSet.OnLearningSetClickListener openActivityViewSet() {
        return learningSet -> {
            Intent intent = new Intent(getActivity(), ActivityViewLearningSet.class);

            intent.putExtra("learningSet", learningSet);
            intent.putExtra("setId", learningSet.getId());

            startActivity(intent);
        };
    }

    private void showAllItems(int index) {
        // TODO: Implement this method to display all items of a specific type
        Utils.showToast(getActivity(), "Here should open a list of " + index);
    }
}