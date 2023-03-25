package eu.senla.service;

import eu.senla.dto.AuthenticationRequestDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface AuthenticationService {

    /**
     * Authenticates a user based on the provided authentication request.
     *
     * @param request An AuthenticationRequestDto object containing the user's authentication credentials.
     * @return A Map object containing information about the authenticated user, including their ID and any associated roles or permissions.
     */
    Map authenticate(AuthenticationRequestDto request);

    /**
     * Logs a user out of the system and invalidates their token.
     *
     * @param request The HttpServletRequest object representing the user's current token.
     * @return A String indicating the result of the logout operation.
     */
    String logout(HttpServletRequest request);
}
