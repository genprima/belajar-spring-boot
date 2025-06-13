package com.nostratech.belajarspringboot.spring.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.nostratech.belajarspringboot.spring.dto.AuthorDto;
import com.nostratech.belajarspringboot.spring.dto.ResultPageResponseDTO;
import com.nostratech.belajarspringboot.spring.model.Author;
import com.nostratech.belajarspringboot.spring.repository.AuthorRepository;
import com.nostratech.belajarspringboot.spring.util.PaginationUtil;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Transactional
    public Boolean createAuthor(AuthorDto authorDto) {
        Author author = new Author();
        author.setName(authorDto.name());
        author.setDescription(authorDto.description());
        authorRepository.save(author);
        return Boolean.TRUE;
    }

    @Transactional(readOnly = true)
    public List<AuthorDto> getAuthors() {
        return authorRepository.findAll().stream()
                .map(a -> new AuthorDto(a.getName(), a.getDescription()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AuthorDto> findAuthors(String name) {
        name = name + "%";
        return authorRepository.findByName(name).stream()
                .map(a -> new AuthorDto(a.getName(), a.getDescription()))
                .toList();
    }

    @Transactional(readOnly = true)
    public ResultPageResponseDTO<AuthorDto> findAuthors(Integer page,
            Integer limit,
            String sortBy,
            String direction,
            String authorName) {

        authorName = !StringUtils.hasText(authorName) ? "%" : authorName + "%";
        Direction dir = PaginationUtil.getSortDirection(direction);

        Pageable pageable = PageRequest.of(page, limit, Sort.by(dir, sortBy));
        Page<Author> authors = authorRepository.findByNameLikeIgnoreCase(authorName, pageable);
        List<AuthorDto> authorDtos = authors.stream().map(a->{
            return new AuthorDto(a.getName(), a.getDescription());
        }).toList();
        return new ResultPageResponseDTO<>(authorDtos, authors.getTotalPages(), authors.getTotalElements());
    }
}
