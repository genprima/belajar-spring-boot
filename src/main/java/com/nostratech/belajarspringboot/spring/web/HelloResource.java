package com.nostratech.belajarspringboot.spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

import com.nostratech.belajarspringboot.spring.config.ApplicationProperties;

@Controller
@RequestMapping(value = "/v1/hello")
public class HelloResource {

    private Logger logger = Logger.getLogger(HelloResource.class.getName());

    @Autowired
    private ApplicationProperties applicationProperties;

    @GetMapping()
    public String hello() {
        return "Hello " + applicationProperties.getName() +
               " with currency " + applicationProperties.getCurrency();
    }

    @GetMapping("/name")
    public String showNameForm() {
        return "author/name-form";
    }

    @PostMapping("/name")
    public String submitName(String name, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("name", name);
        return "redirect:/v1/hello/hallo";
    }

    @GetMapping("/hallo")
    public String hallo(Model model, HttpServletRequest request) {
        // Log all headers
        java.util.Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            logger.info("Header: " + headerName + " = " + headerValue);
        }
        
        // If no name is provided in the model, use a default
        if (!model.containsAttribute("name")) {
            model.addAttribute("name", "Guest");
        }
        return "author/hallo";
    }
}
