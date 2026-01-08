package com.aerienne.gestion.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aerienne.gestion.entity.SimpleEntity;
import com.aerienne.gestion.repository.SimpleEntityRepository;

@RestController
@RequestMapping("/api/simple")
public class SimpleController {
    @Autowired
    private SimpleEntityRepository simpleEntityRepository;

    @GetMapping
    public List<SimpleEntity> getAll() {
        return simpleEntityRepository.findAll();
    }

    @PostMapping
    public SimpleEntity create(@RequestBody SimpleEntity entity) {
        return simpleEntityRepository.save(entity);
    }
}
