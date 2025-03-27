package com.nostratech.spring.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nostratech.spring.config.EmailService;

@Controller
@ResponseBody
@RequestMapping(value = "/v1/hello")
public class HelloResource {

    private final EmailService emailService;

    public HelloResource(EmailService emailService) {
        this.emailService = emailService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String hello() {
        return "hello";
    }

    @RequestMapping(value = "/send-mail", method = RequestMethod.POST)
    public String mail(@RequestBody String messages) {
        try {
            emailService.sendMail(messages);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "mail sent";
    }
}
