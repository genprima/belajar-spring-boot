package com.nostratech.belajarspringboot.spring.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nostratech.belajarspringboot.spring.dto.AuthorDto;
import com.nostratech.belajarspringboot.spring.dto.ResultPageResponseDTO;
import com.nostratech.belajarspringboot.spring.service.AuthorService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("v1/authors")
public class AuthorResource {

    private final AuthorService authorService;

    public AuthorResource(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<String> createAuthorWeb(@RequestBody @Valid AuthorDto authorDto) {
        return ResponseEntity.created(URI.create("/v1/authors")).build();
    }

    @PostMapping("/create")
    public Boolean createAuthor(@RequestBody AuthorDto authorDto) {
        return authorService.createAuthor(authorDto);
        
    }
    
    @GetMapping
    public ResponseEntity<List<AuthorDto>> findAuthors(@RequestParam(value = "name", required = false) String name) {
        return ResponseEntity.ok(authorService.findAuthors(name));
    }

    @GetMapping("/page")
    public ResponseEntity<ResultPageResponseDTO<AuthorDto>> listAuthors(
                                            @RequestParam(value = "pages", defaultValue = "0") Integer pages,
                                            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                                            @RequestParam(value = "direction", defaultValue = "asc") String direction,
                                            @RequestParam(value = "authorName", defaultValue = "") String authorName) {
        ResultPageResponseDTO<AuthorDto> result = authorService.findAuthors(pages, limit, sortBy, direction, authorName);
        return ResponseEntity.ok(result);
    }

}
