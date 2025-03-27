package com.nostratech.spring.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nostratech.spring.service.EmailService;

@RestController
@RequestMapping(value = "/v1/email")
public class EmailResource {

    private final EmailService emailService;

    public EmailResource(EmailService emailService) {
        this.emailService = emailService;
    }


    @PostMapping(value = "/send-mail")
    public String mail(@RequestBody String messages) {
        try {
            emailService.sendMail(messages);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "mail sent...";
    }

}
