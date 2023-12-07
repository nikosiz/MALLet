package com.mallet.mallet.backend.client.group.control;

/*import com.agh.api.GroupContributionDeleteDTO;
import com.agh.api.GroupCreateDTO;
import com.agh.api.GroupDTO;
import com.agh.api.GroupSetCreateDTO;
import com.agh.api.GroupSetDTO;
import com.agh.api.GroupUpdateAdminDTO;
import com.agh.api.GroupUpdateDTO;*/

import com.agh.api.GroupContributionDeleteDTO;
import com.agh.api.GroupCreateDTO;
import com.agh.api.GroupDTO;
import com.agh.api.GroupSetCreateDTO;
import com.agh.api.GroupSetDTO;
import com.agh.api.GroupUpdateAdminDTO;
import com.agh.api.GroupUpdateDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface GroupService {

    @GET(GroupServiceConstants.GROUP_PATH)
    Call<GroupDTO> getGroup(@Query(GroupServiceConstants.ID_PARAM) long id);

    @PUT(GroupServiceConstants.GROUP_CONTRIBUTION_PATH)
    Call<Void> updateGroup(@Body GroupUpdateDTO groupUpdateDTO);

    @PUT(GroupServiceConstants.GROUP_CONTRIBUTION_DELETE_PATH)
    Call<Void> deleteGroupContributions(@Body GroupContributionDeleteDTO GroupContributionDeleteDTO);

    @PUT(GroupServiceConstants.GROUP_ADMIN_PATH)
    Call<Void> updateGroupAdmin(@Body GroupUpdateAdminDTO groupUpdateAdminDTO);

    @POST(GroupServiceConstants.GROUP_PATH)
    Call<Long> createGroup(@Body GroupCreateDTO groupCreateDTO);

    @DELETE(GroupServiceConstants.GROUP_PATH)
    Call<Void> deleteGroup(@Query(GroupServiceConstants.ID_PARAM) long id);

    @PUT(GroupServiceConstants.GROUP_SET_PATH)
    Call<Void> addSet(@Body GroupSetDTO groupSetDTO);

    @HTTP(method = "DELETE", path = GroupServiceConstants.GROUP_SET_PATH, hasBody = true)
    //@DELETE(GroupServiceConstants.GROUP_SET_PATH)
    Call<Void> removeSet(@Body GroupSetDTO groupSetDTO);

    @POST(GroupServiceConstants.GROUP_SET_PATH)
    Call<Void> createSet(@Body GroupSetCreateDTO groupSetCreateDTO);

    @PUT(GroupServiceConstants.GROUP_CONTRIBUTION_PATH)
    Call<Void> updateGroupContribution(@Body GroupUpdateDTO groupUpdateDTO);


}
