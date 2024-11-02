package com.example.eventsmanager.security.auth.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

        @PostMapping("/signin")
        public String signin(@RequestBody SignInRequest signinRequest){
            return authenticationService.signin(signinRequest);
        }
    }