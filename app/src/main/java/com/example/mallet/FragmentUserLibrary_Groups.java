package com.example.mallet;

import android.content.Intent;
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

import com.agh.api.GroupDTO;
import com.agh.api.SetBasicDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.group.control.mapper.GroupAdminDTOMapper;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.entity.set.ModelLearningSetMapper;
import com.example.mallet.backend.mapper.group.GroupCreateContainerMapper;
import com.example.mallet.databinding.FragmentUserLibraryGroupsBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentUserLibrary_Groups extends Fragment {
    private FragmentUserLibraryGroupsBinding binding;
    private UserServiceImpl userService;
    private final List<ModelGroup> groups = new ArrayList<>();
    private TextInputEditText searchEt;
    private LinearLayout userGroupsLl;

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

        setupContents();

        getUserLibraryGroupList(inflater, userGroupsLl, groups, null);

        return binding.getRoot();
    }

    private void setupContents() {
        searchEt = binding.userLibraryGroupsSearchEt;
        userGroupsLl = binding.userLibraryGroupsAllGroupsLl;
    }

    private void getUserLibraryGroupList(@NonNull LayoutInflater inflater,
                                         LinearLayout groupsLl,
                                         List<ModelGroup> groupList,
                                         @Nullable String nextChunkUri) {

        if (Objects.isNull(nextChunkUri)) {
            fetchGroups(0, 10, inflater, groupsLl, groupList);
        } else {
            Uri uri = Uri.parse(nextChunkUri);
            String startPosition = uri.getQueryParameter("startPosition");
            String limit = uri.getQueryParameter("limit");
            fetchGroups(Long.parseLong(startPosition), Long.parseLong(limit), inflater, groupsLl, groupList);
        }
    }

    private void fetchGroups(long startPosition,
                             long limit,
                             @NonNull LayoutInflater inflater,
                             LinearLayout groupsLl,
                             List<ModelGroup> groupList) {/*
        userService.getUserSets(startPosition, limit, new Callback<GroupDTO>() {
            @Override
            public void onResponse(Call<GroupDTO> call, Response<GroupDTO> response) {
                GroupDTO groupDTO = ResponseHandler.handleResponse(response);
                List<ModelGroup> modelGroups = GroupAdminDTOMapper.from(groupDTO.name());
                groupList.addAll(modelGroups);
                groupList.addAll(modelGroups);
                groupList.addAll(modelGroups);
                groupList.addAll(modelGroups);
                groupList.addAll(modelGroups);
                groupList.addAll(modelGroups);
                groupList.addAll(modelGroups);
                groupList.addAll(modelGroups);

                groupView(inflater, groupsLl, groupList);
            }

            @Override
            public void onFailure(Call<GroupDTO> call, Throwable t) {

            }
        });*/
    }

    private void groupView(@NonNull LayoutInflater inflater, LinearLayout userGroupsLl, List<ModelGroup> userLibraryGroupList) {
        for (ModelGroup group : userLibraryGroupList) {
            View groupItemView = inflater.inflate(R.layout.model_group, userGroupsLl, false);

            TextView groupNameTv = groupItemView.findViewById(R.id.group_nameTv);
            groupNameTv.setText(group.getGroupName());

            TextView groupNrOfMembers = groupItemView.findViewById(R.id.group_nrOfMembersTv);
            groupNrOfMembers.setText(group.getNrOfMembers());

            TextView groupNrOfSetsTv = groupItemView.findViewById(R.id.group_nrOfSetsTv);
            groupNrOfSetsTv.setText(group.getNrOfSets());

            // todo odkomentować
            //setNrOfTermsTv.setText(String.valueOf(set.getNrOfTerms()));

            userGroupsLl.addView(groupItemView);
        }
    }

    private List<ModelGroup> getGroupList() {
        List<ModelGroup> groupList = new ArrayList<>();
        groupList.add(new ModelGroup("Group #1", "3", "3"));
        groupList.add(new ModelGroup("Group #2", "7", "3"));
        groupList.add(new ModelGroup("Group #3", "2", "3"));
        groupList.add(new ModelGroup("Group #4", "8", "3"));
        groupList.add(new ModelGroup("Group #5", "1", "3"));
        return groupList;
    }

    private void startViewGroupActivity() {
        Intent intent = new Intent(getContext(), ActivityViewGroup.class);
        startActivity(intent);
    }
}