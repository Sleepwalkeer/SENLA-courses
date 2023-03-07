package eu.senla.service;

import eu.senla.dto.AuthenticationRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface AuthenticationService {
    Map authenticate(AuthenticationRequestDto request);

    void logout(HttpServletRequest request, HttpServletResponse response);

}
