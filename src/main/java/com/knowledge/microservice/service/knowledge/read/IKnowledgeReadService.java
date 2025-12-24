package com.knowledge.microservice.service.knowledge.read;

import com.knowledge.microservice.model.dto.knowledge.KnowledgeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IKnowledgeReadService {
    ResponseEntity<KnowledgeDto> getKnowledgeById(Long id);
    ResponseEntity<Page<KnowledgeDto>> getAllKnowledge(Pageable pageable, String type, String query);
}
