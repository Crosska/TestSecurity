package com.crosska.security.TestSecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ApiController {

    @GetMapping("public")
    public String publicEndpoint() {
        return "This is public endpoint";
    }

    @GetMapping("user")
    public String userEndpoint() {
        return "This is user endpoint";
    }

    @GetMapping("admin")
    public String adminEndpoint() {
        return "This is admin endpoint";
    }

}
