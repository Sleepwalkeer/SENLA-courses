package eu.senla.controller;

import eu.senla.dto.AuthenticationRequestDto;
import eu.senla.service.implementation.AuthenticationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;


    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequestDto request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        authenticationService.logout(request);
        return ResponseEntity.ok(authenticationService.logout(request));
    }
}