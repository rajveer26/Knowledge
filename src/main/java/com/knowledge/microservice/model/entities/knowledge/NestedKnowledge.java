package com.knowledge.microservice.model.entities.knowledge;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.LinkedList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue("NESTED")
@Data
public class NestedKnowledge extends Knowledge {

    @ManyToMany
    @JoinTable(
            name = "knowledge_relations",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    private List<Knowledge> references = new LinkedList<>();
}

