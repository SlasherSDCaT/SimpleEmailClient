package ru.belosludtsev.kursach.emailclient.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping("/ok")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("All is good");
    }
}
