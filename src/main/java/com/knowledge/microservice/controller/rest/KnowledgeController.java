package com.knowledge.microservice.controller.rest;

import com.knowledge.microservice.model.dto.knowledge.KnowledgeDto;
import com.knowledge.microservice.model.input.knowledge.KnowledgeInput;
import com.knowledge.microservice.service.knowledge.read.IKnowledgeReadService;
import com.knowledge.microservice.service.knowledge.write.IKnowledgeWriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/knowledge")
@RequiredArgsConstructor
public class KnowledgeController {

    private final IKnowledgeReadService knowledgeReadService;
    private final IKnowledgeWriteService knowledgeWriteService;

    @GetMapping("/{id}")
    public ResponseEntity<KnowledgeDto> getKnowledgeById(@PathVariable Long id) {
        return knowledgeReadService.getKnowledgeById(id);
    }

    @GetMapping("/get-all-knowledge")
    public ResponseEntity<Page<KnowledgeDto>> getAllKnowledge(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String query
    ) {
        return knowledgeReadService.getAllKnowledge(pageable, type, query);
    }

    @PostMapping("/create-knowledge")
    public ResponseEntity<KnowledgeDto> createKnowledge(@RequestBody KnowledgeInput knowledgeInput) {
        return knowledgeWriteService.createKnowledge(knowledgeInput);
    }

    @PutMapping("/update-knowledge")
    public ResponseEntity<KnowledgeDto> updateKnowledge(@Valid @RequestBody KnowledgeInput knowledgeInput) {
        return knowledgeWriteService.updateKnowledge(knowledgeInput);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKnowledge(@PathVariable Long id) {
        return knowledgeWriteService.deleteKnowledge(id);
    }
}
