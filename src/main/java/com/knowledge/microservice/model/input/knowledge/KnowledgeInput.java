package com.knowledge.microservice.model.input.knowledge;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LinkKnowledgeInput.class, name = "LINK"),
        @JsonSubTypes.Type(value = TextKnowledgeInput.class, name = "TEXT"),
        @JsonSubTypes.Type(value = QuoteKnowledgeInput.class, name = "QUOTE"),
        @JsonSubTypes.Type(value = NestedKnowledgeInput.class, name = "NESTED")
})
@EqualsAndHashCode(callSuper = false)
@Data
public abstract class KnowledgeInput {

    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Title is required")
    @JsonProperty("title")
    private String title;

    @JsonProperty("type")
    private String type;
}
