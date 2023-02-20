package eu.senla.controllers;

import eu.senla.dao.CredentialsDao;
import eu.senla.dto.AuthenticationRequestDto;
import eu.senla.entities.Credentials;
import eu.senla.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private CredentialsDao credentialsDao;
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate (@RequestBody AuthenticationRequestDto request){
        try {
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
            Credentials credentials = credentialsDao.findByUsername(request.getUsername()).
                    orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
            String token = jwtTokenProvider.createToken(request.getUsername(), credentials.getRole().name());
            Map<Object,Object> response = new HashMap<>();
            response.put("username",request.getUsername());
            response.put("token", token);

            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email and/or password", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response){
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request,response,null);
    }
}
