package com.knowledge.microservice.model.entities.knowledge;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("TEXT")
@Data
public class TextKnowledge extends Knowledge {

    @Column(name = "content", length = 5000)
    private String content;
}
