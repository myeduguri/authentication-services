package com.packtpub.authenticationservices.adapter.transportlayers.restapi.controller;

import com.packtpub.authenticationservices.adapter.transportlayers.restapi.dto.request.AuthenticationRequest;
import com.packtpub.authenticationservices.adapter.transportlayers.restapi.dto.response.AuthenticationResponse;
import com.packtpub.authenticationservices.adapter.transportlayers.restapi.dto.response.AuthenticationUserResponse;
import com.packtpub.authenticationservices.internal.entities.Authentication;
import com.packtpub.authenticationservices.internal.usecases.GenerateTokenUseCase;
import com.packtpub.authenticationservices.internal.usecases.ValidateTokenUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping("/v1/api/auth")
@RestController
@Slf4j
public class AuthenticationController {

    private final GenerateTokenUseCase generateTokenUseCase;
    private final ValidateTokenUseCase validateTokenUseCase;

    public AuthenticationController(GenerateTokenUseCase generateTokenUseCase, ValidateTokenUseCase validateTokenUseCase) {
        this.generateTokenUseCase = generateTokenUseCase;
        this.validateTokenUseCase = validateTokenUseCase;
    }

    @PostMapping
    public Mono<AuthenticationResponse> createAuthenticationToken(@RequestBody Mono<AuthenticationRequest> authenticationRequestMono) {
    System.out.println(" In COntroller ");	
        return authenticationRequestMono
                .flatMap(authenticationRequest ->
                        generateTokenUseCase.execute(authenticationRequest.getUsername(), authenticationRequest.getPassword())
                                .map(AuthenticationResponse::new)
                );
    }

    @GetMapping("/validate")
    public ResponseEntity<AuthenticationUserResponse> validateToken(@RequestParam String token) {
        final Authentication authentication = validateTokenUseCase.execute(token);
        return ResponseEntity.ok(new AuthenticationUserResponse(authentication.getUsername(), authentication.getRoles()));
    }

}
