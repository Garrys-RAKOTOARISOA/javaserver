package com.example.demo5.controllers;

import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.TestR;
import com.example.demo5.repositories.TestRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/api/test")
@CrossOrigin("*")
public class TestRController {
    private final TestRRepository testRRepository;

    @Autowired
    public TestRController(TestRRepository testRRepository){
        this.testRRepository = testRRepository;
    }

    @GetMapping("/settest/{message}")
    public String create(@PathVariable("message") String message){
        List<TestR> test = (List<TestR>) testRRepository.findAll();
        test.get(0).setMessage(message);
        testRRepository.save(test.get(0));
        return "Test inseree";
    }

    @GetMapping("/gettest")
    public TestR gettest(){
        return ((List<TestR>) testRRepository.findAll()).get(0);
    }
}
