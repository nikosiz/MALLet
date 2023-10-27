package com.example.mallet.backend.client.group.control;

import com.agh.api.GroupContributionDeleteDTO;
import com.agh.api.GroupCreateDTO;
import com.agh.api.GroupDTO;
import com.agh.api.GroupUpdateAdminDTO;
import com.agh.api.GroupUpdateDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface GroupService {

    @GET(GroupServiceConstants.GROUP_PATH)
    Call<GroupDTO> getGroup(@Query(GroupServiceConstants.ID_PARAM) long id);

    @PUT(GroupServiceConstants.GROUP_CONTRIBUTION_PATH)
    Call<Void> updateGroup(@Body GroupUpdateDTO groupUpdateDTO);

    @DELETE(GroupServiceConstants.GROUP_PATH)
    Call<Void> deleteGroupContributions(@Body GroupContributionDeleteDTO GroupContributionDeleteDTO);

    @PUT(GroupServiceConstants.GROUP_ADMIN_PATH)
    Call<Void> updateGroupAdmin(@Body GroupUpdateAdminDTO groupUpdateAdminDTO);

    @POST(GroupServiceConstants.GROUP_PATH)
    Call<Void> createGroup(@Body GroupCreateDTO groupCreateDTO);

    @POST(GroupServiceConstants.GROUP_PATH)
    Call<Void> deleteGroup(@Body long id);

}
