package com.example.mallet.client.set;

import com.agh.api.SetInformationDTO;
import com.agh.api.TermDTO;

import java.util.List;

public class SetInformationDTOMapper {

    private SetInformationDTOMapper() {
    }

    public static SetInformationDTO from(long id, String name, String description,
                                         String creator,
                                         List<TermDTO> terms, String nextChunkUri) {
        return SetInformationDTO.builder()
                .id(id)
                .name(name)
                .description(description)
                .creator(creator)
                .terms(terms)
                .nextChunkUri(nextChunkUri)
                .build();
    }

}
