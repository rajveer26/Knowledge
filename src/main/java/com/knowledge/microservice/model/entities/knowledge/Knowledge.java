package com.knowledge.microservice.model.entities.knowledge;

import com.knowledge.microservice.model.entities.commonentity.CommonEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "knowledge")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
        name = "knowledge_type",
        discriminatorType = DiscriminatorType.STRING
)
@Data
@DynamicInsert
@DynamicUpdate
public abstract class Knowledge extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(name = "knowledge_type", insertable = false, updatable = false)
    private String type;

}
