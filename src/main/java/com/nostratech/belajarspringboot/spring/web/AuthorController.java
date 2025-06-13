package com.nostratech.belajarspringboot.spring.web;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.nostratech.belajarspringboot.spring.dto.AuthorDto;
import com.nostratech.belajarspringboot.spring.service.AuthorService;

@Controller
@RequestMapping("/author")
public class AuthorController {
    

    private Logger logger = Logger.getLogger(AuthorController.class.getName());

    @Autowired
    private AuthorService authorService;

    @GetMapping("new")
    public String createAuthor(Model model) {
        logger.info("form called: ");
        model.addAttribute("authorDto", new AuthorDto("", ""));

        return "author/create-author";
    }

    @GetMapping("result")
    public String resultAuthor(Model model) {
        // List<String> authorNames = authorService.getAuthorNames();
        // AuthorDto authorDto = new AuthorDto(authorNames.get(0));
        // model.addAttribute("authorDto", authorDto);
        return "author/author-result";
    }

    @PostMapping("new")
    public String submitForm(@ModelAttribute AuthorDto authorDto, RedirectAttributes redirectAttributes) {
        logger.info("Creating new author: " + authorDto);
        authorService.createAuthor(authorDto);
        redirectAttributes.addFlashAttribute("authorDto", authorDto);
        return "redirect:/author/list"; // redirect to API
    }

    @GetMapping("list")
    public String listAuthors(Model model) {
        List<AuthorDto> authors = authorService.getAuthors();
        model.addAttribute("authors", authors);
        return "author/author-list";
    }

    
}
