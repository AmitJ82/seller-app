package org.example.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class ExampleController {

    @GetMapping("/user/role")
    public String getUserRole(Authentication authentication) {
        String roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        System.out.println("Logged in user's role(s): " + roles);
        return "Logged in user's role(s): " + roles;
    }
}