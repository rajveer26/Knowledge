package com.knowledge.microservice.mapper.knowledge;


import com.knowledge.microservice.model.dto.knowledge.*;
import com.knowledge.microservice.model.entities.knowledge.*;
import com.knowledge.microservice.model.input.knowledge.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.SubclassMapping;

@Mapper(componentModel = "spring")
public interface KnowledgeMapper {

    @SubclassMapping(source = LinkKnowledge.class, target = LinkKnowledgeDto.class)
    @SubclassMapping(source = TextKnowledge.class, target = TextKnowledgeDto.class)
    @SubclassMapping(source = QuoteKnowledge.class, target = QuoteKnowledgeDto.class)
    @SubclassMapping(source = NestedKnowledge.class, target = NestedKnowledgeDto.class)
    default KnowledgeDto toDto(Knowledge knowledge) {
        if (knowledge instanceof LinkKnowledge linkKnowledge) {
            return toDto(linkKnowledge);
        } else if (knowledge instanceof TextKnowledge textKnowledge) {
            return toDto(textKnowledge);
        } else if (knowledge instanceof QuoteKnowledge quoteKnowledge) {
            return toDto(quoteKnowledge);
        } else if (knowledge instanceof NestedKnowledge nestedKnowledge) {
            return toDto(nestedKnowledge);
        }
        return null;
    }

    LinkKnowledgeDto toDto(LinkKnowledge knowledge);
    TextKnowledgeDto toDto(TextKnowledge knowledge);
    QuoteKnowledgeDto toDto(QuoteKnowledge knowledge);
    NestedKnowledgeDto toDto(NestedKnowledge knowledge);

    @SubclassMapping(source = LinkKnowledgeInput.class, target = LinkKnowledge.class)
    @SubclassMapping(source = TextKnowledgeInput.class, target = TextKnowledge.class)
    @SubclassMapping(source = QuoteKnowledgeInput.class, target = QuoteKnowledge.class)
    @SubclassMapping(source = NestedKnowledgeInput.class, target = NestedKnowledge.class)
    default Knowledge toEntity(KnowledgeInput input) {
        if (input instanceof LinkKnowledgeInput linkKnowledgeInput) {
            return toEntity(linkKnowledgeInput);
        } else if (input instanceof TextKnowledgeInput textKnowledgeInput) {
            return toEntity(textKnowledgeInput);
        } else if (input instanceof QuoteKnowledgeInput quoteKnowledgeInput) {
            return toEntity(quoteKnowledgeInput);
        } else if (input instanceof NestedKnowledgeInput nestedKnowledgeInput) {
            return toEntity(nestedKnowledgeInput);
        }
        return null;
    }

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationTimeStamp", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updationTimeStamp", ignore = true)
    @Mapping(target = "recordVersion", ignore = true)
    LinkKnowledge toEntity(LinkKnowledgeInput input);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationTimeStamp", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updationTimeStamp", ignore = true)
    @Mapping(target = "recordVersion", ignore = true)
    TextKnowledge toEntity(TextKnowledgeInput input);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationTimeStamp", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updationTimeStamp", ignore = true)
    @Mapping(target = "recordVersion", ignore = true)
    QuoteKnowledge toEntity(QuoteKnowledgeInput input);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "creationTimeStamp", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updationTimeStamp", ignore = true)
    @Mapping(target = "recordVersion", ignore = true)
    @Mapping(target = "references", ignore = true)
    NestedKnowledge toEntity(NestedKnowledgeInput input);
}
