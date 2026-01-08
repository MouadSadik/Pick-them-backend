package com.cigi.pickthem.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author $ {USERS}
 **/
@RestController
@RequestMapping("/api/v1")
public class HealthController {
    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}
