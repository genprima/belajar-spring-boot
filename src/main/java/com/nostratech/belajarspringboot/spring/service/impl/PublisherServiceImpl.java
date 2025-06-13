package com.nostratech.belajarspringboot.spring.service.impl;

import org.springframework.stereotype.Service;

import com.nostratech.belajarspringboot.spring.model.Publisher;
import com.nostratech.belajarspringboot.spring.repository.PublisherRepository;
import com.nostratech.belajarspringboot.spring.service.PublisherService;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public void deletePublisher(Long id) {
        Publisher publisher = publisherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Publisher not found"));
        publisher.setDeleted(true);
        publisherRepository.save(publisher);
    }

    @Override
    public Publisher findPublisher(Long id) {
        return publisherRepository.findByIdAndDeletedFalse(id);
    }

}
