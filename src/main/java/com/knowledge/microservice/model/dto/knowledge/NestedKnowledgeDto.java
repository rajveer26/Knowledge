package com.knowledge.microservice.model.dto.knowledge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class NestedKnowledgeDto extends KnowledgeDto {

    @JsonProperty("references")
    private List<KnowledgeDto> references = new LinkedList<>();
}
