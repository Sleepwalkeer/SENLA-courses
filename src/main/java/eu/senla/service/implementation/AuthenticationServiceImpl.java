package eu.senla.service.implementation;

import eu.senla.dto.AuthenticationRequestDto;
import eu.senla.entity.Credentials;
import eu.senla.repository.CredentialsRepository;
import eu.senla.security.JwtTokenProvider;
import eu.senla.service.AuthenticationService;
import eu.senla.service.JwtTokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final CredentialsRepository credentialsRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final JwtTokenBlacklistService jwtTokenBlacklistService;

    @Value("${jwt.header}")
    private String authorizationHeader;


    @Override
    public Map<Object, Object> authenticate(AuthenticationRequestDto request) {
        authenticationManager.authenticate
                (new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        Credentials credentials = credentialsRepository.findByUsername(request.getUsername()).
                orElseThrow(() -> new UsernameNotFoundException("User doesn't exist"));
        if (credentials.isDeleted()){
            throw new UsernameNotFoundException("Authorization Failed. Your account has been deleted");
        }
        String token = jwtTokenProvider.createToken(request.getUsername(), credentials.getRole().name(), credentials.getId());
        Map<Object, Object> response = new HashMap<>();
        response.put("username", request.getUsername());
        response.put("token", token);
        return response;
    }

    @Override
    public String logout(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        jwtTokenBlacklistService.blacklistToken(token);
        return "Logged out successfully.";

    }

    private String getTokenFromRequest(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }
}
