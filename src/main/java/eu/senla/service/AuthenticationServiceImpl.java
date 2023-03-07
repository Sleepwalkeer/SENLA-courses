package eu.senla.service;

import eu.senla.repository.CredentialsRepository;
import eu.senla.dto.AuthenticationRequestDto;
import eu.senla.entity.Credentials;
import eu.senla.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final CredentialsRepository credentialsRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public Map<Object, Object> authenticate(AuthenticationRequestDto request) {
        authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        Credentials credentials = credentialsRepository.findByUsername(request.getUsername()).
                orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
        String token = jwtTokenProvider.createToken(request.getUsername(), credentials.getRole().name(), credentials.getId());
        Map<Object, Object> response = new HashMap<>();
        response.put("username", request.getUsername());
        response.put("token", token);
        return response;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}
