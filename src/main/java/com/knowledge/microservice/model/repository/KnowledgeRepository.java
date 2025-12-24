package com.knowledge.microservice.model.repository;

import com.knowledge.microservice.model.entities.knowledge.Knowledge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, Long> {

    Optional<Knowledge> findByTitle(String title);

    @Transactional(readOnly = true)
    @Query("""
        SELECT k FROM Knowledge k
        WHERE
            (:type IS NULL OR k.type = :type)
        AND
            (:query IS NULL OR
                lower(k.title) LIKE lower(concat('%', :query, '%'))
            )
        ORDER BY k.creationTimeStamp DESC
    """)
    Page<Knowledge> findAllKnowledge(
            Pageable pageable,
            @Param("type") String type,
            @Param("query") String query
    );
}
