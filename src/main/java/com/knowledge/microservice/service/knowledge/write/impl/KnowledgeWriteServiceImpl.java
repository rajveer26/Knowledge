package com.knowledge.microservice.service.knowledge.write.impl;

import com.knowledge.microservice.mapper.knowledge.KnowledgeMapper;
import com.knowledge.microservice.model.dto.knowledge.KnowledgeDto;
import com.knowledge.microservice.model.entities.knowledge.Knowledge;
import com.knowledge.microservice.model.input.knowledge.KnowledgeInput;
import com.knowledge.microservice.model.repository.KnowledgeRepository;
import com.knowledge.microservice.service.knowledge.write.IKnowledgeWriteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("knowledgeWriteServiceImpl")
@Log4j2(topic = "KnowledgeWriteServiceImpl")
@Transactional
public class KnowledgeWriteServiceImpl implements IKnowledgeWriteService {

    private final KnowledgeRepository knowledgeRepository;
    private final KnowledgeMapper knowledgeMapper;

    @Autowired
    public KnowledgeWriteServiceImpl(KnowledgeRepository knowledgeRepository, KnowledgeMapper knowledgeMapper) {
        super();
        this.knowledgeRepository = knowledgeRepository;
        this.knowledgeMapper = knowledgeMapper;
    }

    @Override
    public ResponseEntity<KnowledgeDto> createKnowledge(KnowledgeInput knowledgeInput) {
        Knowledge knowledge = knowledgeMapper.toEntity(knowledgeInput);
        Knowledge savedKnowledge = knowledgeRepository.save(knowledge);
        return new ResponseEntity<>(knowledgeMapper.toDto(savedKnowledge), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<KnowledgeDto> updateKnowledge(KnowledgeInput knowledgeInput) {
        if (knowledgeInput.getId() == null) {
            throw new EntityNotFoundException("Knowledge ID is required for update");
        }
        
        Knowledge existingKnowledge = knowledgeRepository.findById(knowledgeInput.getId())
                .orElseThrow(() -> new EntityNotFoundException("Knowledge not found with id: " + knowledgeInput.getId()));

        Knowledge knowledgeToSave = knowledgeMapper.toEntity(knowledgeInput);
        knowledgeToSave.setId(existingKnowledge.getId());

        Knowledge savedKnowledge = knowledgeRepository.save(knowledgeToSave);
        return ResponseEntity.ok(knowledgeMapper.toDto(savedKnowledge));
    }

    @Override
    public ResponseEntity<Void> deleteKnowledge(Long id) {
        if (!knowledgeRepository.existsById(id)) {
            throw new EntityNotFoundException("Knowledge not found with id: " + id);
        }
        knowledgeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
