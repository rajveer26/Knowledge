package com.knowledge.microservice.service.knowledge.read.impl;

import com.knowledge.microservice.mapper.knowledge.KnowledgeMapper;
import com.knowledge.microservice.model.dto.knowledge.KnowledgeDto;
import com.knowledge.microservice.model.entities.knowledge.Knowledge;
import com.knowledge.microservice.model.repository.KnowledgeRepository;
import com.knowledge.microservice.service.knowledge.read.IKnowledgeReadService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("knowledgeReadServiceImpl")
@Log4j2(topic = "KnowledgeReadServiceImpl")
@Transactional(readOnly = true)
public class KnowledgeReadServiceImpl implements IKnowledgeReadService {

    private final KnowledgeRepository knowledgeRepository;
    private final KnowledgeMapper knowledgeMapper;

    @Autowired
    public KnowledgeReadServiceImpl(KnowledgeRepository knowledgeRepository, KnowledgeMapper knowledgeMapper) {
        super();
        this.knowledgeRepository = knowledgeRepository;
        this.knowledgeMapper = knowledgeMapper;
    }

    @Override
    public ResponseEntity<KnowledgeDto> getKnowledgeById(Long id) {
        return knowledgeRepository.findById(id)
                .map(k -> ResponseEntity.ok(knowledgeMapper.toDto(k)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @Override
    public ResponseEntity<Page<KnowledgeDto>> getAllKnowledge(Pageable pageable, String type, String query) {
        Page<Knowledge> knowledgePage = knowledgeRepository.findAllKnowledge(pageable, type, query);
        return ResponseEntity.ok(knowledgePage.map(knowledgeMapper::toDto));
    }
}
