package com.example.mallet.client.userSet;

import com.agh.api.UserSetInformationDTO;
import com.example.mallet.client.configuration.RetrofitClient;

import retrofit2.Callback;

public class UserSetInformationImpl {

    private final UserSetInformationService userSetService;

    public UserSetInformationImpl() {
        this.userSetService = RetrofitClient.getRetrofitClient()
                .create(UserSetInformationService.class);
    }

    public void addSet(long id, String name, String description,
                       String creator,
                       UserSetInformationDTO.PermissionType permissionType, String nextChunkUri,
                       Callback<Void> callback) {
        UserSetInformationDTO userSetDTO = UserSetInformationDTOMapper.from(id, name, description, creator, permissionType, nextChunkUri);

        userSetService.addSet(userSetDTO).enqueue(callback);

    }

    public void getSet(long id, String name, String description,
                       String creator,
                       UserSetInformationDTO.PermissionType permissionType, String nextChunkUri,
                       Callback<Void> callback) {
        UserSetInformationDTO userSetDTO = UserSetInformationDTOMapper.from(id, name, description, creator, permissionType, nextChunkUri);

        userSetService.getSet(userSetDTO).enqueue(callback);
    }

}
