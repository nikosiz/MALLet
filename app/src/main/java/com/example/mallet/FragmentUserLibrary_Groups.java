package com.example.mallet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.agh.api.GroupBasicDTO;
import com.agh.api.GroupBasicInformationDTO;
import com.example.mallet.backend.client.configuration.ResponseHandler;
import com.example.mallet.backend.client.group.boundary.GroupServiceImpl;
import com.example.mallet.backend.client.user.boundary.UserServiceImpl;
import com.example.mallet.backend.entity.group.ModelGroupMapper;
import com.example.mallet.databinding.FragmentUserLibraryGroupsBinding;
import com.example.mallet.utils.AuthenticationUtils;
import com.example.mallet.utils.ModelGroup;
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

public class FragmentUserLibrary_Groups extends Fragment {
    private ActivityMain activityMain;
    private FragmentUserLibraryGroupsBinding binding;
    private UserServiceImpl userService;
    private GroupServiceImpl groupService;
    private final List<ModelGroup> groups = new ArrayList<>();
    private TextInputEditText searchEt;
    private LinearLayout userGroupsLl;
    private final AtomicBoolean firstTime = new AtomicBoolean(true);
    private LayoutInflater inflater;
    private final List<ModelGroup> foundGroups = new ArrayList<>();
    private ProgressBar progressBar;
    private Animation fadeInAnimation;
    private ScrollView groupsSv;

    private String nextChunkUri = StringUtils.EMPTY;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof ActivityMain) {
            activityMain = (ActivityMain) context;
            fadeInAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        } else {
            // Handle the case where the activity does not implement the method
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String credential = AuthenticationUtils.get(requireContext());

        this.userService = new UserServiceImpl(credential);
        this.groupService = new GroupServiceImpl(credential);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserLibraryGroupsBinding.inflate(inflater, container, false);

        setupContents(inflater);
        setupSearchAndFetchGroups(0, 50);
        getUserLibraryGroupList(groups, null);

        groupsSv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int contentHeight = groupsSv.getChildAt(0).getHeight();
                int scrollHeight = groupsSv.getHeight();

                int scrollY = groupsSv.getScrollY();

                if (scrollY + scrollHeight >= contentHeight) {
                    if (!nextChunkUri.isEmpty()) {
                        getUserLibraryGroupList(groups, nextChunkUri);
                    }
                }
            }
        });
        return binding.getRoot();
    }

    private void setupContents(LayoutInflater inflater) {
        searchEt = binding.userLibraryGroupsSearchEt;
        userGroupsLl = binding.userLibraryGroupsAllGroupsLl;

        groupsSv = binding.userLibraryGroupsSv;

        progressBar = binding.userLibraryGroupsProgressBar;

        this.inflater = inflater;
    }

    private void setupSearchAndFetchGroups(long startPosition, long limit) {
        RxTextView.textChanges(searchEt)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(text -> {
                    if (firstTime.get()) {
                        return;
                    }
                    foundGroups.clear();
                    if (text.length() == 0) {
                        getUserLibraryGroupList(groups, null);
                        return;
                    }
                    userService.getUserGroups(startPosition, limit, new Callback<GroupBasicDTO>() {
                        @Override
                        public void onResponse(Call<GroupBasicDTO> call, Response<GroupBasicDTO> response) {
                            userGroupsLl.removeAllViews();
                            fetchGroupsForSearch(text.toString(), response);
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

            if (startPosition != null) {
                setupSearchAndFetchGroups(Long.parseLong(startPosition), Long.parseLong(limit));
            }
        } else {
            setupGroupView(foundGroups);
        }
    }

    private void getUserLibraryGroupList(List<ModelGroup> groupList,
                                         @Nullable String nextChunkUri) {

        if (Objects.isNull(nextChunkUri)) {
            fetchUserGroups(0, 10, groupList);
        } else {
            Uri uri = Uri.parse(nextChunkUri);
            String startPosition = uri.getQueryParameter("startPosition");
            String limit = uri.getQueryParameter("limit");
            if (startPosition != null) {
                fetchUserGroups(Long.parseLong(startPosition), Long.parseLong(limit), groupList);
            }
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
                if (Objects.nonNull(groupDTO.nextChunkUri())) {
                    nextChunkUri = groupDTO.nextChunkUri();
                }
                List<ModelGroup> modelGroups = ModelGroupMapper.from(groupDTO.groups());
                groupList.addAll(modelGroups);

                if (!groupList.equals(modelGroups)) {
                    groupList.clear();
                    groupList.addAll(modelGroups);
                }

                setupGroupView(groupList);
                firstTime.set(false);

            }

            @Override
            public void onFailure(Call<GroupBasicDTO> call, Throwable t) {
                Utils.showToast(getContext(), "Network failure");
            }
        });
    }

    private void setupGroupView(List<ModelGroup> userLibraryGroupList) {
        userGroupsLl.removeAllViews();
        for (ModelGroup group : userLibraryGroupList) {
            View groupItemView = inflater.inflate(R.layout.model_group, userGroupsLl, false);

            groupItemView.setOnClickListener(v -> viewGroup(group));

            TextView groupNameTv = groupItemView.findViewById(R.id.group_nameTv);
            groupNameTv.setText(group.getGroupName());

            TextView groupNrOfMembers = groupItemView.findViewById(R.id.group_nrOfMembersTv);
            groupNrOfMembers.setText(group.getNrOfMembers() + " members");

            TextView groupNrOfSetsTv = groupItemView.findViewById(R.id.group_nrOfSetsTv);
            groupNrOfSetsTv.setText(group.getNrOfSets() + " sets");

            groupItemView.startAnimation(fadeInAnimation);

            userGroupsLl.addView(groupItemView);
        }
    }

    private void viewGroup(ModelGroup group) {
        Intent intent = new Intent(requireContext(), ActivityViewGroup.class);

        intent.putExtra("groupId", group.getId());
        intent.putExtra("groupName", group.getGroupName());

        startActivity(intent);
    }
}