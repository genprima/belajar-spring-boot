package com.nostratech.spring.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nostratech.spring.dto.AuthorDto;
import com.nostratech.spring.model.Author;
import com.nostratech.spring.repository.AuthorRepository;


@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void createAuthor(AuthorDto authorDto) {
        Author author = new Author();
        author.setName(authorDto.name());
        author.setDescription(authorDto.description());
        authorRepository.save(author);
    }

    public List<AuthorDto> getAuthors() {
        return authorRepository.findAll().stream()
                .map(a-> new AuthorDto(a.getName(), a.getDescription()))
                .toList();
    }

    public List<AuthorDto> findAuthors(String name) {
        name = name + "%";
        return authorRepository.findByName(name).stream()
                .map(a -> new AuthorDto(a.getName(), a.getDescription()))
                .toList();
    }
}
