package com.knowledge.microservice.service.knowledge.write;

import com.knowledge.microservice.model.dto.knowledge.KnowledgeDto;
import com.knowledge.microservice.model.input.knowledge.KnowledgeInput;
import org.springframework.http.ResponseEntity;

public interface IKnowledgeWriteService {
    ResponseEntity<KnowledgeDto> createKnowledge(KnowledgeInput knowledgeInput);
    ResponseEntity<KnowledgeDto> updateKnowledge(KnowledgeInput knowledgeInput);
    ResponseEntity<Void> deleteKnowledge(Long id);
}
