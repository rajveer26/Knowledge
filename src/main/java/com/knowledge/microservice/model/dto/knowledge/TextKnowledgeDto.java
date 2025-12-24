package com.knowledge.microservice.model.dto.knowledge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TextKnowledgeDto extends KnowledgeDto {

    @JsonProperty("content")
    private String content;
}
