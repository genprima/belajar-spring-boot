package com.nostratech.spring.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nostratech.spring.dto.AuthorDto;
import com.nostratech.spring.service.AuthorService;

@RestController
@RequestMapping("v1/authors")
public class AuthorResource {

    private final AuthorService authorService;

    public AuthorResource(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<List<AuthorDto>> findAuthors(@RequestParam(value = "name", required = false) String name) {
        return ResponseEntity.ok(authorService.findAuthors(name));
    }

}
