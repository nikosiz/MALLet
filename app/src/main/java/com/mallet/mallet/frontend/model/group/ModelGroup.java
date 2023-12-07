package com.mallet.mallet.frontend.model.group;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class ModelGroup {
     Long id;
     String groupName;
     String identifier;
     String nrOfSets, nrOfMembers;

}