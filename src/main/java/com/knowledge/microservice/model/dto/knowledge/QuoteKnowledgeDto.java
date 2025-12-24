package com.knowledge.microservice.model.dto.knowledge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuoteKnowledgeDto extends KnowledgeDto {

    @JsonProperty("quote")
    private String quote;

    @JsonProperty("author")
    private String author;
}
