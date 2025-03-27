package com.nostratech.spring.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nostratech.spring.config.ApplicationProperties;

@Controller
@ResponseBody
@RequestMapping(value = "/v1/hello")
public class HelloResource {

    @Autowired
    private ApplicationProperties applicationProperties;


    @GetMapping()
    public String hello() {
        return "Hello " + applicationProperties.getName() +
               " with currency " + applicationProperties.getCurrency();
    }

    
}
