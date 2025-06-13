package com.nostratech.belajarspringboot.spring.config;
import java.security.Key;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.nostratech.belajarspringboot.spring.model.Author;

import io.jsonwebtoken.security.Keys;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;


@Configuration
@ComponentScan(basePackages = "com.nostratech")
public class AppConfig {

    public AppConfig(){
        
    }

    @Bean
    public Properties getProperties(){
        Properties prop = new Properties();
        prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.ssl.trust", "sandbox.smtp.mailtrap.io");
        return prop;
    }

    @Bean
    public Author author1(){
        Author author = new Author();
        author.setName("Gen Prima From Java Config");
        return author;
    }

    // @Bean
    // public Book book1(){
    //     Book book = new Book("How to be a good programmer from Java Config", author1());
    //     book.setTitle("Spring in Action 6th Edition");
    //     book.setAuthor(author1());
    //     return book;
    // }

    //This can be used in Annotation Base or Java Config
    @Bean
    public Session mailSession1(){
        Session session = Session.getInstance(getProperties(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return passwordAuthentication1();
            }
        });
        return session;
    }

    //This can be used in Annotation Base or Java Config
    @Bean
    public PasswordAuthentication passwordAuthentication1(){
        return new PasswordAuthentication("10d6a47ba0d890", "1ad49dba18e691");
    }

}