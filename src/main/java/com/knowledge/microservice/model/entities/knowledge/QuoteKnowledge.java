package com.knowledge.microservice.model.entities.knowledge;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("QUOTE")
@Data
public class QuoteKnowledge extends Knowledge {

    @Column(name = "quote")
    private String quote;

    @Column(name = "author")
    private String author;
}
