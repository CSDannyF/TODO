package com.UCLL.TODO.controller;

import com.UCLL.TODO.controller.dto.AuthenticationRequest;
import com.UCLL.TODO.controller.dto.AuthenticationResponse;
import com.UCLL.TODO.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/sessions")
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        final var token = sessionService.authenticate(
                authenticationRequest.email(),
                authenticationRequest.password()
        );
        return new AuthenticationResponse(token);
    }
}
