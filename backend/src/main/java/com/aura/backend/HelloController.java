package com.aura.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // Cho phép React truy cập
public class HelloController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Xin chào!";
    }
}