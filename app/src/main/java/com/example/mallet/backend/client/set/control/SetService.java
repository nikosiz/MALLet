package com.example.mallet.backend.client.set.control;

import com.agh.api.Language;
import com.agh.api.SetBasicDTO;
import com.agh.api.SetDetailDTO;

import java.util.Set;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SetService {

    @GET(SetServiceConstants.SET_PATH)
    Call<SetDetailDTO> getSet(@Query(SetServiceConstants.ID_PARAM) long id);

    @GET(SetServiceConstants.SET_BASIC_PATH)
    Call<SetBasicDTO> getBasicSet(@Query(SetServiceConstants.START_POSITION_PARAM) long startPosition,
                                  @Query(SetServiceConstants.LIMIT_PARAM) long limit,
                                  @Query(SetServiceConstants.LANGUAGE_PARAM) Language language);

    @GET(SetServiceConstants.SET_BASIC_PATH)
    Call<SetBasicDTO> getBasicSet(@Query(SetServiceConstants.IDS_PARAM) Set<Long> ids);

    @GET(SetServiceConstants.SET_BASIC_PATH)
    Call<SetBasicDTO> getBasicSet(@Query(SetServiceConstants.TOPIC_PARAM) String topic);
}
