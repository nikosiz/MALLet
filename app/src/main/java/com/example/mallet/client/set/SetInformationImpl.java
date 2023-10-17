package com.example.mallet.client.set;

import static com.example.mallet.utils.Language.EN;

import com.agh.api.SetInformationDTO;
import com.agh.api.TermDTO;
import com.example.mallet.client.configuration.RetrofitClient;

import java.util.List;

import retrofit2.Callback;

public class SetInformationImpl {

    private final SetInformationService setService;

    public SetInformationImpl() {
        this.setService = RetrofitClient.getRetrofitClient()
                .create(SetInformationService.class);
    }

    public void getSet(long id, String name, String description,
                       String creator,
                       List<TermDTO> terms, String nextChunkUri,
                       Callback<Void> callback) {
        SetInformationDTO setDTO = SetInformationDTOMapper.from(id, name, description, creator, terms, nextChunkUri);

        setService.getSet(id, 0, EN).enqueue(callback);
    }

}
