package com.oversoul.controller;

import com.oversoul.service.AmazonSESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @Autowired
    public AmazonSESService amazonSESService;

    @GetMapping("health-check")
    public String healthCheck() {
        return "it is up";
    }

    @GetMapping("mail")
    public String testMail() throws Exception {
        return amazonSESService.sendMail();
    }

}
