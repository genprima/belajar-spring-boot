package com.nostratech.spring.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.nostratech.spring.dto.AuthorDto;


@Service
public class AuthorService {

    private static List<AuthorDto> authors = new ArrayList<>();

    public void createAuthor(AuthorDto authorDto) {
        authors.add(new AuthorDto(authorDto.name(), authorDto.description()));
    }

    public List<AuthorDto> getAuthors() {
        return authors.stream()
                .map(a-> new AuthorDto(a.name(), a.description()))
                .toList();
    }

    public String getByIndex(int index) {
        return authors.get(index).name();
    }
}
