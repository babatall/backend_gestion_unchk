package com.tall.GestionUnchk.repository;

import com.tall.GestionUnchk.entity.Document;
import com.tall.GestionUnchk.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository
        extends JpaRepository<Document, Long> {

    List<Document> findByType(DocumentType type);

}