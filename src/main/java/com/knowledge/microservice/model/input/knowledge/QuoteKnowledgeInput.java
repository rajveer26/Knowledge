package com.knowledge.microservice.model.input.knowledge;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuoteKnowledgeInput extends KnowledgeInput {

    @JsonProperty("quote")
    @NotBlank(message = "Quote is required")
    private String quote;

    @JsonProperty("author")
    private String author;
}
