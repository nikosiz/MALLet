package com.example.mallet;

import static com.example.mallet.ActivityViewGroup.groupId;
import static com.example.mallet.ActivityViewGroup.groupName;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.agh.api.GroupDTO;
import com.example.mallet.backend.client.group.boundary.GroupServiceImpl;
import com.example.mallet.backend.entity.set.ModelLearningSetMapper;
import com.example.mallet.databinding.FragmentViewGroupSetsBinding;
import com.example.mallet.utils.ModelLearningSet;
import com.example.mallet.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentViewGroup_Sets extends Fragment {
    private FragmentViewGroupSetsBinding binding;
    private LinearLayout userLibrarySetsLl;
    private final GroupDTO chosenGroup;
    List<ModelLearningSet> userLibrarySetList;

    public FragmentViewGroup_Sets(GroupDTO chosenGroup) {
        this.chosenGroup = chosenGroup;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentViewGroupSetsBinding.inflate(inflater, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        setupContents();

        userLibrarySetList = getUserLibrarySetList();

        groupService = new GroupServiceImpl(ActivityViewGroup.credential);

        displaySet();

        return binding.getRoot();
    }

    private Long setId;

    private void displaySet() {
        for (ModelLearningSet set : userLibrarySetList) {
            View setItemView = getLayoutInflater().inflate(R.layout.model_learning_set, userLibrarySetsLl, false);

            ImageView deleteSetIv = setItemView.findViewById(R.id.learningSet_deleteIv);
            deleteSetIv.setOnClickListener(v -> deleteSetFromGroup());

            setId = set.getId();
            TextView setName = setItemView.findViewById(R.id.learningSet_nameTv);
            TextView nrOfTerms = setItemView.findViewById(R.id.learningSet_nrOfTermsTv);
            TextView creator = setItemView.findViewById(R.id.learningSet_creatorTv);
            Utils.hideItems(creator);

            setName.setText(set.getName());
            nrOfTerms.setText(set.getNrOfTerms() + " terms");

            userLibrarySetsLl.addView(setItemView);
            setItemView.setOnClickListener(view -> openActivityViewSet(set));
        }
    }

    private GroupServiceImpl groupService;

    private void deleteSetFromGroup() {
        groupService.removeSet(groupId, setId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Utils.showToast(getActivity(), "Deleted");

                Intent intent = new Intent(getContext(), ActivityViewGroup.class);

                intent.putExtra("groupId", groupId);
                intent.putExtra("groupName", groupName);

                startActivity(intent);

                requireActivity().finish();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
        /*groupService.removeSet(ActivityViewGroup.groupId, setId, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // Utils.showToast(getActivity(), "deleted");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });*/
    }

    private void setupContents() {
        userLibrarySetList = getUserLibrarySetList();
        userLibrarySetsLl = binding.viewGroupSetsSetListLl;
    }

    private List<ModelLearningSet> getUserLibrarySetList() {
        return ModelLearningSetMapper.from(chosenGroup.sets());
    }

    private void openActivityViewSet(ModelLearningSet set) {
        Intent intent = new Intent(getContext(), ActivityViewLearningSet.class);

        intent.putExtra("groupId", chosenGroup.id());

        intent.putExtra("setId", set.getId());
        intent.putExtra("learningSet", set);
        intent.putExtra("isSetInGroup", true);

        intent.putExtra("isSetNew", false);
        intent.putExtra("isUserSet", true);
        intent.putExtra("isSetInGroup", true);
        intent.putExtra("canUserEditSet", ActivityViewGroup.canUserEditSet);

        startActivity(intent);

        getActivity().finish();
    }

}
