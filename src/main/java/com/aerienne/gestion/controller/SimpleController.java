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
import com.aerienne.gestion.service.SimpleService;

@RestController
@RequestMapping("/api/simple")

public class SimpleController {
    @Autowired
    private SimpleEntityRepository simpleEntityRepository;

    @Autowired
    private SimpleService simpleService;


    @GetMapping
    public List<SimpleEntity> getAll() {
        return simpleEntityRepository.findAll();
    }

    // Endpoint pour récupérer le pourcentage depuis le service
    @GetMapping("/percentage-a")
    public double getPercentageNameStartsWithA() {
        return simpleService.percentageNameStartsWithA();
    }

    @PostMapping
    public SimpleEntity create(@RequestBody SimpleEntity entity) {
        return simpleEntityRepository.save(entity);
    }
}
