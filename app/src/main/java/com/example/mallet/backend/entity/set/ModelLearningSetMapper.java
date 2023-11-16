package com.example.mallet.backend.entity.set;

import com.agh.api.SetInformationDTO;
import com.example.mallet.utils.ModelLearningSet;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModelLearningSetMapper {

    private ModelLearningSetMapper() {}

    public static List<ModelLearningSet> from(Collection<SetInformationDTO> setInformationDTOS){
        return setInformationDTOS.stream()
                .map(ModelLearningSetMapper::from)
                .collect(Collectors.toList());
    }

    public static ModelLearningSet from(SetInformationDTO setInformationDTO){
        if(Objects.isNull(setInformationDTO.creator())){
            return new ModelLearningSet(
                    setInformationDTO.id(),
                    setInformationDTO.name(),
                    setInformationDTO.identifier(),
                    setInformationDTO.description(),
                    "",
                    setInformationDTO.numberOfTerms());
        }
        return new ModelLearningSet(
                setInformationDTO.id(),
                setInformationDTO.name(),
                setInformationDTO.identifier(),
                setInformationDTO.description(),
                setInformationDTO.creator().name(),
               setInformationDTO.numberOfTerms());
    }
}