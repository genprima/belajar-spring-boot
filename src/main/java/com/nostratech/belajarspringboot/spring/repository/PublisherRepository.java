package com.nostratech.belajarspringboot.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nostratech.belajarspringboot.spring.model.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    @Modifying
    @Query("UPDATE Publisher p SET p.deleted = true WHERE p.id = :id")
    void deletePublisher(@Param("id") Long id);

    Publisher findByIdAndDeletedFalse(@Param("id") Long id);

}
