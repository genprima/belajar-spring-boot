package com.nostratech.belajarspringboot.spring.model;

import java.util.List;

import org.hibernate.annotations.SQLRestriction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.SequenceGenerator;

@Entity
@Table(name = "publisher")
@SQLRestriction("deleted = false")
public class Publisher {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "publisher_seq")
    @SequenceGenerator(name = "publisher_seq", sequenceName = "publisher_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "publisher")
    private List<Book> books;

    @Column(name = "deleted")
    private Boolean deleted;
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
