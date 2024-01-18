package com.example.demo5.controllers;

import com.example.demo5.fonc.Fonction;
import com.example.demo5.models.TestR;
import com.example.demo5.repositories.TestRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/test")
@CrossOrigin("*")
public class TestRController {
    private final TestRRepository testRRepository;

    @Autowired
    public TestRController(TestRRepository testRRepository){
        this.testRRepository = testRRepository;
    }

    @GetMapping("/create")
    public String create(){
        TestR testR = new TestR();
        Timestamp timestamp = Fonction.getCurrentTimestamp();
        testR.setTemps1(timestamp);
        System.out.println("temps="+timestamp);
        testRRepository.save(testR);
        return "Test inseree";
    }
}
