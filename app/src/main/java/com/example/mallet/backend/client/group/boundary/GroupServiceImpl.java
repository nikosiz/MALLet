package com.example.mallet.backend.client.group.boundary;

import com.agh.api.GroupContributionDeleteDTO;
import com.agh.api.GroupCreateDTO;
import com.agh.api.GroupDTO;
import com.agh.api.GroupSetCreateDTO;
import com.agh.api.GroupSetDTO;
import com.agh.api.GroupUpdateAdminDTO;
import com.agh.api.GroupUpdateDTO;
import com.example.mallet.backend.client.configuration.RetrofitClient;
import com.example.mallet.backend.client.group.control.GroupService;
import com.example.mallet.backend.client.group.control.mapper.GroupAdminDTOMapper;
import com.example.mallet.backend.client.group.control.mapper.GroupContributionDeleteDTOMapper;
import com.example.mallet.backend.client.group.control.mapper.GroupCreateDTOMapper;
import com.example.mallet.backend.client.group.control.mapper.GroupSetCreateDTOMapper;
import com.example.mallet.backend.client.group.control.mapper.GroupSetDTOMapper;
import com.example.mallet.backend.client.group.control.mapper.GroupUpdateDTOMapper;
import com.example.mallet.backend.entity.group.create.GroupCreateContainer;
import com.example.mallet.backend.entity.group.update.GroupUpdateContainer;
import com.example.mallet.backend.entity.set.SetCreateContainer;

import java.util.Set;

import lombok.NonNull;
import retrofit2.Callback;

public class GroupServiceImpl {

    private final GroupService groupService;

    public GroupServiceImpl(String credential) {
        this.groupService = RetrofitClient.getRetrofitClient(credential)
                .create(GroupService.class);
    }

    public void getGroup(@NonNull Long id,
                         Callback<GroupDTO> callback) {
        groupService.getGroup(id)
                .enqueue(callback);
    }

    public void updateGroup(@NonNull GroupUpdateContainer groupUpdateContainer,
                            Callback<Void> callback) {
        GroupUpdateDTO groupUpdateDTO = GroupUpdateDTOMapper.from(groupUpdateContainer);

        groupService.updateGroup(groupUpdateDTO)
                .enqueue(callback);
    }

    public void createGroup(@NonNull GroupCreateContainer groupCreateContainer,
                            Callback<Long> callback) {
        GroupCreateDTO groupCreateDTO = GroupCreateDTOMapper.from(groupCreateContainer);

        groupService.createGroup(groupCreateDTO)
                .enqueue(callback);
    }

    public void deleteGroup(@NonNull Long id,
                            Callback<Void> callback) {
        groupService.deleteGroup(id)
                .enqueue(callback);
    }

    public void updateGroupAdmin(@NonNull Long groupId,
                                 @NonNull Long newAdminUserId,
                                 Callback<Void> callback) {
        GroupUpdateAdminDTO groupUpdateAdminDTO = GroupAdminDTOMapper.from(groupId, newAdminUserId);

        groupService.updateGroupAdmin(groupUpdateAdminDTO)
                .enqueue(callback);
    }

    public void updateGroupContribution(@NonNull GroupUpdateContainer groupUpdateContainer,
                                         Callback<Void> callback) {
        GroupUpdateDTO groupUpdateDTO = GroupUpdateDTOMapper.from(groupUpdateContainer);

        groupService.updateGroupContribution(groupUpdateDTO)
                .enqueue(callback);
    }


    public void deleteGroupContributions(@NonNull Long groupId,
                                         @NonNull Set<Long> contributionsToDeleteIds,
                                         Callback<Void> callback) {
        GroupContributionDeleteDTO groupContributionDeleteDTO = GroupContributionDeleteDTOMapper.from(groupId, contributionsToDeleteIds);

        groupService.deleteGroupContributions(groupContributionDeleteDTO)
                .enqueue(callback);
    }

    public void addSet(@NonNull Long groupId,
                       @NonNull Long setId,
                       Callback<Void> callback) {
        GroupSetDTO groupSetDTO = GroupSetDTOMapper.from(groupId, setId);

        groupService.addSet(groupSetDTO)
                .enqueue(callback);
    }

    public void removeSet(@NonNull Long groupId,
                          @NonNull Long setId,
                          Callback<Void> callback) {
        GroupSetDTO groupSetDTO = GroupSetDTOMapper.from(groupId, setId);

        groupService.removeSet(groupSetDTO)
                .enqueue(callback);
    }

    public void createSet(@NonNull Long groupId,
                          @NonNull SetCreateContainer setCreateContainer,
                          Callback<Void> callback) {
        GroupSetCreateDTO groupSetCreateDTO = GroupSetCreateDTOMapper.from(groupId, setCreateContainer);

        groupService.createSet(groupSetCreateDTO)
                .enqueue(callback);
    }

}
