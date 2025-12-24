package com.knowledge.microservice.model.dto.knowledge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LinkKnowledgeDto extends KnowledgeDto {

    @JsonProperty("url")
    private String url;

    @JsonProperty("description")
    private String description;
}
