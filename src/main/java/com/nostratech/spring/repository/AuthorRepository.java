package com.nostratech.spring.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nostratech.spring.model.Author;


public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByNameLikeIgnoreCase(String name);

    @Query("SELECT a FROM Author a WHERE UPPER(a.name) LIKE UPPER(:name)") //JPQL
    public List<Author> findByName(@Param("name") String name);

}
