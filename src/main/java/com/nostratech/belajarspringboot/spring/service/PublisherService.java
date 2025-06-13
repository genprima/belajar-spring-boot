package com.nostratech.belajarspringboot.spring.service;

import com.nostratech.belajarspringboot.spring.model.Publisher;

public interface PublisherService {

    void deletePublisher(Long id);

    Publisher findPublisher(Long id);

}
