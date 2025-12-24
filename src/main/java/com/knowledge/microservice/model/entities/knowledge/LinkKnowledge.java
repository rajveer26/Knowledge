package com.knowledge.microservice.model.entities.knowledge;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("LINK")
@Data
public class LinkKnowledge extends Knowledge {

    @Column(name = "url")
    private String url;

    @Column(name = "description")
    private String description;
}
