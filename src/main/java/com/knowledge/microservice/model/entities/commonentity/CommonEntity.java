package com.knowledge.microservice.model.entities.commonentity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Rajveer Pratap Singh,
 * This class serves as a base entity for other entities in the system, providing common fields
 * such as creation and modification timestamps, and user identifiers for tracking changes.
 * It is annotated with JPA's @MappedSuperclass, indicating that it is not an entity itself, but
 * its properties are inherited by entities that extend it.
 * The class includes fields for tracking the user who created and last updated the entity,
 * as well as the timestamps for creation and last update. It also includes a version field
 * for optimistic locking to handle concurrent updates.
 * Lombok annotations are used to generate boilerplate code such as getters, setters,
 * constructors, and equals/hashCode methods.
 * Jackson annotations are used for JSON serialization and deserialization, ensuring
 * proper formatting of date and time fields.
 */

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class CommonEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    //Who's Who Columns

    @CreationTimestamp
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("creationTimeStamp")
    @Column(name = "Creation_date_Time")
    private LocalDateTime creationTimeStamp;


    @UpdateTimestamp
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("updationTimeStamp")
    @Column(name = "Updation_date_Time")
    private LocalDateTime updationTimeStamp;

}
