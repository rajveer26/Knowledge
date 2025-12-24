package com.knowledge.microservice.model.input.knowledge;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LinkKnowledgeInput extends KnowledgeInput {


    @NotBlank(message = "URL is required")
    @JsonProperty("url")
    private String url;

    @JsonProperty("description")
    private String description;
}
