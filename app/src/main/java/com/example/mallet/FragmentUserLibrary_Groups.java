package com.example.mallet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.agh.api.GroupBasicDTO;
import com.agh.api.GroupBasicInformationDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.entity.group.ModelGroupMapper;
import com.example.mallet.databinding.FragmentUserLibraryGroupsBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelGroup;
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

public class FragmentUserLibrary_Groups extends Fragment {
    private FragmentUserLibraryGroupsBinding binding;
    private UserServiceImpl userService;
    private final List<ModelGroup> groups = new ArrayList<>();
    private TextInputEditText searchEt;
    private LinearLayout userGroupsLl;
    private final AtomicBoolean firstTime = new AtomicBoolean(true);
    private LayoutInflater inflater;
    private final List<ModelGroup> foundGroups = new ArrayList<>();
    private ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String credential = AuthenticationUtils.get(getContext());
        this.userService = new UserServiceImpl(credential);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserLibraryGroupsBinding.inflate(inflater, container, false);

        setupContents(inflater);
        setupSearchAndFetchGroups(0, 50);
        getUserLibraryGroupList(userGroupsLl, groups, null);

        return binding.getRoot();
    }

    private void setupContents(LayoutInflater inflater) {
        searchEt = binding.userLibraryGroupsSearchEt;
        userGroupsLl = binding.userLibraryGroupsAllGroupsLl;

        progressBar = binding.userLibraryGroupsProgressBar;


        this.inflater = inflater;
    }

    private void setupSearchAndFetchGroups(long startPosition, long limit) {
        RxTextView.textChanges(searchEt)
                .debounce(1, TimeUnit.SECONDS)
                .subscribe(text -> {
                    if(firstTime.get()){
                        return;
                    }
                    foundGroups.clear();
                    if (text.length() == 0) {
                        getUserLibraryGroupList(userGroupsLl, groups, null);
                        return;
                    }
                    userService.getUserGroups(startPosition, limit, new Callback<GroupBasicDTO>() {
                        @Override
                        public void onResponse(Call<GroupBasicDTO> call, Response<GroupBasicDTO> response) {
                            userGroupsLl.removeAllViews();
                            fetchGroupsForSearch(text.toString(),response);
                        }

                        @Override
                        public void onFailure(Call<GroupBasicDTO> call, Throwable t) {
                            Utils.showToast(getContext(), "Network failure");
                        }
                    });
                });
    }

    private void fetchGroupsForSearch(String text, Response<GroupBasicDTO> response) {
        GroupBasicDTO groupBasicDTO = ResponseHandler.handleResponse(response);
        List<GroupBasicInformationDTO> collect = groupBasicDTO.groups().stream()
                .filter(group -> group.name().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
        List<ModelGroup> groups = ModelGroupMapper.from(collect);
        foundGroups.addAll(groups);
        if (Objects.nonNull(groupBasicDTO.nextChunkUri())) {
            Uri uri = Uri.parse(groupBasicDTO.nextChunkUri());
            String startPosition = uri.getQueryParameter("startPosition");
            String limit = uri.getQueryParameter("limit");

            setupSearchAndFetchGroups(Long.parseLong(startPosition), Long.parseLong(limit));
        }else{
            setView(foundGroups);
        }
    }
    private void getUserLibraryGroupList(LinearLayout groupsLl,
                                         List<ModelGroup> groupList,
                                         @Nullable String nextChunkUri) {

        if (Objects.isNull(nextChunkUri)) {
            fetchUserGroups(0, 10, groupList);
        } else {
            Uri uri = Uri.parse(nextChunkUri);
            String startPosition = uri.getQueryParameter("startPosition");
            String limit = uri.getQueryParameter("limit");
            fetchUserGroups(Long.parseLong(startPosition), Long.parseLong(limit), groupList);
        }
    }

    private void fetchUserGroups(long startPosition,
                                 long limit,
                                 List<ModelGroup> groupList) {
        userService.getUserGroups(startPosition, limit, new Callback<GroupBasicDTO>() {
            @Override
            public void onResponse(Call<GroupBasicDTO> call, Response<GroupBasicDTO> response) {
                Utils.hideItems(progressBar);

                GroupBasicDTO groupDTO = ResponseHandler.handleResponse(response);
                List<ModelGroup> modelGroups = ModelGroupMapper.from(groupDTO.groups());
                groupList.addAll(modelGroups);

                if(!groupList.equals(modelGroups)){
                    groupList.clear();
                    groupList.addAll(modelGroups);
                }

                setView(groupList);
                firstTime.set(false);

            }

            @Override
            public void onFailure(Call<GroupBasicDTO> call, Throwable t) {
                Utils.showToast(getContext(), "Network failure");
            }
        });
    }

    private void setView(List<ModelGroup> userLibraryGroupList) {
        userGroupsLl.removeAllViews();
        for (ModelGroup group : userLibraryGroupList) {
            View groupItemView = inflater.inflate(R.layout.model_group, userGroupsLl, false);

            groupItemView.setOnClickListener(v -> viewGroup(group));

            TextView groupNameTv = groupItemView.findViewById(R.id.group_nameTv);
            groupNameTv.setText(group.getGroupName());

            TextView groupNrOfMembers = groupItemView.findViewById(R.id.group_nrOfMembersTv);
            groupNrOfMembers.setText(group.getNrOfMembers());

            TextView groupNrOfSetsTv = groupItemView.findViewById(R.id.group_nrOfSetsTv);
            groupNrOfSetsTv.setText(group.getNrOfSets());


            userGroupsLl.addView(groupItemView);
        }
    }

    private void viewGroup(ModelGroup group) {
        Intent intent = new Intent(requireContext(), ActivityViewGroup.class);

        intent.putExtra("groupId", group.getId());

        startActivity(intent);
    }

    private void startViewGroupActivity() {
        Intent intent = new Intent(getContext(), ActivityViewGroup.class);
        startActivity(intent);
    }
}