package eu.senla.services;

import eu.senla.dto.AuthenticationRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface AuthenticationService {
    public Map authenticate(AuthenticationRequestDto request);

    public void logout(HttpServletRequest request, HttpServletResponse response);

}
