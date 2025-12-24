package com.knowledge.microservice.model.input.knowledge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class NestedKnowledgeInput extends KnowledgeInput {

    @JsonProperty("references")
    private List<KnowledgeInput> references = new LinkedList<>();
}
